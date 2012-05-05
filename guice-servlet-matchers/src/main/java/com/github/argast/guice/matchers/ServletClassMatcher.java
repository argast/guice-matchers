package com.github.argast.guice.matchers;

import javax.servlet.http.HttpServlet;

import com.google.inject.servlet.LinkedServletBinding;
import com.google.inject.servlet.ServletModuleBinding;

public class ServletClassMatcher extends AbstractClassMatcher {

	public ServletClassMatcher(Class<? extends HttpServlet> servlet) {
		super(servlet);
	}

	@Override
	protected boolean correctBindingType(ServletModuleBinding binding) {
		return binding instanceof LinkedServletBinding;
	}
	
	@Override
	protected Class<?> extractClass(ServletModuleBinding binding) {
		return ((LinkedServletBinding)binding).getLinkedKey().getTypeLiteral().getRawType();
	}
	
	@Override
	protected String initialDescription() {
		return "servlet";
	}
}
