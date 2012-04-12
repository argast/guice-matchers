package com.github.argast.guice.matchers;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.inject.servlet.ServletModuleBinding;

public class InitParamMatcher extends TypeSafeMatcher<ServletModuleBinding> {

	private final String key;
	private final String value;

	public InitParamMatcher(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public void describeTo(Description d) {
		d.appendValue(key).appendValue(value);
	}
	
	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		return binding.getInitParams().containsKey(key) && binding.getInitParams().get(key).equals(value);
	}
}
