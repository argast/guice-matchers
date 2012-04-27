package com.github.argast.guice.matchers;

import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.inject.servlet.ServletModuleBinding;

public class InitParamsMatcher extends TypeSafeMatcher<ServletModuleBinding> {

	private final Map<String, String> params;

	public InitParamsMatcher(Map<String, String> params) {
		this.params = params;
	}

	public void describeTo(Description description) {
		description.appendText("binding with init parameters:\n");
		for (Map.Entry<String, String> e: params.entrySet()) {
			description.appendText("- ").appendText(e.getKey()).appendText(" = ").appendText(e.getValue()).appendText("\n");
		}
	}
	
	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		return params.equals(binding.getInitParams());
	}
}
