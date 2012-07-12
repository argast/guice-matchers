package com.github.argast.guice.matchers;

import com.google.inject.Binding;
import com.google.inject.spi.BindingScopingVisitor;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EagerSingletonMatcherTest {

	
	@Test
	public void testThatBindingIsVisitedByScopeVisitor() throws Exception {
		Binding<?> binding = mock(Binding.class);
		when(binding.acceptScopingVisitor(any(BindingScopingVisitor.class))).thenReturn(false);
		new EagerSingletonMatcher().matchesSafely(binding);
		verify(binding).acceptScopingVisitor(any(BindingScopingVisitor.class));
	}
	
	@Test
	public void testThatMatcherReturnsTrueForEagerSingleton() throws Exception {
		
		
	}
}
