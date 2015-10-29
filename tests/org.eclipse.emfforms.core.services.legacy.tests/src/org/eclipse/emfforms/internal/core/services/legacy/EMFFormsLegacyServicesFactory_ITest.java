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

import org.eclipse.emf.ecp.view.spi.context.EMFFormsLegacyServicesFactory;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicesFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class EMFFormsLegacyServicesFactory_ITest {

	private static BundleContext bundleContext;
	private ServiceReference<EMFFormsLegacyServicesFactory> serviceReference;

	private EMFFormsScopedServicesFactory serviceFactory;
	private ServiceReference<EMFFormsScopedServicesFactory> serviceFactoryReference;

	@BeforeClass
	public static void setUpBeforeClass() {
		bundleContext = FrameworkUtil.getBundle(EMFFormsLegacyServicesFactory_ITest.class)
			.getBundleContext();
	}

	@Before
	public void setUp() {
		serviceReference = bundleContext.getServiceReference(EMFFormsLegacyServicesFactory.class);
		bundleContext.getService(serviceReference);

		serviceFactoryReference = bundleContext.getServiceReference(EMFFormsScopedServicesFactory.class);
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
			.getGlobalImmediateService(TestGlobalViewModelService.class);
		assertTrue(globalImmediateService.isPresent());

		final Optional<TestLocalViewModelService> localImmediateService = serviceFactory
			.getLocalImmediateService(TestLocalViewModelService.class);
		assertTrue(localImmediateService.isPresent());
	}

}
