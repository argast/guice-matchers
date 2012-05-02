package com.github.argast.guice.matchers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.StringDescription;
import org.junit.Test;

import com.google.inject.servlet.ServletModuleBinding;

public class InitParamsMatcherTest {

	@SuppressWarnings("serial")
	private static final Map<String, String> PARAMS = new HashMap<String, String>() {{
		put("key1", "value1");
		put("key2", "value2");
	}};
	
	@Test
	public void testThatMatcherReturnsTrueIfParametersAreEqual() throws Exception {
		assertTrue(matches(bindingThatReturnsParams(PARAMS)));
	}

	@Test
	public void testThatMatcherReturndFalseIfParametersAreDifferent() throws Exception {
		assertFalse(matches(bindingThatReturnsParams(Collections.singletonMap("key1", "value1"))));
	}
	
	@Test
	public void testThatDescriptionIsCorrect() throws Exception {
		StringDescription desc = new StringDescription();
		createMatcher().describeTo(desc);
		assertEquals("binding with init parameters:\n- key2 = value2\n- key1 = value1\n", desc.toString());
	}

	private InitParamsMatcher createMatcher() {
		return new InitParamsMatcher(PARAMS);
	}

	private boolean matches(ServletModuleBinding binding) {
		return createMatcher().matchesSafely(binding);
	}
	
	private ServletModuleBinding bindingThatReturnsParams(Map<String, String> params) {
		ServletModuleBinding binding = mock(ServletModuleBinding.class);
		when(binding.getInitParams()).thenReturn(params);
		return binding;
	}
}
