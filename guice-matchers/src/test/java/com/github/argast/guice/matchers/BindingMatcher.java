package com.github.argast.guice.matchers;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.inject.Binding;
import com.google.inject.Key;

public class BindingMatcher extends TypeSafeMatcher<Binding<?>> {

	private final Key<?> key;

	public BindingMatcher(Key<?> key) {
		this.key = key;
	}
	
	public void describeTo(Description d) {
		d.appendText("binding with key: ").appendValue(key);
	}
	
	@Override
	public boolean matchesSafely(Binding<?> item) {
		return item.getKey().equals(key);
	}
	
}
