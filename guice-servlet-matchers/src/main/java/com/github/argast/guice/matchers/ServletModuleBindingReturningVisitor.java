package com.github.argast.guice.matchers;

import com.google.inject.servlet.InstanceFilterBinding;
import com.google.inject.servlet.InstanceServletBinding;
import com.google.inject.servlet.LinkedFilterBinding;
import com.google.inject.servlet.LinkedServletBinding;
import com.google.inject.servlet.ServletModuleBinding;
import com.google.inject.servlet.ServletModuleTargetVisitor;
import com.google.inject.spi.DefaultBindingTargetVisitor;

class ServletModuleBindingReturningVisitor extends DefaultBindingTargetVisitor<Object, ServletModuleBinding> implements ServletModuleTargetVisitor<Object, ServletModuleBinding> {
	
	public ServletModuleBinding visit(InstanceServletBinding binding) {
		return binding;
	}
	
	public ServletModuleBinding visit(LinkedServletBinding binding) {
		return binding;
	}
	
	public ServletModuleBinding visit(LinkedFilterBinding binding) {
		return binding;
	}

	public ServletModuleBinding visit(InstanceFilterBinding binding) {
		return binding;
	}
	
}