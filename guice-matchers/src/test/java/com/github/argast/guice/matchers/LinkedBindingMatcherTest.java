package com.github.argast.guice.matchers;

import com.google.inject.Key;
import com.google.inject.spi.LinkedKeyBinding;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LinkedBindingMatcherTest {

    public static final Key<Object> FROM_KEY = Key.get(Object.class);
    public static final Key<String> TARGET_KEY = Key.get(String.class);

    @Test
    public void testThatBindingIsMatchedWhenKeyIsTheSame() throws Exception {
        LinkedKeyBinding binding = mockBindingToReturnKeys(FROM_KEY, TARGET_KEY);
        assertTrue(matcher().matchesSafely(binding));
    }

    @Test
    public void testThatBindingIsNotMatchedWhenKeysAreDifferent() throws Exception {
        LinkedKeyBinding binding = mockBindingToReturnKeys(FROM_KEY, Key.get(Long.class));
        assertFalse(matcher().matchesSafely(binding));
    }

    @Test
    public void testThatDescriptionIsCorrect() throws Exception {
        Description d = new StringDescription();
        matcher().describeTo(d);
        assertEquals(String.format("from <%s> to <%s>", FROM_KEY, TARGET_KEY), d.toString());
    }

    private LinkedBindingMatcher matcher() {
        return new LinkedBindingMatcher(FROM_KEY, TARGET_KEY);
    }

    private LinkedKeyBinding mockBindingToReturnKeys(Key<?> key, Key<?> linkedKey) {
        LinkedKeyBinding binding = mock(LinkedKeyBinding.class);
        when(binding.getKey()).thenReturn(key);
        when(binding.getLinkedKey()).thenReturn(linkedKey);
        return binding;
    }
}
