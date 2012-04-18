package com.github.argast.guice.matchers;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

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
import static com.github.argast.guice.matchers.GuiceServletMatchers.*; 

public class ServletModuleIntegrationTest {

	private static class FakeFilter implements Filter {
		public void destroy() {}
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {}
		public void init(FilterConfig filterConfig) throws ServletException {}
	}
	
	@SuppressWarnings("serial")
	private static class FakeServlet extends HttpServlet {}
	
	private Injector injector = Guice.createInjector(new ServletModule() {
		protected void configureServlets() {
			bind(FakeServlet.class).asEagerSingleton();
			serve("/test/*", "/second/test/*").with(FakeServlet.class, Collections.singletonMap("key", "value"));
			
			bind(FakeFilter.class).asEagerSingleton();
			filter("/to/filter/*").through(FakeFilter.class, Collections.singletonMap("filterKey", "filterValue"));
		};
	});
	
	@Before
	public void setUp() throws Exception {
		// init guice filter to force loading of servlets and filters
		injector.getInstance(GuiceFilter.class).init(new DummyFilterConfig());
	}
	
	@Test
	public void testThatUriIsServed() throws Exception {
		assertServlet(FakeServlet.class).serves("/test/uri/*").on(injector);
	}
	
	@Test
	public void testThatSecondUriIsServed() throws Exception {
		assertServlet(FakeServlet.class).serves("/second/test/uri/*").on(injector);
	}
	
	@Test
	public void testThatPatternIsServed() throws Exception {
		assertServlet(FakeServlet.class).servesPattern("/test/*").on(injector);
	}

	@Test
	public void testThatSecondPatternIsServed() throws Exception {
		assertServlet(FakeServlet.class).servesPattern("/second/test/*").on(injector);
	}
	
	@Test
	public void testThatUriIsFiltered() throws Exception {
		assertFilter(FakeFilter.class).filters("/to/filter/*").on(injector);
	}
	
	@Test
	public void testThatServletIsInitializedWithParam() throws Exception {
		assertServlet(FakeServlet.class).hasInitParameter("key", "value").on(injector);
	}
	
	@Test
	public void testThatFilterIsInitializedWithParameter() throws Exception {
		assertFilter(FakeFilter.class).hasInitParameter("filterKey", "filterValue").on(injector);
	}
	
	@Test(expected = AssertionError.class)
	public void testThatAssertionErrorIsThrownIfTestChecksForUnusedParameter() throws Exception {
		assertFilter(FakeFilter.class).hasInitParameter("noSuchKey", "noSuchValue").on(injector);
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
