package com.github.argast.guice.matchers;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scope;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AllOf;

import java.util.ArrayList;
import java.util.List;

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
		
		private Class<?> bindingClass;
		private Class<?> targetClazz;
		
		public MatcherBuilder(Class<?> bindingClass) {
			this.bindingClass = bindingClass;
		}
		
		public MatcherBuilder to(Class<?> clazz) {
			this.targetClazz = clazz;
			scopeMatcher = new NoScopeMatcher();
			return this;
		}
		
		public MatcherBuilder withoutScope() {
			scopeMatcher = new NoScopeMatcher();
			return this;
		}
		
		public MatcherBuilder asEagerSingleton() {
			scopeMatcher = new ScopeMatcher(new EagerSingletonScopingVisitor());
			return this;
		}

		public void describeTo(Description description) {
            description.appendText("\nBinding matching conditions:\n");
            for (Matcher<?> matcher: matchers) {
                description.appendText(" - ").appendDescriptionOf(matcher).appendText("\n");
            }
		}
		
		public boolean matchesSafely(Injector injector) {
            if (targetClazz != null) {
                matchers.add(new LinkedBindingMatcher(Key.get(bindingClass), Key.get(targetClazz)));
            } else {
                matchers.add(new ConstructorBindingMatcher(Key.get(bindingClass)));
            }
			matchers.add(scopeMatcher);
			
			boolean result = false;
			for (Binding<?> b: injector.getAllBindings().values()) {
                System.out.println(b);
				result |= new AllOf(matchers).matches(b);
			}
			return result;
		}

		public Matcher<Injector> in(Scope scope) {
			scopeMatcher = new ScopeMatcher(new ScopeMatchingVisitor(scope));
			return this;
		}
	}
}
