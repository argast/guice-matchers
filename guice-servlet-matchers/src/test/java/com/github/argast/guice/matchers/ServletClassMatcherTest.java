package com.github.argast.guice.matchers;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServlet;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.Key;
import com.google.inject.servlet.LinkedServletBinding;

@SuppressWarnings("serial")
public class ServletClassMatcherTest {

	private class Servlet1 extends HttpServlet {}
	private class Servlet2 extends HttpServlet {}
	
	@Test
	public void testThatMatcherReturnsTrueWhenClassesMatch() throws Exception {
		assertTrue(new ServletClassMatcher(Servlet1.class).matchesSafely(bindingThatReturnsServlet(Servlet1.class)));
	}
	
	@Test
	public void testThatMatcherReturnsTrueWhenClassesDoNotMatch() throws Exception {
		assertFalse(new ServletClassMatcher(Servlet1.class).matchesSafely(bindingThatReturnsServlet(Servlet2.class)));
	}
	
	private LinkedServletBinding bindingThatReturnsServlet(final Class<? extends HttpServlet> servletClass) {
		LinkedServletBinding binding = mock(LinkedServletBinding.class);
		when(binding.getLinkedKey()).thenAnswer(new Answer<Key<?>>() {
			public Key<?> answer(InvocationOnMock invocation) throws Throwable {
				return Key.get(servletClass);
			}
		});
		return binding;
	}
	
}
