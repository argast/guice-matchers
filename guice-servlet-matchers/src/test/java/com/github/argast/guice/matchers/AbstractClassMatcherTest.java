package com.github.argast.guice.matchers;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServlet;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.Key;
import com.google.inject.servlet.LinkedFilterBinding;
import com.google.inject.servlet.LinkedServletBinding;
import com.google.inject.servlet.ServletModuleBinding;

@SuppressWarnings("serial")
public class AbstractClassMatcherTest {

	private class TestServlet extends HttpServlet {}
	private class OtherServlet extends HttpServlet {}
	
	@Test
	public void testThatMatcherReturnsFalseIfBindingIsIncorrect() throws Exception {
		LinkedFilterBinding binding = mock(LinkedFilterBinding.class);
		assertFalse(createMatcherInstance().matchesSafely(binding));
	}
	
	@Test
	public void testThatMatcherReturnsFalseIfClassesDontMatch() throws Exception {
		assertFalse(createMatcherInstance().matchesSafely(bindingThatReturnsKeyOf(OtherServlet.class)));
	}
	
	@Test
	public void testThatMatcherReturnsTrueIfBindingTypeIsCorrectAndClassesAreEqual() throws Exception {
		assertTrue(createMatcherInstance().matchesSafely(bindingThatReturnsKeyOf(TestServlet.class)));
	}
	
	@Test
	public void testThatDescriptionIsCorrect() throws Exception {
		StringDescription desc = new StringDescription();
		createMatcherInstance().describeTo(desc);
		assertEquals(String.format("servlet class equal to \"%s\"", TestServlet.class.getName()), desc.toString());
	}

	private LinkedServletBinding bindingThatReturnsKeyOf(final Class<?> clazz) {
		LinkedServletBinding binding = mock(LinkedServletBinding.class);
		when(binding.getLinkedKey()).thenAnswer(new Answer<Key<?>>() {
			public Key<?> answer(InvocationOnMock invocation) throws Throwable {
				return Key.get(clazz);
			}
		});
		return binding;
	}

	private AbstractClassMatcher createMatcherInstance() {
		return new AbstractClassMatcher(TestServlet.class) {
			@Override
			protected boolean correctBindingType(ServletModuleBinding binding) {
				return binding instanceof LinkedServletBinding;
			}
			
			@Override
			protected Class<?> extractClass(ServletModuleBinding binding) {
				return ((LinkedServletBinding)binding).getLinkedKey().getTypeLiteral().getRawType();
			}
			
			@Override
			protected String initialDescription() {
				return "servlet";
			}
		};
	}
}
