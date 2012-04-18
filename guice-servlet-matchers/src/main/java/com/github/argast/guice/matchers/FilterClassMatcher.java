package com.github.argast.guice.matchers;

import javax.servlet.Filter;

import com.google.inject.servlet.LinkedFilterBinding;
import com.google.inject.servlet.ServletModuleBinding;



public class FilterClassMatcher extends AbstractClassMatcher {

	public FilterClassMatcher(Class<? extends Filter> filter) {
		super(filter);
	}
	
	@Override
	protected boolean correctBindingType(ServletModuleBinding binding) {
		return binding instanceof LinkedFilterBinding;
	}
	
	@Override
	protected Class<?> extractClass(ServletModuleBinding binding) {
		return ((LinkedFilterBinding)binding).getLinkedKey().getTypeLiteral().getRawType();
	}
	
	@Override
	protected String initialDescription() {
		return "filter";
	}
}
