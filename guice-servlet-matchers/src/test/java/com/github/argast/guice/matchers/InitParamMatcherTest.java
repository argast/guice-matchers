package com.github.argast.guice.matchers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.hamcrest.StringDescription;
import org.junit.Test;

import com.google.inject.servlet.ServletModuleBinding;

public class InitParamMatcherTest {

	private static final String DEFAULT_PARAM_VALUE = "paramValue";
	private static final String DEFAULT_PARAM_KEY = "paramKey";

	@Test
	public void testThatMatcherReturnsTrueWhenBindingHasParamWithCorrectValue() throws Exception {
		assertTrue(matchingResultOf(bindingThatReturns(Collections.singletonMap(DEFAULT_PARAM_KEY, DEFAULT_PARAM_VALUE))));
	}
	
	@Test
	public void testThatMatcherReturnsFalseWhenBindingHasNullParams() throws Exception {
		assertFalse(matchingResultOf(bindingThatReturns(null)));
	}
	
	@Test
	public void testThatMatcherReturnsFalseWhenBindingHasDifferentParam() throws Exception {
		assertFalse(matchingResultOf(bindingThatReturns(Collections.singletonMap("otherParams", "someValue"))));
	}

	@Test
	public void testThatMatcherReturnsFalseWhenBindingHasParamWithDifferentValue() throws Exception {
		assertFalse(matchingResultOf(bindingThatReturns(Collections.singletonMap(DEFAULT_PARAM_KEY, "otherValue"))));
	}
	
	@Test
	public void testThatMatcherDescriptionIsCorrect() throws Exception {
		StringDescription description = new StringDescription();
		createMatcher().describeTo(description);
		String expectedDescription = String.format("binding containing init parameter: \"%s\" = \"%s\"", DEFAULT_PARAM_KEY, DEFAULT_PARAM_VALUE);
		assertEquals(expectedDescription, description.toString());
	}

	private ServletModuleBinding bindingThatReturns(Map<String, String> paramMap) {
		ServletModuleBinding binding = mock(ServletModuleBinding.class);
		when(binding.getInitParams()).thenReturn(paramMap);
		return binding;
	}
	
	private boolean matchingResultOf(ServletModuleBinding binding) {
		return createMatcher().matchesSafely(binding);
	}

	private InitParamMatcher createMatcher() {
		return new InitParamMatcher(DEFAULT_PARAM_KEY, DEFAULT_PARAM_VALUE);
	}
}
