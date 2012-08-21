package com.github.argast.guice.matchers;

import com.google.inject.spi.DefaultBindingScopingVisitor;
import org.hamcrest.Description;

public class EagerSingletonScopingVisitor extends DefaultBindingScopingVisitor<Boolean> implements SelfDescribingBindingScopingVisitor {

    @Override
    public Boolean visitEagerSingleton() {
        return true;
    }

    @Override
    protected Boolean visitOther() {
        return false;
    }

    public void describeTo(Description d) {
        d.appendText("eager singleton binding");
    }
}
