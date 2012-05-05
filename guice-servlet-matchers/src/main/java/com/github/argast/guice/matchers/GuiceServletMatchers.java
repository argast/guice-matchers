package com.github.argast.guice.matchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import org.hamcrest.Matcher;
import org.junit.Assert;

import com.google.inject.Injector;
import com.google.inject.servlet.ServletModuleBinding;

public class GuiceServletMatchers {

	public static ServletMatcherBuilder assertServlet(Class<? extends HttpServlet> servletClass) {
		return new MatcherBuilderImpl(new ServletClassMatcher(servletClass));
	}

	public static FilterMatcherBuilder assertFilter(Class<? extends Filter> filterClass) {
		return new MatcherBuilderImpl(new FilterClassMatcher(filterClass));
	}
	
	@SuppressWarnings("unchecked")
	public static Matcher<Injector> containsBinding(Matcher<ServletModuleBinding> matcher) {
		return new InjectorWrapperMatcher(matcher);
	}
	
	@SuppressWarnings("unchecked")
	public static Matcher<Injector> containsBinding(Matcher<ServletModuleBinding> matcher1, Matcher<ServletModuleBinding> matcher2) {
		return new InjectorWrapperMatcher(matcher1, matcher2);
	}
		
	public static Matcher<Injector> containsBinding(Matcher<ServletModuleBinding>... matchers) {
		return new InjectorWrapperMatcher(matchers);
	}
	
	
	
	
	private static class MatcherBuilderImpl implements ServletMatcherBuilder, FilterMatcherBuilder {
		
		private List<Matcher<ServletModuleBinding>> matchers = new ArrayList<Matcher<ServletModuleBinding>>();
		
		public MatcherBuilderImpl(Matcher<ServletModuleBinding> m) {
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
		
		public MatcherBuilder filtersPattern(String pattern) {
			matchers.add(new PatternMatcher(pattern));
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
		
	static interface MatcherBuilder {
		MatcherBuilder hasInitParameter(String key, String value);
		MatcherBuilder hasInitParameters(Map<String, String> params);
		void on(Injector injector);
	}
	
	static interface ServletMatcherBuilder extends MatcherBuilder {
		MatcherBuilder serves(String uri);
		MatcherBuilder servesPattern(String pattern);		
	}
	
	static interface FilterMatcherBuilder extends MatcherBuilder {
		MatcherBuilder filters(String uri);
		MatcherBuilder filtersPattern(String pattern);			
	}
}
