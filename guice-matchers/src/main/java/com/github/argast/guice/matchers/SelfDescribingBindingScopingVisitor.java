package com.github.argast.guice.matchers;

import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.DefaultBindingScopingVisitor;
import org.hamcrest.Description;


public interface SelfDescribingBindingScopingVisitor extends BindingScopingVisitor<Boolean> {
    void describeTo(Description d);
}
