package com.github.argast.guice.matchers;

import com.google.inject.Key;
import com.google.inject.spi.ConstructorBinding;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ConstructorBindingMatcher extends TypeSafeMatcher<ConstructorBinding<?>> {

	
	private final Key<?> key;
	private final Class<?> targetClass;

	public ConstructorBindingMatcher(Key<?> key, Class<?> targetClass) {
		this.key = key;
		this.targetClass = targetClass;
	}
	
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean matchesSafely(ConstructorBinding<?> item) {
		return item.getKey().equals(key);
	}
}
