package com.github.argast.guice.matchers;

import com.google.inject.Key;
import com.google.inject.spi.ConstructorBinding;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConstructorBindingTest {

    public static final Key<String> KEY = Key.get(String.class);
    public static final Key<Integer> DIFFERENT_KEY = Key.get(Integer.class);
    private final ConstructorBinding binding = mock(ConstructorBinding.class);

    @Before
    public void setUp() throws Exception {
        when(binding.getKey()).thenReturn(KEY);
    }

    @Test
    public void testThatConstructorBindingWithCorrectKeyIsMatched() throws Exception {
        assertTrue(new ConstructorBindingMatcher(KEY).matchesSafely(binding));
    }

    @Test
    public void testThatConstructorBindingWithDifferentKeyIsNotMatched() throws Exception {
        assertFalse(new ConstructorBindingMatcher(DIFFERENT_KEY).matchesSafely(binding));
    }

    @Test
    public void testThatDescriptionIsCorrect() throws Exception {
        Description d = new StringDescription();
        new ConstructorBindingMatcher(KEY).describeTo(d);
        assertEquals("constructor binding to " + KEY, d.toString());
    }
}
