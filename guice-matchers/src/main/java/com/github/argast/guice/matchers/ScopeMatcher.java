package com.github.argast.guice.matchers;

import com.google.inject.Binding;
import com.google.inject.spi.BindingScopingVisitor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ScopeMatcher extends TypeSafeMatcher<Binding<?>> {

    private SelfDescribingBindingScopingVisitor visitor;

    public ScopeMatcher(SelfDescribingBindingScopingVisitor visitor) {
        this.visitor = visitor;
    }
	
	public void describeTo(Description d) {
        visitor.describeTo(d);
	}
	
	@Override
	public boolean matchesSafely(Binding<?> b) {
		return b.acceptScopingVisitor(visitor);
	}
}
