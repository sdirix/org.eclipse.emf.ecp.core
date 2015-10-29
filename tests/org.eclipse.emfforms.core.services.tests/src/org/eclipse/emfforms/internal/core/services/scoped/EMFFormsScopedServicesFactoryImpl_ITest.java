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
package org.eclipse.emfforms.internal.core.services.scoped;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicePolicy;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceScope;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicesFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class EMFFormsScopedServicesFactoryImpl_ITest {

	private static BundleContext bundleContext;
	private EMFFormsScopedServicesFactory service;
	private ServiceReference<EMFFormsScopedServicesFactory> serviceReference;
	private EMFFormsScopedServiceProvider scopedServiceProvider;

	@BeforeClass
	public static void setUpBeforeClass() {
		bundleContext = FrameworkUtil.getBundle(EMFFormsScopedServicesFactoryImpl_ITest.class)
			.getBundleContext();
	}

	@Before
	public void setUp() throws DatabindingFailedException {
		final Dictionary<String, Object> dictionary = new Hashtable<String, Object>();
		dictionary.put("service.ranking", 50); //$NON-NLS-1$
		scopedServiceProvider = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		when(scopedServiceProvider.provideService()).thenReturn(mock(Object.class));
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.LAZY);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsScopedServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);

		bundleContext.registerService(EMFFormsScopedServiceProvider.class, scopedServiceProvider, dictionary);
		serviceReference = bundleContext.getServiceReference(EMFFormsScopedServicesFactory.class);
		service = bundleContext.getService(serviceReference);
	}

	@After
	public void tearDown() {
		bundleContext.ungetService(serviceReference);
	}

	@Test
	public void testServiceType() throws DatabindingFailedException {
		assertTrue(EMFFormsScopedServicesFactoryImpl.class.isInstance(service));
		verify(scopedServiceProvider, atLeastOnce()).getPolicy();
		verify(scopedServiceProvider, atLeastOnce()).getScope();
		verify(scopedServiceProvider, atLeastOnce()).getPriority();
		verify(scopedServiceProvider, atLeastOnce()).getType();

	}

}
