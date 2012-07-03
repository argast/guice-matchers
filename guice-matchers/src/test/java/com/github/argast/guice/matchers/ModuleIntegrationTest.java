package com.github.argast.guice.matchers;

import static com.github.argast.guice.matchers.GuiceMatchers.binds;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;

public class ModuleIntegrationTest {

	static interface TestInterface {}
	static class TestClass implements TestInterface {}
	static interface TestInterfaceEagerSingleton {}
	static class TestClassEagerSingleton implements TestInterfaceEagerSingleton {}
	static interface TestInterfaceScope {}
	static class TestClassScope implements TestInterfaceScope {}
	static class OtherTestClass {}
	
	private Injector injector = Guice.createInjector(new AbstractModule() {
		@Override
		protected void configure() {
			bind(TestInterface.class).to(TestClass.class);
			bind(TestInterfaceEagerSingleton.class).to(TestClassEagerSingleton.class).asEagerSingleton();
			bind(TestInterfaceScope.class).to(TestClassScope.class).in(Scopes.SINGLETON);
		}
	});
	
	@Test
	public void testThatClassIsBound() throws Exception {
		assertThat(injector, binds(TestInterface.class).to(TestClass.class).withoutScope());		
	}
	
	@Test
	public void testThatClassIsBoundAsEagerSingleton() throws Exception {
		assertThat(injector, binds(TestInterfaceEagerSingleton.class).to(TestClassEagerSingleton.class).asEagerSingleton());		
	}

	@Test
	public void testThatClassIsBoundInScope() throws Exception {
		assertThat(injector, binds(TestInterfaceScope.class).to(TestClassScope.class).in(Scopes.SINGLETON));		
	}
}

