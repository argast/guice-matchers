package com.github.argast.guice.matchers;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.servlet.InstanceFilterBinding;
import com.google.inject.servlet.InstanceServletBinding;
import com.google.inject.servlet.LinkedFilterBinding;
import com.google.inject.servlet.LinkedServletBinding;
import com.google.inject.servlet.ServletModuleBinding;
import com.google.inject.servlet.ServletModuleTargetVisitor;
import com.google.inject.spi.DefaultBindingTargetVisitor;

public class GuiceServletMatchers {

	public static MatcherBuilder assertServlet(Class<? extends HttpServlet> servletClass) {
		return new MatcherBuilder(new ServletClassMatcher(servletClass));
	}

	public static MatcherBuilder assertFilter(Class<? extends Filter> filterClass) {
		return new MatcherBuilder(new FilterClassMatcher(filterClass));
	}
	
	public static class MatcherBuilder {
		
		private List<Matcher<?>> matchers = new ArrayList<Matcher<?>>();
		
		public MatcherBuilder(Matcher<ServletModuleBinding> m) {
			matchers.add(m);
		}
		
		public MatcherBuilder serves(String uri) {
			matchers.add(new UriMatcher(uri));
			return this;
		}

		public MatcherBuilder servesPattern(String pattern) {
			matchers.add(new PatternMatcher(pattern));
			return this;
		}
		
		public MatcherBuilder filters(String uri) {
			matchers.add(new UriMatcher(uri));
			return this;
		}
		
		public MatcherBuilder hasInitParameter(String key, String value) {
			matchers.add(new InitParamMatcher(key, value));
			return this;
		}
		
		public void on(Injector injector) {
			List<Matcher<?>> matchers = new ArrayList<Matcher<?>>();
			
			
			for (Binding<?> b: injector.getBindings().values()) {
				matchers.add(new SingleBindingMatcher(b));
			}
			
			Assert.assertThat(this.matchers, AnyOf.anyOf(matchers));
		}
	}
	
	private static class SingleBindingMatcher extends TypeSafeMatcher<List<Matcher<?>>> {

		private final Binding<?> binding;

		public SingleBindingMatcher(Binding<?> binding) {
			this.binding = binding;
		}
		
		public void describeTo(Description description) {
		}
		
		@Override
		public boolean matchesSafely(List<Matcher<?>> matchers) {
			return this.binding.acceptTargetVisitor(new Visitor(matchers));
		}
	}
	
	private static class Visitor extends DefaultBindingTargetVisitor<Object, Boolean> implements ServletModuleTargetVisitor<Object, Boolean> {
		
		private final List<Matcher<?>> matchers;

		public Visitor(List<Matcher<?>> matchers) {
			this.matchers = matchers;
		}
		
		@Override
		protected Boolean visitOther(Binding<? extends Object> binding) {
			return false;
		}
		
		public Boolean visit(InstanceServletBinding binding) {
			return matchAll(binding);
		}
		
		public Boolean visit(LinkedServletBinding binding) {
			return matchAll(binding);
		}
		
		public Boolean visit(LinkedFilterBinding binding) {
			return matchAll(binding);
		}

		public Boolean visit(InstanceFilterBinding binding) {
			return matchAll(binding);
		}
		
		private <T extends ServletModuleBinding> boolean matchAll(ServletModuleBinding binding) {
			Matcher<ServletModuleBinding>[] a = matchers.toArray(new Matcher[matchers.size()]);
//			Assert.assertThat(binding, AllOf.allOf(a));
			return AllOf.allOf(a).matches(binding);
		}
	}
}
