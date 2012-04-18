package com.github.argast.guice.matchers;

import static org.junit.Assert.assertEquals;
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
	
	private ServletClassMatcher matcher = new ServletClassMatcher(null);

	@Test
	public void testThatLinkedFilterBindingIsACorrectTypeForFilterClassMatcher() throws Exception {
		LinkedServletBinding binding = mock(LinkedServletBinding.class);
		assertTrue(matcher.correctBindingType(binding));
	}
	
	@Test
	public void testThatCorrectClassInstanceIfExtractedFromBinding() throws Exception {
		LinkedServletBinding binding = mock(LinkedServletBinding.class);
		when(binding.getLinkedKey()).thenAnswer(new Answer<Key<?>>() {
			public Key<?> answer(InvocationOnMock invocation) throws Throwable {
				return Key.get(Servlet1.class);
			}
		});
		assertEquals(Servlet1.class, matcher.extractClass(binding));
	}
	
	@Test
	public void testThatCorrectInitialDescriptionIsReturndFromFilterClassMatcher() throws Exception {
		assertEquals("servlet", matcher.initialDescription());
	}
}
