package com.github.argast.guice.matchers;

import javax.servlet.http.HttpServlet;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

public class ServletModuleTest {

	private Injector injector = Guice.createInjector(new ServletModule() {
		protected void configureServlets() {
			serve("/test/*").with(HttpServlet.class);
			serve("/other/*").with(HttpServlet.class);
		};
	});
	
	
	@Test
	public void testThatUriIsServed() throws Exception {
		GuiceServletMatchers.assertServlet(HttpServlet.class).serves("/test1/a/*").on(injector);
	}
	
	@Test
	public void testThatPatternIsServed() throws Exception {
		GuiceServletMatchers.assertServlet(HttpServlet.class).servesPattern("/test/a/*").on(injector);
	}
	
}
