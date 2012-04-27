package com.github.argast.guice.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.google.inject.servlet.ServletModuleBinding;

public abstract class AbstractClassMatcher extends TypeSafeMatcher<ServletModuleBinding> {

	private final Class<?> expectedType;

	public AbstractClassMatcher(Class<?> expectedType) {
		this.expectedType = expectedType;
	}

	protected abstract boolean correctBindingType(ServletModuleBinding binding);
	protected abstract Class<?> extractClass(ServletModuleBinding binding);
	protected abstract String initialDescription();
	
	public void describeTo(Description d) {
		d.appendText(initialDescription()).appendText(" class equal to ").appendValue(expectedType.getName());
	}

	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		return correctBindingType(binding) && expectedType.equals(extractClass(binding));
	}


}