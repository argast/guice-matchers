
package com.github.argast.guice.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.StringDescription;
import org.junit.Test;

import com.google.inject.servlet.ServletModuleBinding;

public class UriMatcherTest {

	private static final String TEST_URI = "some/uri";
	
	private ServletModuleBinding binding = mock(ServletModuleBinding.class);

	@Test
	public void testThatMatchesUriIsInvoked() throws Exception {
		createMatcherForUri(null).matchesSafely(binding);
		verify(binding).matchesUri(anyString());
	}
	
	@Test
	public void testThatMatcherReturnsTrueUriMatches() throws Exception {
		when(binding.matchesUri(TEST_URI)).thenReturn(true);
		assertTrue(createMatcherForUri(TEST_URI).matchesSafely(binding));
	}
	
	@Test
	public void testThatMatcherReturnsFalseWhenUriDoesntMatch() throws Exception {
		when(binding.matchesUri(TEST_URI)).thenReturn(false);
		assertFalse(createMatcherForUri(TEST_URI).matchesSafely(binding));
	}

	@Test
	public void testThatMatcherAppendsCorrectDescription() throws Exception {
		StringDescription description = new StringDescription();
		createMatcherForUri(TEST_URI).describeTo(description);
		assertEquals("binding matching uri: \"" + TEST_URI + "\"", description.toString());
	}
	
	private UriMatcher createMatcherForUri(String uri) {
		return new UriMatcher(uri);
	}
}



