package com.github.argast.guice.matchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AllOf;
import org.junit.Assert;

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
		
		private List<Matcher<ServletModuleBinding>> matchers = new ArrayList<Matcher<ServletModuleBinding>>();
		
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
			Assert.assertThat(injector, new InjectorWrapperMatcher(matchers));
		}

		public MatcherBuilder hasInitParameters(Map<String, String> params) {
			matchers.add(new InitParamsMatcher(params));
			return this;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Matcher<Injector> containsBinding(Matcher<ServletModuleBinding> matcher) {
		return new InjectorWrapperMatcher(matcher);
	}
	
	@SuppressWarnings("unchecked")
	public static Matcher<Injector> containsBinding(Matcher<ServletModuleBinding> matcher1, Matcher<ServletModuleBinding> matcher2) {
		return new InjectorWrapperMatcher(matcher1, matcher2);
	}
	
	@SuppressWarnings("unchecked")
	public static Matcher<Injector> containsBinding(Matcher<ServletModuleBinding> matcher1, Matcher<ServletModuleBinding> matcher2, Matcher<ServletModuleBinding> matcher3) {
		return new InjectorWrapperMatcher(matcher1, matcher2, matcher3);
	}
	
	public static Matcher<Injector> containsBinding(Matcher<ServletModuleBinding>... matchers) {
		return new InjectorWrapperMatcher(matchers);
	}
	
	public static class InjectorWrapperMatcher extends TypeSafeMatcher<Injector> {

		private final List<Matcher<ServletModuleBinding>> matchers;

		public InjectorWrapperMatcher(List<Matcher<ServletModuleBinding>> matchers) {
			this.matchers = matchers;
		}
		
		public InjectorWrapperMatcher(Matcher<ServletModuleBinding>... matchers) {
			this.matchers = Arrays.asList(matchers);
		}
		
		public void describeTo(Description description) {
			description.appendText("\nBinding matching conditions:\n");
			for (Matcher<?> matcher: matchers) {
				description.appendText(" - ").appendDescriptionOf(matcher).appendText("\n");
			}
		}
		
		@Override
		public boolean matchesSafely(Injector injector) {
			boolean match = false;
			InjectorWrapper i = new InjectorWrapper(injector);
			for (ServletModuleBinding binding: i.getServletBindings()) {
				match |= AllOf.allOf((Iterable)matchers).matches(binding);
			}
			return match;
		}
	}
	
	private static class InjectorWrapper {

		private List<ServletModuleBinding> servletBindings = new ArrayList<ServletModuleBinding>();
		
		public InjectorWrapper(Injector injector) {
			for (Binding<?> b: injector.getBindings().values()) {
				ServletModuleBinding servletModuleBinding = b.acceptTargetVisitor(new ServletModuleBindingReturningVisitor());
				if (servletModuleBinding != null) {
					servletBindings.add(servletModuleBinding);
				}
			}
		}
		
		public List<ServletModuleBinding> getServletBindings() {
			return servletBindings;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("current servlet bindings:\n");
			for (ServletModuleBinding b: servletBindings) {
				sb.append(b).append('\n');
			}
			return sb.toString();
		}
	}
	
	private static class ServletModuleBindingReturningVisitor extends DefaultBindingTargetVisitor<Object, ServletModuleBinding> implements ServletModuleTargetVisitor<Object, ServletModuleBinding> {
		
		public ServletModuleBinding visit(InstanceServletBinding binding) {
			return binding;
		}
		
		public ServletModuleBinding visit(LinkedServletBinding binding) {
			return binding;
		}
		
		public ServletModuleBinding visit(LinkedFilterBinding binding) {
			return binding;
		}

		public ServletModuleBinding visit(InstanceFilterBinding binding) {
			return binding;
		}
		
	}
}
