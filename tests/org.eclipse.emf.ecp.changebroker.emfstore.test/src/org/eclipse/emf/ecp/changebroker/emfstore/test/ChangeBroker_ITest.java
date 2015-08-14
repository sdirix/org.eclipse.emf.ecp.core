/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * jfaltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.changebroker.emfstore.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.changebroker.emfstore.internal.ECPNotificationProvider;
import org.eclipse.emf.ecp.changebroker.spi.ChangeBroker;
import org.eclipse.emf.ecp.changebroker.spi.ChangeObserver;
import org.eclipse.emf.ecp.changebroker.spi.NotificationProvider;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.workspace.internal.core.WorkspaceProvider;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Tests the integration with the ChangeBroker service
 *
 * @author jfaltermeier
 *
 */
@SuppressWarnings("restriction")
public class ChangeBroker_ITest {

	@Test
	public void testServiceAvailable() {
		final BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		final ServiceReference<ChangeBroker> serviceReference = bundleContext.getServiceReference(ChangeBroker.class);
		assertNotNull("Null service reference", serviceReference); //$NON-NLS-1$
		final ChangeBroker service = bundleContext.getService(serviceReference);
		assertNotNull("ChangeBroker is null", service); //$NON-NLS-1$
		assertTrue(org.eclipse.emf.ecp.changebroker.internal.ChangeBrokerImpl.class.isInstance(service));
		final org.eclipse.emf.ecp.changebroker.internal.ChangeBrokerImpl broker = org.eclipse.emf.ecp.changebroker.internal.ChangeBrokerImpl.class
			.cast(service);
		final Set<NotificationProvider> notificationProviders = broker.getNotificationProviders();
		assertEquals(1, notificationProviders.size());
		assertTrue(ECPNotificationProvider.class.isInstance(notificationProviders.iterator().next()));
		bundleContext.ungetService(serviceReference);

	}

	private Notification receivedNotification;

	@Ignore
	@Test
	public void testWorkspaceProviderIntegration() throws ECPProjectWithNameExistsException {
		final ECPProvider provider = ECPUtil.getECPProviderRegistry().getProvider(WorkspaceProvider.NAME);
		final ECPProperties properties = ECPUtil.createProperties();
		properties.addProperty(WorkspaceProvider.PROP_ROOT_URI, WorkspaceProvider.VIRTUAL_ROOT_URI);
		final ECPProject project = ECPUtil.getECPProjectManager().createProject(provider, "TestProject", properties);
		final EClass testEObject = EcoreFactory.eINSTANCE.createEClass();
		project.getContents().add(testEObject);
		final BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		final ServiceReference<ChangeBroker> serviceReference = bundleContext.getServiceReference(ChangeBroker.class);
		final ChangeBroker broker = bundleContext.getService(serviceReference);
		broker.subscribe(new ChangeObserver() {

			@Override
			public void handleNotification(Notification notification) {
				receivedNotification = notification;

			}
		});
		final String testName = "testName";
		testEObject.setName(testName);
		assertNotNull(receivedNotification);
		assertEquals(testEObject, receivedNotification.getNotifier());
		assertEquals(EcorePackage.eINSTANCE.getENamedElement_Name(), receivedNotification.getFeature());
		assertEquals(testName, receivedNotification.getNewValue());
	}

}
