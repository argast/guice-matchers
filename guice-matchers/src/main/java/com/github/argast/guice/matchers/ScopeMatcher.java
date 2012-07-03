package com.github.argast.guice.matchers;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.inject.Binding;
import com.google.inject.Scope;
import com.google.inject.spi.DefaultBindingScopingVisitor;

public class ScopeMatcher extends TypeSafeMatcher<Binding<?>> {

	private final Scope scope;

	public ScopeMatcher(Scope scope) {
		this.scope = scope;
	}
	
	public void describeTo(Description arg0) {
	}
	
	@Override
	public boolean matchesSafely(Binding<?> b) {
		return b.acceptScopingVisitor(new Visitor());
	}
	
	private class Visitor extends DefaultBindingScopingVisitor<Boolean> {
		
		@Override
		protected Boolean visitOther() {
			return false;
		}
		
		@Override
		public Boolean visitScope(Scope scope) {
			return ScopeMatcher.this.scope.equals(scope);
		}
	}
}
