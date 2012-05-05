package com.github.argast.guice.matchers;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AllOf;

import com.google.inject.Injector;
import com.google.inject.servlet.ServletModuleBinding;

public class InjectorWrapperMatcher extends TypeSafeMatcher<Injector> {

	private final List<Matcher<ServletModuleBinding>> matchers;

	public InjectorWrapperMatcher(List<Matcher<ServletModuleBinding>> matchers) {
		this.matchers = matchers;
	}
	
	public InjectorWrapperMatcher(Matcher<ServletModuleBinding>... matchers) {
		this.matchers = Arrays.asList(matchers);
	}
	
	public void describeTo(Description description) {
		description.appendText("\nBinding matching conditions:\n");
		for (Matcher<?> matcher: matchers) {
			description.appendText(" - ").appendDescriptionOf(matcher).appendText("\n");
		}
	}
	
	@Override
	public boolean matchesSafely(Injector injector) {
		boolean match = false;
		InjectorWrapper i = new InjectorWrapper(injector);
		for (ServletModuleBinding binding: i.getServletBindings()) {
			match |= AllOf.allOf((Iterable)matchers).matches(binding);
		}
		return match;
	}
}