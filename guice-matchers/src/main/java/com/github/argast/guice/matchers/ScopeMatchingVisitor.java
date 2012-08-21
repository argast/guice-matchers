package com.github.argast.guice.matchers;

import com.google.inject.Scope;
import com.google.inject.spi.DefaultBindingScopingVisitor;
import org.hamcrest.Description;

public class ScopeMatchingVisitor extends DefaultBindingScopingVisitor<Boolean> implements SelfDescribingBindingScopingVisitor {

    private final Scope scope;

    public ScopeMatchingVisitor(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Boolean visitScope(Scope scope) {
        return this.scope.equals(scope);
    }

    @Override
    protected Boolean visitOther() {
        return false;
    }

    public void describeTo(Description d) {
        d.appendText("binding with scope: " + scope);
    }
}
