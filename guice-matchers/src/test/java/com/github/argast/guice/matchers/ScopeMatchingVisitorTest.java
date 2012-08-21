package com.github.argast.guice.matchers;

import com.google.inject.Scope;
import com.google.inject.Scopes;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ScopeMatchingVisitorTest {

    @Test
    public void testThatScopeVisitorReturnsTrueForMatchingScope() throws Exception {
        assertTrue(visitorForScope(Scopes.SINGLETON).visitScope(Scopes.SINGLETON));
    }

    @Test
    public void testThatVisitorReturnsFalseForNotMatchingScope() throws Exception {
        assertFalse(visitorForScope(Scopes.NO_SCOPE).visitScope(Scopes.SINGLETON));
    }

    @Test
    public void testThatVisitorReturnsFalseForOthers() throws Exception {
        assertFalse(visitorForScope(Scopes.SINGLETON).visitOther());
    }

    private ScopeMatchingVisitor visitorForScope(Scope scope) {
        return new ScopeMatchingVisitor(scope);
    }

    @Test
    public void testThatDescriptionIsCorrect() throws Exception {
        Description d = new StringDescription();
        visitorForScope(Scopes.SINGLETON).describeTo(d);
        assertEquals("binding with scope: " + Scopes.SINGLETON, d.toString());
    }
}
