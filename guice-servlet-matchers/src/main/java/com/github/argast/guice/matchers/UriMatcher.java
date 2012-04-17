package com.github.argast.guice.matchers;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.inject.servlet.ServletModuleBinding;

public class UriMatcher extends TypeSafeMatcher<ServletModuleBinding> {

	private final String uri;

	public UriMatcher(String uri) {
		this.uri = uri;
	}
	
	public void describeTo(Description d) {
		d.appendText("binding matching uri: ").appendValue(uri);
	}
	
	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		return binding.matchesUri(uri);
	}
}
