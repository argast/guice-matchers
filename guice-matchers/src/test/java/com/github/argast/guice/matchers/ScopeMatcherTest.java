package com.github.argast.guice.matchers;


import com.google.inject.Binding;
import com.google.inject.spi.BindingScopingVisitor;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScopeMatcherTest {

    @Test
    public void testThatBindingIsVisitedByVisitor() throws Exception {
        Binding<?> b = mock(Binding.class);
        BindingScopingVisitor<Boolean> visitor = mock(BindingScopingVisitor.class);
        when(b.acceptScopingVisitor(visitor)).thenReturn(true);
        assertTrue(new ScopeMatcher(visitor).matchesSafely(b));
    }
}
