package com.github.argast.guice.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AllOf;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scope;

public class GuiceMatchers {
	
	public static Matcher<Binding<?>> withoutScope() {
		return new NoScopeMatcher();
	}
	
	public static MatcherBuilder binds(Class<?> clazz) {
		return new MatcherBuilder(clazz);
	}
		
	static class MatcherBuilder extends TypeSafeMatcher<Injector> {
		
		private List<Matcher<? extends Binding<?>>> matchers = new ArrayList<Matcher<? extends Binding<?>>>();
		private Matcher<? extends Binding<?>> scopeMatcher;
		
		private final Class<?> bindingClass;
		private Class<?> targetClazz;
		
		public MatcherBuilder(Class<?> bindingClass) {
			this.bindingClass = bindingClass;
		}
		
		public MatcherBuilder to(Class<?> clazz) {
			this.targetClazz = clazz;
			matchers.add(new LinkedBindingMatcher(Key.get(bindingClass), Key.get(targetClazz)));
			scopeMatcher = new NoScopeMatcher();
			return this;
		}
		
		public MatcherBuilder withoutScope() {
			scopeMatcher = new NoScopeMatcher();
			return this;
		}
		
		public MatcherBuilder asEagerSingleton() {
			scopeMatcher = new EagerSingletonMatcher();
			return this;
		}
		
		public void describeTo(Description description) {
			// TODO Auto-generated method stub
			
		}
		
		public boolean matchesSafely(Injector injector) {
			matchers.add(scopeMatcher);
			
			boolean result = false;
			for (Binding<?> b: injector.getAllBindings().values()) {
				result |= new AllOf(matchers).matches(b);
			}
			return result;
		}

		public Matcher<Injector> in(Scope scope) {
			scopeMatcher = new ScopeMatcher(scope);
			return this;
		}
	}
}
