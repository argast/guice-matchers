package com.github.argast.guice.matchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.inject.Injector;
import com.google.inject.servlet.ServletModuleBinding;

public class GuiceServletMatchers {

	public static ServletMatcherBuilder serves(String uri) {
		return new MatcherBuilderImpl(new UriMatcher(uri));
	}

	public static ServletMatcherBuilder servesPattern(String uri) {
		return new MatcherBuilderImpl(new PatternMatcher(uri));
	}

	public static FilterMatcherBuilder filters(String uri) {
		return new MatcherBuilderImpl(new UriMatcher(uri));
	}

	public static FilterMatcherBuilder filtersPattern(String uri) {
		return new MatcherBuilderImpl(new PatternMatcher(uri));
	}
	
	@SuppressWarnings("unchecked")
	public static Matcher<Injector> containsBinding(Matcher<ServletModuleBinding> matcher) {
		return new InjectorWrapperMatcher(matcher);
	}
	
	private static class MatcherBuilderImpl implements ServletMatcherBuilder, FilterMatcherBuilder {
		
		private List<Matcher<ServletModuleBinding>> matchers = new ArrayList<Matcher<ServletModuleBinding>>();
		
		public MatcherBuilderImpl(Matcher<ServletModuleBinding> m) {
			matchers.add(m);
		}

		@Override
		public Matcher<Injector> with(Class<? extends HttpServlet> servlet) {
			return with(servlet, Collections.EMPTY_MAP);
		}
		
		@Override
		public Matcher<Injector> with(Class<? extends HttpServlet> servlet,
				Map<String, String> initParameters) {
			matchers.add(new ServletClassMatcher(servlet));
			matchers.add(new InitParamsMatcher(initParameters));
			return new InjectorWrapperMatcher(matchers);
		}
		
		@Override
		public Matcher<Injector> through(Class<? extends Filter> filter) {
			return through(filter, Collections.EMPTY_MAP);
		}
		
		@Override
		public Matcher<Injector> through(Class<? extends Filter> filter,
				Map<String, String> initParameters) {
			matchers.add(new FilterClassMatcher(filter));
			matchers.add(new InitParamsMatcher(initParameters));
			return new InjectorWrapperMatcher(matchers);
		}
	}
		
	static interface ServletMatcherBuilder {
		Matcher<Injector> with(Class<? extends HttpServlet> servlet);
		Matcher<Injector> with(Class<? extends HttpServlet> servlet, Map<String, String> initParameters);
	}
	
	static interface FilterMatcherBuilder {
		Matcher<Injector> through(Class<? extends Filter> servlet);
		Matcher<Injector> through(Class<? extends Filter> servlet, Map<String, String> initParameters);
	}
}
