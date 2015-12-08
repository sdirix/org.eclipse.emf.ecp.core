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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicePolicy;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceScope;
import org.junit.Before;
import org.junit.Test;

public class EMFFormsScopedServicesFactoryImpl_Test {

	private EMFFormsScopedServicesFactoryImpl emfFormsScopedServicesFactory;

	@Before
	public void before() {
		emfFormsScopedServicesFactory = new EMFFormsScopedServicesFactoryImpl();
	}

	@Test
	public void testAddedToLocalLazy() {
		final EMFFormsScopedServiceProvider<?> scopedServiceProvider = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(mock(Object.class)).when(scopedServiceProvider).provideService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.LAZY);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsScopedServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertTrue(emfFormsScopedServicesFactory.getLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testAddedToGlobalImmediate() {
		final EMFFormsScopedServiceProvider<?> scopedServiceProvider = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(mock(Object.class)).when(scopedServiceProvider).provideService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsScopedServiceScope.GLOBAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.getLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getLocalImmediateService(Object.class).isPresent());
		assertTrue(emfFormsScopedServicesFactory.getGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testAddedToGlobalLazy() {
		final EMFFormsScopedServiceProvider<?> scopedServiceProvider = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).provideService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.LAZY);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsScopedServiceScope.GLOBAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.getLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalImmediateService(Object.class).isPresent());
		assertTrue(emfFormsScopedServicesFactory.getGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testAddedToLocalImmediate() {
		final EMFFormsScopedServiceProvider<?> scopedServiceProvider = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).provideService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsScopedServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.getLocalLazyService(Object.class).isPresent());
		assertTrue(emfFormsScopedServicesFactory.getLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testRemoveFromLocalLazy() {
		final EMFFormsScopedServiceProvider<?> scopedServiceProvider = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).provideService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.LAZY);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsScopedServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		emfFormsScopedServicesFactory.removeEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.getLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testRemoveFromGlobalImmediate() {
		final EMFFormsScopedServiceProvider<?> scopedServiceProvider = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).provideService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsScopedServiceScope.GLOBAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		emfFormsScopedServicesFactory.removeEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.getLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testRemoveFromGlobalLazy() {
		final EMFFormsScopedServiceProvider<?> scopedServiceProvider = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).provideService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.LAZY);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsScopedServiceScope.GLOBAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		emfFormsScopedServicesFactory.removeEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.getLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testRemoveFromLocalImmediate() {
		final EMFFormsScopedServiceProvider<?> scopedServiceProvider = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider).getType();
		doReturn(Object.class).when(scopedServiceProvider).provideService();
		when(scopedServiceProvider.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.IMMEDIATE);
		when(scopedServiceProvider.getScope()).thenReturn(EMFFormsScopedServiceScope.LOCAL);
		when(scopedServiceProvider.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider);
		emfFormsScopedServicesFactory.removeEMFFormsScopedServiceProvider(scopedServiceProvider);
		assertFalse(emfFormsScopedServicesFactory.getLocalLazyService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getLocalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalImmediateService(Object.class).isPresent());
		assertFalse(emfFormsScopedServicesFactory.getGlobalLazyService(Object.class).isPresent());
	}

	@Test
	public void testAddedToLocalLazyPriorityFirstLowThenHigh() {
		final EMFFormsScopedServiceProvider<?> scopedServiceProvider1 = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider1).getType();
		final Object mock1 = mock(Object.class);
		doReturn(mock1).when(scopedServiceProvider1).provideService();
		when(scopedServiceProvider1.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.LAZY);
		when(scopedServiceProvider1.getScope()).thenReturn(EMFFormsScopedServiceScope.LOCAL);
		when(scopedServiceProvider1.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider1);

		final EMFFormsScopedServiceProvider<?> scopedServiceProvider2 = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider2).getType();
		final Object mock2 = mock(Object.class);
		doReturn(mock2).when(scopedServiceProvider2).provideService();
		when(scopedServiceProvider2.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.LAZY);
		when(scopedServiceProvider2.getScope()).thenReturn(EMFFormsScopedServiceScope.LOCAL);
		when(scopedServiceProvider2.getPriority()).thenReturn(2d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider2);

		assertSame(mock2, emfFormsScopedServicesFactory.getLocalLazyService(Object.class).get());
	}

	@Test
	public void testAddedToLocalLazyPriorityFirstHighThenLow() {

		final EMFFormsScopedServiceProvider<?> scopedServiceProvider2 = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider2).getType();
		final Optional<Object> mock2 = Optional.of(mock(Object.class));
		doReturn(mock2).when(scopedServiceProvider2).provideService();
		when(scopedServiceProvider2.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.LAZY);
		when(scopedServiceProvider2.getScope()).thenReturn(EMFFormsScopedServiceScope.LOCAL);
		when(scopedServiceProvider2.getPriority()).thenReturn(2d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider2);

		final EMFFormsScopedServiceProvider<?> scopedServiceProvider1 = mock(EMFFormsScopedServiceProvider.class);
		doReturn(Object.class).when(scopedServiceProvider1).getType();
		final Optional<Object> mock1 = Optional.of(mock(Object.class));
		doReturn(mock1).when(scopedServiceProvider1).provideService();
		when(scopedServiceProvider1.getPolicy()).thenReturn(EMFFormsScopedServicePolicy.LAZY);
		when(scopedServiceProvider1.getScope()).thenReturn(EMFFormsScopedServiceScope.LOCAL);
		when(scopedServiceProvider1.getPriority()).thenReturn(1d);
		emfFormsScopedServicesFactory.addEMFFormsScopedServiceProvider(scopedServiceProvider1);
		assertSame(mock2, emfFormsScopedServicesFactory.getLocalLazyService(Object.class).get());
	}
}
