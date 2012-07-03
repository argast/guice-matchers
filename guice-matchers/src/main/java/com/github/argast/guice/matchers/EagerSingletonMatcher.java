package com.github.argast.guice.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.inject.Binding;
import com.google.inject.spi.DefaultBindingScopingVisitor;

public class EagerSingletonMatcher extends TypeSafeMatcher<Binding<?>> {

	@Override
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
