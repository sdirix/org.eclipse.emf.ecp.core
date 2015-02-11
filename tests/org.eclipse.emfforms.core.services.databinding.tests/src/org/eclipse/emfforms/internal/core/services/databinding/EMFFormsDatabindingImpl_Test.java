/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.databinding;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter;
import org.junit.Before;
import org.junit.Test;

/**
 * This JUnit test tests the correct functionality of {@link EMFFormsDatabindingImpl}.
 *
 * @author Lucas Koehler
 *
 */
public class EMFFormsDatabindingImpl_Test {

	private EMFFormsDatabindingImpl databindingService;

	/**
	 * Set up that is executed before every test.
	 */
	@Before
	public void setUp() {
		databindingService = new EMFFormsDatabindingImpl();
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableValue(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 */
	@Test
	public void testGetObservableValue() {
		final Realm realm = mock(Realm.class);
		databindingService = spy(new EMFFormsDatabindingImpl(realm));
		final VDomainModelReference reference = mock(VDomainModelReference.class);
		final EObject eObject = mock(EObject.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final IValueProperty expectedResultProperty = mock(IValueProperty.class);
		final IObservableValue expectedObservableValue = mock(IObservableValue.class);

		when(converter1.isApplicable(reference)).thenReturn(0d);
		when(converter1.convert(reference)).thenReturn(expectedResultProperty);
		when(expectedResultProperty.observe(realm, eObject)).thenReturn(expectedObservableValue);

		databindingService.addDomainModelReferenceConverter(converter1);
		final IObservableValue resultObservableValue = databindingService.getObservableValue(reference, eObject);

		verify(databindingService).getValueProperty(reference);
		verify(expectedResultProperty).observe(realm, eObject);
		assertEquals(expectedObservableValue, resultObservableValue);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableValue(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 * <p>
	 * Tests whether the method returns the correct result for both arguments being <strong>null</strong>.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetObservableValueNullBoth() {
		databindingService.getObservableValue(null, null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableValue(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 * <p>
	 * Tests whether the method returns the correct result for the VDomainModelReference argument being
	 * <strong>null</strong>.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetObservableValueNullDomainModelReference() {
		databindingService.getObservableValue(null, mock(EObject.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableValue(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 * <p>
	 * Tests whether the method returns the correct result for the EObject argument being <strong>null</strong>.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetObservableValueNullObject() {
		databindingService.getObservableValue(mock(VDomainModelReference.class), null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test(expected = IllegalStateException.class)
	public void testGetValuePropertyNoApplicableConverter() {
		databindingService.getValueProperty(mock(VDomainModelReference.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 * Tests whether the correct converter is used when one is applicable and one is not.
	 */
	@Test
	public void testGetValuePropertyOneApplicable() {
		final VDomainModelReference reference = mock(VDomainModelReference.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter2 = mock(DomainModelReferenceConverter.class);
		final IValueProperty expectedResultProperty = mock(IValueProperty.class);

		when(converter1.isApplicable(reference)).thenReturn(0d);
		when(converter1.convert(reference)).thenReturn(expectedResultProperty);
		when(converter2.isApplicable(reference)).thenReturn(DomainModelReferenceConverter.NOT_APPLICABLE);
		when(converter2.convert(reference)).thenReturn(mock(IValueProperty.class));

		databindingService.addDomainModelReferenceConverter(converter1);
		databindingService.addDomainModelReferenceConverter(converter2);
		final IValueProperty valueProperty = databindingService.getValueProperty(reference);
		assertEquals(expectedResultProperty, valueProperty);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 * Tests whether the correct converter is used when there are two applicable ones with different priorities.
	 * Also tests whether the correct result is returned.
	 */
	@Test
	public void testGetValuePropertyTwoApplicable() {
		final VDomainModelReference reference = mock(VFeaturePathDomainModelReference.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter2 = mock(DomainModelReferenceConverter.class);
		final IValueProperty expectedResultProperty = mock(IValueProperty.class);

		when(converter1.isApplicable(reference)).thenReturn(5d);
		when(converter1.convert(reference)).thenReturn(expectedResultProperty);
		when(converter2.isApplicable(reference)).thenReturn(1d);
		when(converter2.convert(reference)).thenReturn(mock(SimpleValueProperty.class));

		databindingService.addDomainModelReferenceConverter(converter1);
		databindingService.addDomainModelReferenceConverter(converter2);
		final IValueProperty valueProperty = databindingService.getValueProperty(reference);
		assertEquals(expectedResultProperty, valueProperty);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 * Tests whether the {@link EMFFormsDatabindingImpl} considers all {@link DomainModelReferenceConverter}s, that are
	 * registered to it, for its conversions.
	 */
	@Test
	public void testGetValuePropertyAllConsidered() {
		final VDomainModelReference reference = mock(VDomainModelReference.class);

		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter2 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter3 = mock(DomainModelReferenceConverter.class);

		databindingService.addDomainModelReferenceConverter(converter1);
		databindingService.addDomainModelReferenceConverter(converter2);
		databindingService.addDomainModelReferenceConverter(converter3);

		databindingService.getValueProperty(reference);

		verify(converter1).isApplicable(reference);
		verify(converter2).isApplicable(reference);
		verify(converter3).isApplicable(reference);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 * <p>
	 * Tests whether the method returns the correct result for a <strong>null</strong> argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetValuePropertyNull() {
		databindingService.getValueProperty(null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#removeDomainModelReferenceConverter(org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter)}
	 * .
	 */
	@Test(expected = IllegalStateException.class)
	public void testRemoveDomainModelReferenceConverter() {
		final VDomainModelReference reference = mock(VFeaturePathDomainModelReference.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);

		when(converter1.isApplicable(reference)).thenReturn(5d);

		databindingService.addDomainModelReferenceConverter(converter1);
		databindingService.removeDomainModelReferenceConverter(converter1);
		databindingService.getValueProperty(reference);
	}
}
