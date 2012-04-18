package com.github.argast.guice.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.Key;
import com.google.inject.servlet.InstanceFilterBinding;
import com.google.inject.servlet.LinkedFilterBinding;
import com.google.inject.servlet.ServletModuleBinding;

public class FilterClassMatcherTest {

	private class Filter1 implements Filter {
		public void destroy() {}
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {}
		public void init(FilterConfig filterConfig) throws ServletException {}
	}

	private class Filter2 implements Filter {
		public void destroy() {}
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {}
		public void init(FilterConfig filterConfig) throws ServletException {}
	}
	
	@Test
	public void testThatMatcherReturnsTrueIfClassIsCorrect() throws Exception {
		assertTrue(matchResultOf(bindingThatReturns(Filter1.class)));
	}

	@Test
	public void testThatMatcherReturnsFalseIfClassIsDifferent() throws Exception {
		assertFalse(matchResultOf(bindingThatReturns(Filter2.class)));
	}
	
	@Test
	public void testThatMatcherRetunsFalseIfBindingTypeIsNotLinkedFilterBinding() throws Exception {
		InstanceFilterBinding binding = mock(InstanceFilterBinding.class);
		when(binding.getFilterInstance()).thenReturn(new Filter1());
		assertFalse(matchResultOf(binding));
	}
	
	@Test
	public void testThatDescriptionIsCorrect() throws Exception {
		StringDescription description = new StringDescription();
		createFilter().describeTo(description);
		assertEquals("filter class equal to \"" + Filter1.class.getName() + "\"", description.toString());
	}

	private boolean matchResultOf(ServletModuleBinding binding) {
		return createFilter().matchesSafely(binding);
	}

	private FilterClassMatcher createFilter() {
		return new FilterClassMatcher(Filter1.class);
	}
	
	private LinkedFilterBinding bindingThatReturns(final Class<?> filterClass) {
		LinkedFilterBinding binding = mock(LinkedFilterBinding.class);
		when(binding.getLinkedKey()).thenAnswer(new Answer<Key<?>>() {
			public Key<?> answer(InvocationOnMock invocation) throws Throwable {
				return Key.get(filterClass);
			}
		});
		return binding;
	}
}
