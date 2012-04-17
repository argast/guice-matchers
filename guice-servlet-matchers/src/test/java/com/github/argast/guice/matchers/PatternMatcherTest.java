package com.github.argast.guice.matchers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hamcrest.StringDescription;
import org.junit.Test;

import com.google.inject.servlet.ServletModuleBinding;

public class PatternMatcherTest {


	private static final String URI_PATTERN = "/uri/pattern";

	@Test
	public void testThatMatcherReturnsTrueWhenPatternsAreEqual() throws Exception {
		ServletModuleBinding binding = mockBindingToReturnPattern(URI_PATTERN);
		assertTrue(createMatcher().matchesSafely(binding));
	}

	@Test
	public void testThatMatcherReturnsFalseWhenPatternsAreNotEqual() throws Exception {
		ServletModuleBinding binding = mockBindingToReturnPattern("/some/other/pattern");
		assertFalse(createMatcher().matchesSafely(binding));
	}

	@Test
	public void testThatMatcherHasCorrectDescription() throws Exception {
		StringDescription description = new StringDescription();
		createMatcher().describeTo(description);
		assertEquals("pattern equal to \"" + URI_PATTERN + "\"", description.toString());
	}
	
	private PatternMatcher createMatcher() {
		return new PatternMatcher(URI_PATTERN);
	}

	private ServletModuleBinding mockBindingToReturnPattern(String pattern) {
		ServletModuleBinding binding = mock(ServletModuleBinding.class);
		when(binding.getPattern()).thenReturn(pattern);
		return binding;
	}
}
