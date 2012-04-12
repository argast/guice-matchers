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
		d.appendValue(filter);
	}
	
	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		return filter.equals(getServletClass(binding));
	}

	private Class<?> getServletClass(ServletModuleBinding binding) {
		if (binding instanceof LinkedFilterBinding) {
			return ((LinkedFilterBinding)binding).getLinkedKey().getTypeLiteral().getRawType();
		} else if (binding instanceof InstanceFilterBinding) {
			return ((InstanceFilterBinding)binding).getFilterInstance().getClass();
		}
		return null;
	}
}
