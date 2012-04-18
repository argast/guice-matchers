package com.github.argast.guice.matchers;

import javax.servlet.Filter;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.inject.servlet.InstanceFilterBinding;
import com.google.inject.servlet.LinkedFilterBinding;
import com.google.inject.servlet.ServletModuleBinding;

public class FilterClassMatcher extends TypeSafeMatcher<ServletModuleBinding> {

	private final Class<? extends Filter> filter;

	public FilterClassMatcher(Class<? extends Filter> filter) {
		this.filter = filter;
	}
	
	public void describeTo(Description d) {
		d.appendText("filter class equal to ").appendValue(filter.getName());
	}
	
	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		return binding instanceof LinkedFilterBinding && filter.equals(((LinkedFilterBinding)binding).getLinkedKey().getTypeLiteral().getRawType());
	}
}
