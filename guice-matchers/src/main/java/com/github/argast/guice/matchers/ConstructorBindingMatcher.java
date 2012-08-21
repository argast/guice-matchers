package com.github.argast.guice.matchers;

import com.google.inject.Key;
import com.google.inject.spi.ConstructorBinding;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ConstructorBindingMatcher extends TypeSafeMatcher<ConstructorBinding<?>> {

	private final Key<?> key;

	public ConstructorBindingMatcher(Key<?> key) {
		this.key = key;
	}
	
	public void describeTo(Description d) {
        d.appendText("constructor binding to " + key.toString());
	}
	
	public boolean matchesSafely(ConstructorBinding<?> item) {
		return item.getKey().equals(key);
	}
}
