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
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.eclipse.emfforms.core.services.datatemplate.test.model.audit.AdminUser;
import org.eclipse.emfforms.core.services.datatemplate.test.model.audit.AuditFactory;
import org.eclipse.emfforms.core.services.datatemplate.test.model.audit.AuditPackage;
import org.eclipse.emfforms.core.services.datatemplate.test.model.audit.GuestUser;
import org.eclipse.emfforms.core.services.datatemplate.test.model.audit.RegisteredUser;
import org.eclipse.emfforms.datatemplate.DataTemplateFactory;
import org.eclipse.emfforms.datatemplate.Template;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for {@link XmiTemplateProvider}.
 *
 * @author Lucas Koehler
 *
 */
public class XmiTemplateProvider_Test {

	private XmiTemplateProvider provider;
	private Template adminTemplate;
	private Template guestTemplate;
	private Template registeredTemplate;

	@Before
	public void setUp() {
		adminTemplate = DataTemplateFactory.eINSTANCE.createTemplate();
		adminTemplate.setName("Admin Template");
		guestTemplate = DataTemplateFactory.eINSTANCE.createTemplate();
		guestTemplate.setName("Guest Template");
		registeredTemplate = DataTemplateFactory.eINSTANCE.createTemplate();
		registeredTemplate.setName("Registered Template");

		final AdminUser adminUser = AuditFactory.eINSTANCE.createAdminUser();
		adminUser.setLogin("admin");
		final GuestUser guestUser = AuditFactory.eINSTANCE.createGuestUser();
		guestUser.setLogin("guest");
		final RegisteredUser registeredUser = AuditFactory.eINSTANCE.createRegisteredUser();
		registeredUser.setLogin("registered");

		adminTemplate.setInstance(adminUser);
		guestTemplate.setInstance(guestUser);
		registeredTemplate.setInstance(registeredUser);

		provider = new XmiTemplateProvider();
		provider.setReportService(mock(ReportService.class));
		provider.registerTemplate(adminTemplate);
		provider.registerTemplate(guestTemplate);
		provider.registerTemplate(registeredTemplate);
	}

	@Test
	public void testCanProvide() {
		assertTrue(provider.canProvide(AuditPackage.eINSTANCE.getAdminUser()));
		assertTrue(provider.canProvide(AuditPackage.eINSTANCE.getRegisteredUser()));
		assertTrue(provider.canProvide(AuditPackage.eINSTANCE.getGuestUser()));
		assertTrue(provider.canProvide(AuditPackage.eINSTANCE.getUser()));
		assertTrue(provider.canProvide(AuditPackage.eINSTANCE.getPrivilegedUser()));
		assertFalse(provider.canProvide(AuditPackage.eINSTANCE.getUserGroup()));
	}

	@Test
	public void testProvide() {
		// Only the template for admin matches
		final Set<Template> adminSet = provider.provide(AuditPackage.eINSTANCE.getAdminUser());
		assertEquals(1, adminSet.size());
		assertEquals(adminTemplate, adminSet.iterator().next());

		// Only the template for guest matches
		final Set<Template> guestSet = provider.provide(AuditPackage.eINSTANCE.getGuestUser());
		assertEquals(1, guestSet.size());
		assertEquals(guestTemplate, guestSet.iterator().next());

		// The templates for registered and admin (sub class of registered) match
		final Set<Template> registeredSet = provider.provide(AuditPackage.eINSTANCE.getRegisteredUser());
		assertEquals(2, registeredSet.size());
		assertTrue(registeredSet.contains(adminTemplate));
		assertTrue(registeredSet.contains(registeredTemplate));

		// The template for admin (sub class of privileged) matches
		final Set<Template> privilegedSet = provider.provide(AuditPackage.eINSTANCE.getPrivilegedUser());
		assertEquals(1, privilegedSet.size());
		assertEquals(adminTemplate, privilegedSet.iterator().next());

		// All templates match (all other user classes are sub classes of User)
		final Set<Template> userSet = provider.provide(AuditPackage.eINSTANCE.getUser());
		assertEquals(3, userSet.size());
		assertTrue(userSet.contains(adminTemplate));
		assertTrue(userSet.contains(registeredTemplate));
		assertTrue(userSet.contains(guestTemplate));

		// No templates matches for user group
		final Set<Template> userGroupSet = provider.provide(AuditPackage.eINSTANCE.getUserGroup());
		assertEquals(0, userGroupSet.size());
	}
}
