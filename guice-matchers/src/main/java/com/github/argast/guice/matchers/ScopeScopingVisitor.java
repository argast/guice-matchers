package com.github.argast.guice.matchers;

import com.google.inject.Scope;
import com.google.inject.spi.DefaultBindingScopingVisitor;

public class ScopeScopingVisitor extends DefaultBindingScopingVisitor<Boolean> {

    private final Scope scope;

    public ScopeScopingVisitor(Scope scope) {
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
}
