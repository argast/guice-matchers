package com.github.argast.guice.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AllOf;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;

public class GuiceMatchers {
	
	public static Matcher<Binding<?>> withoutScope() {
		return new NoScopeMatcher();
	}
	
//	public static Matcher<Injector> containsBinding(Matcher<Binding<? extends Object>> matcher1) {
//		return containsBindingInternal(matcher1);
//	}
//
//	public static Matcher<Injector> binds(Matcher<Binding<?>> matcher1, Matcher<Binding<?>> matcher2) {
//		return containsBindingInternal(matcher1, matcher2);
//	}

	public static MatcherBuilder binds(Class<?> clazz) {
		return new MatcherBuilder(clazz);
	}
		
	static class MatcherBuilder extends TypeSafeMatcher<Injector> {
		
		private List<Matcher<? extends Binding<?>>> matchers = new ArrayList<Matcher<? extends Binding<?>>>();
		
		private final Class<?> bindingClass;
		private Class<?> targetClazz;
		
		public MatcherBuilder(Class<?> bindingClass) {
			this.bindingClass = bindingClass;
		}
		
		public MatcherBuilder to(Class<?> clazz) {
			this.targetClazz = clazz;
			matchers.add(new LinkedBindingMatcher(Key.get(bindingClass), Key.get(targetClazz)));
			return this;
		}
		
		public MatcherBuilder withoutScope() {
			matchers.add(new NoScopeMatcher());
			return this;
		}
		
		public void describeTo(Description description) {
			// TODO Auto-generated method stub
			
		}
		
		public boolean matchesSafely(Injector injector) {
			boolean result = false;
			for (Binding<?> b: injector.getAllBindings().values()) {
				System.out.println(b);
				result |= new AllOf(matchers).matches(b);
			}
			return result;
		}
	}
}
