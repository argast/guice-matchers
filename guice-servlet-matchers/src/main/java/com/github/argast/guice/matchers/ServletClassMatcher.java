package com.github.argast.guice.matchers;

import javax.servlet.http.HttpServlet;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.inject.servlet.InstanceServletBinding;
import com.google.inject.servlet.LinkedServletBinding;
import com.google.inject.servlet.ServletModuleBinding;

public class ServletClassMatcher extends TypeSafeMatcher<ServletModuleBinding> {

	private final Class<? extends HttpServlet> servlet;

	public ServletClassMatcher(Class<? extends HttpServlet> servlet) {
		this.servlet = servlet;
	}
	
	public void describeTo(Description d) {
		d.appendText("servlet class ").appendValue(servlet.getName());
	}
	
	@Override
	public boolean matchesSafely(ServletModuleBinding binding) {
		return servlet.equals(getServletClass(binding));
	}

	private Class<?> getServletClass(ServletModuleBinding binding) {
		if (binding instanceof LinkedServletBinding) {
			return ((LinkedServletBinding)binding).getLinkedKey().getTypeLiteral().getRawType();
		} else if (binding instanceof InstanceServletBinding) {
			return ((InstanceServletBinding)binding).getServletInstance().getClass();
		}
		return null;
	}
}
