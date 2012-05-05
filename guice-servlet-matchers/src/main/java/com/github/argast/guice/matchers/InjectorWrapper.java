package com.github.argast.guice.matchers;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModuleBinding;

class InjectorWrapper {

	private List<ServletModuleBinding> servletBindings = new ArrayList<ServletModuleBinding>();
	
	public InjectorWrapper(Injector injector) {
		for (Binding<?> b: injector.getBindings().values()) {
			ServletModuleBinding servletModuleBinding = b.acceptTargetVisitor(new ServletModuleBindingReturningVisitor());
			if (servletModuleBinding != null) {
				servletBindings.add(servletModuleBinding);
			}
		}
	}
	
	public List<ServletModuleBinding> getServletBindings() {
		return servletBindings;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("current servlet bindings:\n");
		for (ServletModuleBinding b: servletBindings) {
			sb.append(b).append('\n');
		}
		return sb.toString();
	}
}