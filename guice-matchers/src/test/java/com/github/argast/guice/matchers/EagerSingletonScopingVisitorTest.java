package com.github.argast.guice.matchers;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class EagerSingletonScopingVisitorTest {


    @Test
    public void testThatVisitorIsReturningTrueForEagerSingleton() throws Exception {
        assertTrue(visitor().visitEagerSingleton());
    }

    @Test
    public void testThatVisitorIsReturningFalseForOthers() throws Exception {
        assertFalse(visitor().visitOther());
    }

    private EagerSingletonScopingVisitor visitor() {
        return new EagerSingletonScopingVisitor();
    }

    @Test
    public void testThatDescriptionsCorrect() throws Exception {
        Description d = new StringDescription();
        visitor().describeTo(d);
        assertEquals("eager singleton binding", d.toString());
    }
}
