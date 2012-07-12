package com.github.argast.guice.matchers;

import com.google.inject.Binding;
import com.google.inject.spi.DefaultBindingScopingVisitor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class EagerSingletonMatcher extends TypeSafeMatcher<Binding<?>> {

	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean matchesSafely(Binding<?> b) {
		return b.acceptScopingVisitor(new DefaultBindingScopingVisitor<Boolean>() {
			@Override
			protected Boolean visitOther() {
				return false;
			}
			@Override
			public Boolean visitEagerSingleton() {
				return true;
			}
		});
	}
}
