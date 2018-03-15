/*******************************************************************************
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.test;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.ui.view.swt.reference.CreateNewModelElementStrategy;
import org.eclipse.emf.ecp.ui.view.swt.reference.CreateNewModelElementStrategy.Provider;
import org.eclipse.emf.ecp.ui.view.swt.reference.DefaultCreateNewModelElementStrategyProvider;
import org.eclipse.emf.ecp.ui.view.swt.reference.EClassSelectionStrategy;
import org.eclipse.emfforms.bazaar.Bazaar;
import org.eclipse.emfforms.bazaar.BazaarContext;
import org.eclipse.emfforms.bazaar.Bid;
import org.eclipse.emfforms.bazaar.Create;
import org.eclipse.emfforms.common.Optional;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * @author eugen
 *
 */
public class DefaultCreateNewModelElementStrategyProvider_PTest {

	private DefaultCreateNewModelElementStrategyProvider provider;
	private static BundleContext bundleContext;
	private ServiceReference<Provider> serviceReference;
	private Bazaar<CreateNewModelElementStrategy> bazaar;

	private BazaarContext context;

	@BeforeClass
	public static void prepare() {
		final Bundle bundle = FrameworkUtil.getBundle(DefaultCreateNewModelElementStrategyProvider_PTest.class);
		bundleContext = bundle.getBundleContext();
	}

	@Before
	public void setup() {
		serviceReference = bundleContext.getServiceReference(CreateNewModelElementStrategy.Provider.class);
		provider = (DefaultCreateNewModelElementStrategyProvider) bundleContext.getService(serviceReference);
		final Bazaar.Builder<CreateNewModelElementStrategy> builder = Bazaar.Builder.with(singleton(provider));
		bazaar = builder.build();
		final Map<String, ?> values = new HashMap<String, Object>();
		values.put(EObject.class.getName(), null);
		values.put(EReference.class.getName(), null);
		context = BazaarContext.Builder.with(values).build();
	}

	@Test
	public void testDefault() {
		final CreateNewModelElementStrategy createProduct = bazaar
			.createProduct(context);

		final Optional<EObject> createNewModelElement = createProduct.createNewModelElement(null,
			EcorePackage.eINSTANCE.getEClass_EAllAttributes());
		assertTrue(createNewModelElement.isPresent());
	}

	@Test
	public void testEClassSelectionStrategy() {
		final TestEClassSelectionStrategyProvider mock = spy(new TestEClassSelectionStrategyProvider());
		when(mock.bid()).thenReturn(1d);
		final EClassSelectionStrategy strategy = mock(EClassSelectionStrategy.class);
		final EClass toCreate = EcorePackage.eINSTANCE.getEPackage();
		final Collection<EClass> eClasses = Collections.singleton(toCreate);
		when(strategy.collectEClasses(isNull(EObject.class), same(EcorePackage.eINSTANCE.getEClass_EAllAttributes()),
			any(Collection.class))).thenReturn(eClasses);
		when(mock.create()).thenReturn(strategy);
		provider.addEClassSelectionStrategyProvider(mock);
		final CreateNewModelElementStrategy createProduct = bazaar
			.createProduct(context);

		final Optional<EObject> createNewModelElement = createProduct.createNewModelElement(null,
			EcorePackage.eINSTANCE.getEClass_EAllAttributes());
		assertTrue(EPackage.class.isInstance(createNewModelElement.get()));

		inOrder(strategy).verify(strategy, calls(1)).collectEClasses(isNull(EObject.class),
			same(EcorePackage.eINSTANCE.getEClass_EAllAttributes()),
			any(Collection.class));
	}

	public class TestEClassSelectionStrategyProvider implements EClassSelectionStrategy.Provider {

		@Bid
		public Double bid() {
			return null;
		}

		@Create
		public EClassSelectionStrategy create() {
			return null;
		}
	}
}
