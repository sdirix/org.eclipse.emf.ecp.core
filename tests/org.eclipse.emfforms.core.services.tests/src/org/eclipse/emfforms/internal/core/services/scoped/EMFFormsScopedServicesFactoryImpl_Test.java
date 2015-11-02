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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.core.services.view.EMFFormsViewServiceFactory;
import org.eclipse.emfforms.spi.core.services.view.EMFFormsViewServicePolicy;
import org.eclipse.emfforms.spi.core.services.view.EMFFormsViewServiceScope;
import org.junit.Before;
import org.junit.Test;

public class EMFFormsScopedServicesFactoryImpl_Test {

	private EMFFormsViewServiceManagerImpl emfFormsScopedServicesFactory;

	@Before
	public void before() {
		emfFormsScopedServicesFactory = new EMFFormsViewServiceManagerImpl();
	}

	@Test
	public void testAddedToLocalLazy() {
		final EMFFormsViewServiceFactory<?> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(mock(Object.class)).when(scopedServiceProvider).createService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.LAZY);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertTrue(emfFormsScopedServicesFactory.createLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testAddedToGlobalImmediate() {
		final EMFFormsViewServiceFactory<?> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(mock(Object.class)).when(scopedServiceProvider).createService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.GLOBAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.createLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createLocalImmediateService(Object.class).isPresent());
		assertTrue(emfFormsScopedServicesFactory.createGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testAddedToGlobalLazy() {
		final EMFFormsViewServiceFactory<?> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).createService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.LAZY);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.GLOBAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.createLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalImmediateService(Object.class).isPresent());
		assertTrue(emfFormsScopedServicesFactory.createGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testAddedToLocalImmediate() {
		final EMFFormsViewServiceFactory<?> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).createService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.createLocalLazyService(Object.class).isPresent());
		assertTrue(emfFormsScopedServicesFactory.createLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testRemoveFromLocalLazy() {
		final EMFFormsViewServiceFactory<?> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).createService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.LAZY);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		emfFormsScopedServicesFactory.removeEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.createLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testRemoveFromGlobalImmediate() {
		final EMFFormsViewServiceFactory<?> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).createService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.GLOBAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		emfFormsScopedServicesFactory.removeEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.createLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testRemoveFromGlobalLazy() {
		final EMFFormsViewServiceFactory<?> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).createService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.LAZY);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.GLOBAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		emfFormsScopedServicesFactory.removeEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.createLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testRemoveFromLocalImmediate() {
		final EMFFormsViewServiceFactory<?> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).createService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		emfFormsScopedServicesFactory.removeEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.createLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.createGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testAddedToLocalLazyPriorityFirstLowThenHigh() {
		final EMFFormsViewServiceFactory<?> scopedServiceProvider1 = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider1).getType();
		final Object mock1 = mock(Object.class);
		doReturn(mock1).when(scopedServiceProvider1).createService();
		when(scopedServiceProvider1.getPolicy()).thenReturn(EMFFormsViewServicePolicy.LAZY);
		when(scopedServiceProvider1.getScope()).thenReturn(EMFFormsViewServiceScope.LOCAL);
		when(scopedServiceProvider1.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider1);

		final EMFFormsViewServiceFactory<?> scopedServiceProvider2 = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider2).getType();
		final Object mock2 = mock(Object.class);
		doReturn(mock2).when(scopedServiceProvider2).createService();
		when(scopedServiceProvider2.getPolicy()).thenReturn(EMFFormsViewServicePolicy.LAZY);
		when(scopedServiceProvider2.getScope()).thenReturn(EMFFormsViewServiceScope.LOCAL);
		when(scopedServiceProvider2.getPriority()).thenReturn(2d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider2);

		assertSame(mock2, emfFormsScopedServicesFactory.createLocalLazyService(Object.class).get());
	}

	@Test
	public void testAddedToLocalLazyPriorityFirstHighThenLow() {

		final EMFFormsViewServiceFactory<?> scopedServiceProvider2 = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider2).getType();
		final Optional<Object> mock2 = Optional.of(mock(Object.class));
		doReturn(mock2).when(scopedServiceProvider2).createService();
		when(scopedServiceProvider2.getPolicy()).thenReturn(EMFFormsViewServicePolicy.LAZY);
		when(scopedServiceProvider2.getScope()).thenReturn(EMFFormsViewServiceScope.LOCAL);
		when(scopedServiceProvider2.getPriority()).thenReturn(2d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider2);

		final EMFFormsViewServiceFactory<?> scopedServiceProvider1 = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider1).getType();
		final Optional<Object> mock1 = Optional.of(mock(Object.class));
		doReturn(mock1).when(scopedServiceProvider1).createService();
		when(scopedServiceProvider1.getPolicy()).thenReturn(EMFFormsViewServicePolicy.LAZY);
		when(scopedServiceProvider1.getScope()).thenReturn(EMFFormsViewServiceScope.LOCAL);
		when(scopedServiceProvider1.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider1);
		assertSame(mock2, emfFormsScopedServicesFactory.createLocalLazyService(Object.class).get());
	}

	@Test
	public void testGetAllGlobalImmediateServiceTypesNoRegistered() {
		final Set<Class<?>> serviceTypes = emfFormsScopedServicesFactory.getAllGlobalImmediateServiceTypes();
		assertNotNull(serviceTypes);
		assertTrue(serviceTypes.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllGlobalImmediateServiceTypes() {
		final EMFFormsViewServiceFactory<Object> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		when(scopedServiceProvider.createService()).thenReturn(mock(Object.class));
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.GLOBAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);

		final Set<Class<?>> serviceTypes = emfFormsScopedServicesFactory.getAllGlobalImmediateServiceTypes();
		assertNotNull(serviceTypes);
		assertEquals(1, serviceTypes.size());
		assertEquals(Object.class, serviceTypes.iterator().next());
	}

	@Test
	public void testGetAllLocalImmediateServiceTypesNoRegistered() {
		final Set<Class<?>> serviceTypes = emfFormsScopedServicesFactory.getAllLocalImmediateServiceTypes();
		assertNotNull(serviceTypes);
		assertTrue(serviceTypes.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllLocalImmediateServiceTypes() {
		final EMFFormsViewServiceFactory<Object> scopedServiceProvider = mock(EMFFormsViewServiceFactory.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		when(scopedServiceProvider.createService()).thenReturn(mock(Object.class));
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsViewServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsViewServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);

		final Set<Class<?>> serviceTypes = emfFormsScopedServicesFactory.getAllLocalImmediateServiceTypes();
		assertNotNull(serviceTypes);
		assertEquals(1, serviceTypes.size());
		assertEquals(Object.class, serviceTypes.iterator().next());
	}
}
