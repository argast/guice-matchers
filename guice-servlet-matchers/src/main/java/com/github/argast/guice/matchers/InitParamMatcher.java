package com.github.argast.guice.matchers;

import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.inject.servlet.ServletModuleBinding;

public class InitParamMatcher extends TypeSafeMatcher<ServletModuleBinding> {

	private final String key;
	private final String value;

	public InitParamMatcher(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public void describeTo(Description d) {
		d.appendText("binding containing init parameter: ").appendValue(key).appendText(" = ").appendValue(value);
	}
	
	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		Map<String, String> initParams = binding.getInitParams();
		return initParams != null && initParams.containsKey(key) && initParams.get(key).equals(value);
	}
}
