package com.github.argast.guice.matchers;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

public class ServletModuleIntegrationTest {

	private class FakeFilter implements Filter {
		public void destroy() {}
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {}
		public void init(FilterConfig filterConfig) throws ServletException {}
	}
	
	private Injector injector = Guice.createInjector(new ServletModule() {
		protected void configureServlets() {
			serve("/test/*").with(HttpServlet.class);
			serve("/other/*").with(HttpServlet.class);
			
			filter("/to/filter/*").through(FakeFilter.class);
		};
	});
	
	
	@Test
	public void testThatUriIsServed() throws Exception {
		GuiceServletMatchers.assertServlet(HttpServlet.class).serves("/test/a/*").on(injector);
	}
	
	@Test
	public void testThatPatternIsServed() throws Exception {
		GuiceServletMatchers.assertServlet(HttpServlet.class).servesPattern("/test/*").on(injector);
	}
	
	@Test
	public void testThatUriIsFiltered() throws Exception {
		GuiceServletMatchers.assertFilter(FakeFilter.class).filters("/to/filter/*").on(injector);
	}
	
}
