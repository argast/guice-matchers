package com.github.argast.guice.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.inject.servlet.ServletModuleBinding;

public class PatternMatcher extends TypeSafeMatcher<ServletModuleBinding> {

	private final String pattern;

	public PatternMatcher(String pattern) {
		this.pattern = pattern;
	}
	
	public void describeTo(Description d) {
		d.appendText("pattern equal to ").appendValue(pattern);
	}
	
	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		return pattern.equals(binding.getPattern());
	}
}
