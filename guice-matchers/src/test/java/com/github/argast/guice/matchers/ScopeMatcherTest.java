package com.github.argast.guice.matchers;


import com.google.inject.Binding;
import com.google.inject.Scopes;
import com.google.inject.spi.BindingScopingVisitor;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScopeMatcherTest {

    @Test
    public void testThatBindingIsVisitedByVisitor() throws Exception {
        Binding<?> b = mock(Binding.class);
        SelfDescribingBindingScopingVisitor visitor = mock(SelfDescribingBindingScopingVisitor.class);
        when(b.acceptScopingVisitor(visitor)).thenReturn(true);
        assertTrue(new ScopeMatcher(visitor).matchesSafely(b));
    }

    @Test
    public void testThatVisitorIsDescribingItself() throws Exception {
        Description d = mock(Description.class);
        SelfDescribingBindingScopingVisitor visitor = mock(SelfDescribingBindingScopingVisitor.class);
        new ScopeMatcher(visitor).describeTo(d);
        verify(visitor).describeTo(d);
    }
}
