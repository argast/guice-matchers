package com.github.argast.guice.matchers;

import static org.junit.Assert.assertEquals;
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

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.Key;
import com.google.inject.servlet.LinkedFilterBinding;

public class FilterClassMatcherTest {

	private FilterClassMatcher matcher = new FilterClassMatcher(null);

	@Test
	public void testThatLinkedFilterBindingIsACorrectTypeForFilterClassMatcher() throws Exception {
		LinkedFilterBinding binding = mock(LinkedFilterBinding.class);
		assertTrue(matcher.correctBindingType(binding));
	}
	
	@Test
	public void testThatCorrectClassInstanceIfExtractedFromBinding() throws Exception {
		LinkedFilterBinding binding = mock(LinkedFilterBinding.class);
		when(binding.getLinkedKey()).thenAnswer(new Answer<Key<?>>() {
			public Key<?> answer(InvocationOnMock invocation) throws Throwable {
				return Key.get(Filter1.class);
			}
		});
		assertEquals(Filter1.class, matcher.extractClass(binding));
	}
	
	@Test
	public void testThatCorrectInitialDescriptionIsReturndFromFilterClassMatcher() throws Exception {
		assertEquals("filter", matcher.initialDescription());
	}

	private class Filter1 implements Filter {
		public void destroy() {}
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {}
		public void init(FilterConfig filterConfig) throws ServletException {}
	}
	
}
