/*******************************************************************************
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.datatemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emfforms.core.services.datatemplate.test.model.audit.AuditPackage;
import org.eclipse.emfforms.datatemplate.Template;
import org.eclipse.emfforms.internal.core.services.label.BundleResolver;
import org.eclipse.emfforms.spi.localization.EMFFormsLocalizationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.osgi.framework.Bundle;

/**
 * Unit tests for {@link BlankTemplateProvider}.
 *
 * @author Lucas Koehler
 *
 */
@SuppressWarnings("restriction")
public class BlankTemplateProvider_Test {

	private BlankTemplateProvider provider;

	/**
	 * Set up the test cases.
	 */
	@Before
	public void setUp() {
		provider = new BlankTemplateProvider();
		final EMFFormsLocalizationService localizationService = mock(EMFFormsLocalizationService.class);
		when(localizationService.getString(BlankTemplateProvider.class,
			MessageKeys.BlankTemplateProvider_blankTemplateLabel)).thenReturn("Blank {0}"); //$NON-NLS-1$
		when(localizationService.getString(any(Bundle.class), any(String.class))).thenAnswer(new Answer<String>() {

			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				final String classKey = (String) invocation.getArguments()[1];
				return classKey.substring(4, classKey.length() - 5);
			}
		});

		final BundleResolver bundleResolver = mock(BundleResolver.class);
		provider.setBundleResolver(bundleResolver);
		provider.setLocalizationService(localizationService);
	}

	@Test
	public void testCanProvide_ConcreteClass() {
		assertTrue(provider.canProvide(AuditPackage.eINSTANCE.getGuestUser()));
	}

	@Test
	public void testCanProvide_ConcreteSubClass() {
		assertTrue(provider.canProvide(AuditPackage.eINSTANCE.getUser()));
	}

	@Test
	public void testCanProvide_NoConcreteSubClass() {
		assertFalse(provider.canProvide(AuditPackage.eINSTANCE.getAbstractSubUser()));
	}

	@Test
	public void testProvide_NoSubClasses() {
		final Set<Template> result = provider.provide(AuditPackage.eINSTANCE.getPrivilegedUser());
		assertEquals(1, result.size());
		final Template template = result.iterator().next();
		assertEquals(AuditPackage.eINSTANCE.getAdminUser(), template.getInstance().eClass());
		assertEquals("Blank AdminUser", template.getName()); //$NON-NLS-1$
	}

	@Test
	public void testProvide_ConcreteSubClasses() {
		final Set<Template> result = provider.provide(AuditPackage.eINSTANCE.getUser());
		assertEquals(3, result.size());
		assertTrue(hasTemplateWithName(result, "Blank AdminUser")); //$NON-NLS-1$
		assertTrue(hasTemplateWithName(result, "Blank GuestUser")); //$NON-NLS-1$
		assertTrue(hasTemplateWithName(result, "Blank RegisteredUser")); //$NON-NLS-1$

	}

	private boolean hasTemplateWithName(Collection<Template> templates, String name) {
		for (final Template template : templates) {
			if (template.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void testProvide_NonConcreteSubClass() {
		final Set<Template> result = provider.provide(AuditPackage.eINSTANCE.getAbstractSubUser());
		assertEquals(0, result.size());
	}
}
