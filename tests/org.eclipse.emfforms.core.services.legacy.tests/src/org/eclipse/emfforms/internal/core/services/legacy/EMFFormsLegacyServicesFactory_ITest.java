/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.legacy;

import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecp.view.spi.context.EMFFormsLegacyServicesManager;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.core.services.view.EMFFormsViewServiceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class EMFFormsLegacyServicesFactory_ITest {

	private static BundleContext bundleContext;
	private ServiceReference<EMFFormsLegacyServicesManager> serviceReference;

	private EMFFormsViewServiceManager serviceFactory;
	private ServiceReference<EMFFormsViewServiceManager> serviceFactoryReference;

	@BeforeClass
	public static void setUpBeforeClass() {
		bundleContext = FrameworkUtil.getBundle(EMFFormsLegacyServicesFactory_ITest.class)
			.getBundleContext();
	}

	@Before
	public void setUp() {
		serviceReference = bundleContext.getServiceReference(EMFFormsLegacyServicesManager.class);
		final EMFFormsLegacyServicesManager legacyService = bundleContext.getService(serviceReference);
		legacyService.instantiate();

		serviceFactoryReference = bundleContext.getServiceReference(EMFFormsViewServiceManager.class);
		serviceFactory = bundleContext.getService(serviceFactoryReference);
	}

	@After
	public void tearDown() {
		bundleContext.ungetService(serviceReference);
		bundleContext.ungetService(serviceFactoryReference);
	}

	@Test
	public void testExtensionPointParsing() {
		final Optional<TestGlobalViewModelService> globalImmediateService = serviceFactory
			.createGlobalImmediateService(TestGlobalViewModelService.class);
		assertTrue(globalImmediateService.isPresent());

		final Optional<TestLocalViewModelService> localImmediateService = serviceFactory
			.createLocalImmediateService(TestLocalViewModelService.class);
		assertTrue(localImmediateService.isPresent());

		final Optional<ITestGlobalViewModelService> globalImmediateService2 = serviceFactory
			.createGlobalImmediateService(ITestGlobalViewModelService.class);
		assertTrue(globalImmediateService2.isPresent());

		final Optional<ITestViewModelService> localImmediateService2 = serviceFactory
			.createLocalImmediateService(ITestViewModelService.class);
		assertTrue(localImmediateService2.isPresent());
	}

}
