package com.github.argast.guice.matchers;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import org.junit.Test;

import static com.github.argast.guice.matchers.GuiceMatchers.binds;
import static org.junit.Assert.assertThat;

public class ModuleIntegrationTest {

	static interface TestInterface {}
	static class TestClass implements TestInterface {}
    static class NotBoundTestClass implements TestInterface {}
    static interface TestInterfaceEagerSingleton {}
	static class TestClassEagerSingleton implements TestInterfaceEagerSingleton {}
	static interface TestInterfaceScope {}
	static class TestClassScope implements TestInterfaceScope {}
	static class OtherTestClass {}
    static class BoundDirectlyClass {}

	
	private Injector injector = Guice.createInjector(new AbstractModule() {
		@Override
		protected void configure() {
			bind(TestInterface.class).to(TestClass.class);
		    bind(TestInterfaceEagerSingleton.class).to(TestClassEagerSingleton.class).asEagerSingleton();
			bind(TestInterfaceScope.class).to(TestClassScope.class).in(Scopes.SINGLETON);
            bind(BoundDirectlyClass.class);
		}
	});
	
	@Test
	public void testThatClassIsBoundToInterface() throws Exception {
		assertThat(injector, binds(TestInterface.class).to(TestClass.class).withoutScope());		
	}

    @Test(expected = AssertionError.class)
    public void testThatExceptionIsThrownIfClassIsNotBoundToInterface() throws Exception {
        assertThat(injector, binds(TestInterface.class).to(NotBoundTestClass.class));
    }

    @Test
	public void testThatClassIsBoundAsEagerSingleton() throws Exception {
		assertThat(injector, binds(TestInterfaceEagerSingleton.class).to(TestClassEagerSingleton.class).asEagerSingleton());		
	}

	@Test
	public void testThatClassIsBoundInScope() throws Exception {
		assertThat(injector, binds(TestInterfaceScope.class).to(TestClassScope.class).in(Scopes.SINGLETON));
	}

    @Test
    public void testThatClassIsBound() throws Exception {
        assertThat(injector, binds(BoundDirectlyClass.class).withoutScope());
    }

    @Test(expected = AssertionError.class)
    public void testThatAssertionErrorIsThrownWhenClassIsNotBound() throws Exception {
        assertThat(injector, binds(NotBoundTestClass.class).withoutScope());
    }
}

