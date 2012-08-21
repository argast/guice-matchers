package com.github.argast.guice.matchers;

import com.google.inject.Binding;
import com.google.inject.spi.DefaultBindingScopingVisitor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class NoScopeMatcher extends TypeSafeMatcher<Binding<?>> {

	public void describeTo(Description d) {
        d.appendText("with no scope");
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
		public Boolean visitNoScoping() {
			return true;
		}
	}
}
