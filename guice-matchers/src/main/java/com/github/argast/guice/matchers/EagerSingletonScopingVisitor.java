package com.github.argast.guice.matchers;

import com.google.inject.spi.DefaultBindingScopingVisitor;

public class EagerSingletonScopingVisitor extends DefaultBindingScopingVisitor<Boolean> {

    @Override
    public Boolean visitEagerSingleton() {
        return true;
    }

    @Override
    protected Boolean visitOther() {
        return false;
    }
}
