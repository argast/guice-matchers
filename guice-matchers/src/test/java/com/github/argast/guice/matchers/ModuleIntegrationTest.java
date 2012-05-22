package com.github.argast.guice.matchers;

import static com.github.argast.guice.matchers.GuiceMatchers.binds;
import static com.github.argast.guice.matchers.GuiceMatchers.withoutScope;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ModuleIntegrationTest {

	static interface TestInterface {}
	static class TestClass implements TestInterface {}
	static class OtherTestClass {}
	
	private Injector injector = Guice.createInjector(new AbstractModule() {
		@Override
		protected void configure() {
			bind(TestInterface.class).to(TestClass.class);
		}
	});
	
	@Test
	public void testThatClassIsBound() throws Exception {
		assertThat(injector, binds(TestInterface.class).to(TestClass.class).withoutScope());		
	}
}
