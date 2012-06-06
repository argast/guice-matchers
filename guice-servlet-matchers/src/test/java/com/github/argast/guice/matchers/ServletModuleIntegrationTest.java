package com.github.argast.guice.matchers;

import static com.github.argast.guice.matchers.GuiceServletMatchers.filters;
import static com.github.argast.guice.matchers.GuiceServletMatchers.filtersPattern;
import static com.github.argast.guice.matchers.GuiceServletMatchers.serves;
import static com.github.argast.guice.matchers.GuiceServletMatchers.servesPattern;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;

@SuppressWarnings("serial")
public class ServletModuleIntegrationTest {

	private static final Map<String, String> PARAMS = new HashMap<String, String>() {{
		put("key1", "value1");
		put("key2", "value2");
	}};
	
	private static final Map<String, String> FILTER_PARAMS = Collections.singletonMap("filterKey", "filterValue");
	
	private static class UriFilter implements Filter {
		public void destroy() {}
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {}
		public void init(FilterConfig filterConfig) throws ServletException {}
	}

	private static class UriFilterWithParameters implements Filter {
		public void destroy() {}
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {}
		public void init(FilterConfig filterConfig) throws ServletException {}
	}
	
	private static class RegexFilter implements Filter {
		public void destroy() {}
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {}
		public void init(FilterConfig filterConfig) throws ServletException {}
	}
	
	
	@SuppressWarnings("serial")
	private static class UriServlet extends HttpServlet {}
	private static class RegexServlet extends HttpServlet {}
	private static class UriServletWithParameters extends HttpServlet {}
	
	private Injector injector = Guice.createInjector(new ServletModule() {
		protected void configureServlets() {
			bind(UriServlet.class).asEagerSingleton();
			serve("/test/*", "/second/test/*").with(UriServlet.class);

			bind(UriServletWithParameters.class).asEagerSingleton();
			serve("/test/withParams/*").with(UriServlet.class, PARAMS);
			
			bind(UriFilter.class).asEagerSingleton();
			filter("/to/filter/*").through(UriFilter.class);
			
			bind(UriFilterWithParameters.class).asEagerSingleton();
			filter("/filter/withParams/*").through(UriFilterWithParameters.class, FILTER_PARAMS);
			
			bind(RegexServlet.class).asEagerSingleton();
			serveRegex("/regex/(test|impl)/[abc]*/.*.html").with(RegexServlet.class);
			
			bind(RegexFilter.class).asEagerSingleton();
			filterRegex("/regex/(test|impl)/[def]*/.*.html").through(RegexFilter.class);
		};
	});
	
	@Before
	public void setUp() throws Exception {
		// init guice filter to force loading of servlets and filters
		injector.getInstance(GuiceFilter.class).init(new DummyFilterConfig());
	}
	
	@Test
	public void testThatUriIsServed() throws Exception {
		assertThat(injector, serves("/test/uri/*").with(UriServlet.class));
	}
	
	@Test(expected = AssertionError.class)
	public void testThatAssertionErrorIsThrownWhenUriIsNotServed() throws Exception {
		assertThat(injector, serves("/incorrect/uri/").with(UriServlet.class));
	}
	
	@Test
	public void testThatSecondUriIsServed() throws Exception {
		assertThat(injector, serves("/second/test/uri/*").with(UriServlet.class));
	}
	
	@Test
	public void testThatPatternIsServed() throws Exception {
		assertThat(injector, servesPattern("/test/*").with(UriServlet.class));
	}

	@Test
	public void testThatSecondPatternIsServed() throws Exception {
		assertThat(injector, servesPattern("/second/test/*").with(UriServlet.class));
	}
	
	@Test(expected = AssertionError.class)
	public void testThatAssertionErrorIsThrownWhenIncorrectPatternIsAsserted() throws Exception {
		assertThat(injector, servesPattern("/incorrect/pattern/*").with(UriServlet.class));
	}

	@Test
	public void testThatUriIsFiltered() throws Exception {
		assertThat(injector, filters("/to/filter/a").through(UriFilter.class));
	}
	
	@Test(expected = AssertionError.class)
	public void testThatAssertionErrorIsThrownWhenIncorrectUriIsAssertedForFilter() throws Exception {
		assertThat(injector, filters("/incorrect/uri").through(UriFilter.class));
	}
	
	@Test
	public void testThatServletIsInitializedWithParams() throws Exception {
		assertThat(injector, serves("/second/test/123").with(UriServlet.class));
	}

	@Test(expected = AssertionError.class)
	public void testThatServletIsNotInitializedWithParams() throws Exception {
		assertThat(injector, serves("/test/withParams/a").with(UriServletWithParameters.class, Collections.singletonMap("key1", "value1")));
	}
	
	@Test
	public void testThatFilterIsInitializedWithParameters() throws Exception {
		assertThat(injector, filters("/filter/withParams/a").through(UriFilterWithParameters.class, FILTER_PARAMS));
	}
	
	@Test(expected = AssertionError.class)
	public void testThatAssertionErrorIsThrownIfTestChecksForNonExistingParameter() throws Exception {
		assertThat(injector, filters("/filter/withParams/a").through(UriFilterWithParameters.class, Collections.singletonMap("notExisting", "value")));
	}
	
	@Test
	public void testThatServletServesRegex() throws Exception {
		assertThat(injector, serves("/regex/test/abcacb/index.html").with(RegexServlet.class));
	}
	
	@Test(expected = AssertionError.class)
	public void testThatAssertionErrorIsThrownWhenServletDoesNotServeRegexBecauseExtensionIsDifferent() throws Exception {
		assertThat(injector, serves("/regex/test/abc/index.xml").with(RegexServlet.class));
	}
	
	@Test
	public void testThatFilterFiltersRegex() throws Exception {
		assertThat(injector, filters("/regex/test/def/index.html").through(RegexFilter.class));
	}
	
	@Test(expected = AssertionError.class)
	public void testThatAssertionErrorIsThrownWhenFilterDoesNotFilterRegexBecauseExtensionIsDifferent() throws Exception {
		assertThat(injector, filters("/regex/test/def/index.xml").through(RegexFilter.class));
	}	

	@Test
	public void testThatFilterFiltersPattern() throws Exception {
		assertThat(injector, filtersPattern("/to/filter/*").through(UriFilter.class));
	}
	
	@Test(expected = AssertionError.class)
	public void testThatAssertionErrorIsThrownWhenIfPatternIsIncorrect() throws Exception {
		assertThat(injector, filtersPattern("/incorrect/path/*").through(UriFilter.class));
	}
	
	private final class DummyFilterConfig implements FilterConfig {
		public ServletContext getServletContext() {
			return null;
		}

		public Enumeration getInitParameterNames() {
			return null;
		}

		public String getInitParameter(String name) {
			return null;
		}

		public String getFilterName() {
			return "guice-filter";
		}
	}
}
