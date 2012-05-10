package com.github.argast.guice.matchers;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scopes;

public class ModuleIntegrationTest {

	static class TestClass {}
	
	private Injector injector = Guice.createInjector(new AbstractModule() {
		@Override
		protected void configure() {
			bind(TestClass.class);
		}
	});
	
	
	@Test
	public void testThatClassIsBound() throws Exception {
		for (Binding b: injector.getAllBindings().values()) {
			System.out.print(new BindingMatcher(Key.get(TestClass.class)).matchesSafely(b));
			System.out.print(" - ");
			System.out.print(new NoScopeMatcher(Scopes.NO_SCOPE).matchesSafely(b));
			System.out.println();
			
		}
		
		assertThat(injector, containsBinding(to(TestClass.class), withoutScope()));
		assertThat(injector, containsBinding(to(TestClass.class), asEagerSingleton()));
		assertThat(injector, containsBinding(to(TestClass.class), withScope()));
		
	}
}
