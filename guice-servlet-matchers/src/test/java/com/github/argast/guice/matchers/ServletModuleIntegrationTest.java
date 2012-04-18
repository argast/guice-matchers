package com.github.argast.guice.matchers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
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

public class ServletModuleIntegrationTest {

	private static class FakeFilter implements Filter {
		public void destroy() {}
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {}
		public void init(FilterConfig filterConfig) throws ServletException {}
	}
	
	private static class FakeServlet extends HttpServlet {
		
		public FakeServlet() {
			System.out.println("bb");
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void init(ServletConfig config) throws ServletException {
			System.out.println("aa");
			super.init(config);
		}
	}
	
	private Injector injector = Guice.createInjector(new ServletModule() {
		protected void configureServlets() {
			bind(FakeServlet.class);
			serve("/test/*").with(FakeServlet.class);
			serve("/other/*").with(FakeServlet.class, Collections.singletonMap("key", "value"));
			
			filter("/to/filter/*").through(FakeFilter.class);
		};
	});
	
	@Before
	public void setUp() throws Exception {
		injector.getInstance(GuiceFilter.class).init(new FilterConfig() {
			
			public ServletContext getServletContext() {
				return null;
			}
			
			public Enumeration getInitParameterNames() {
				return new Vector().elements();
			}
			
			public String getInitParameter(String name) {
				// TODO Auto-generated method stub
				return "";
			}
			
			public String getFilterName() {
				// TODO Auto-generated method stub
				return "guice-filter";
			}
		});
	}
	
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
	
	@Test
	public void testThatServletIsInitializeWithParam() throws Exception {
		GuiceServletMatchers.assertServlet(FakeServlet.class).hasInitParameter("key", "value").on(injector);
	}
	
}
