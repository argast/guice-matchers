package com.github.argast.guice.matchers;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.inject.servlet.ServletModuleBinding;

public class PatternMatcher extends TypeSafeMatcher<ServletModuleBinding> {

	private final String pattern;

	public PatternMatcher(String pattern) {
		this.pattern = pattern;
	}
	
	public void describeTo(Description d) {
		d.appendText("pattern ").appendValue(pattern);
	}
	
	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		return pattern.equals(binding.getPattern());
	}
}
