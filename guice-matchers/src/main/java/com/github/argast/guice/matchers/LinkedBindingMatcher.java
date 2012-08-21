package com.github.argast.guice.matchers;

import com.google.inject.Key;
import com.google.inject.spi.LinkedKeyBinding;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class LinkedBindingMatcher extends TypeSafeMatcher<LinkedKeyBinding<?>> {

	private final Key<?> key;
	private final Key<?> linkedKey;

	public LinkedBindingMatcher(Key<?> key, Key<?> linkedKey) {
		this.key = key;
		this.linkedKey = linkedKey;
	}
	
	public void describeTo(Description d) {
		d.appendText("from ").appendValue(key).appendText(" to ").appendValue(linkedKey) ;
	}
	
	@Override
	public boolean matchesSafely(LinkedKeyBinding<?> item) {		
		return item.getKey().equals(key) && item.getLinkedKey().equals(linkedKey);
	}
	
}
