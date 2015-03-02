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
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.list.SimpleListProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
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
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testGetObservableValue() throws DatabindingFailedException {
		final Realm realm = mock(Realm.class);
		databindingService = spy(new EMFFormsDatabindingImpl(realm));
		final VDomainModelReference reference = mock(VDomainModelReference.class);
		final EObject eObject = mock(EObject.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final IValueProperty expectedResultProperty = mock(IValueProperty.class);
		final IObservableValue expectedObservableValue = mock(IObservableValue.class);

		when(converter1.isApplicable(reference)).thenReturn(0d);
		when(converter1.convertToValueProperty(reference)).thenReturn(expectedResultProperty);
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
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetObservableValueNullBoth() throws DatabindingFailedException {
		databindingService.getObservableValue(null, null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableValue(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 * <p>
	 * Tests whether the method returns the correct result for the VDomainModelReference argument being
	 * <strong>null</strong>.
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetObservableValueNullDomainModelReference() throws DatabindingFailedException {
		databindingService.getObservableValue(null, mock(EObject.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableValue(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 * <p>
	 * Tests whether the method returns the correct result for the EObject argument being <strong>null</strong>.
	 * 
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetObservableValueNullObject() throws DatabindingFailedException {
		databindingService.getObservableValue(mock(VDomainModelReference.class), null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 * 
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalStateException.class)
	public void testGetValuePropertyNoApplicableConverter() throws DatabindingFailedException {
		databindingService.getValueProperty(mock(VDomainModelReference.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 * Tests whether the correct converter is used when one is applicable and one is not.
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testGetValuePropertyOneApplicable() throws DatabindingFailedException {
		final VDomainModelReference reference = mock(VDomainModelReference.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter2 = mock(DomainModelReferenceConverter.class);
		final IValueProperty expectedResultProperty = mock(IValueProperty.class);

		when(converter1.isApplicable(reference)).thenReturn(0d);
		when(converter1.convertToValueProperty(reference)).thenReturn(expectedResultProperty);
		when(converter2.isApplicable(reference)).thenReturn(DomainModelReferenceConverter.NOT_APPLICABLE);
		when(converter2.convertToValueProperty(reference)).thenReturn(mock(IValueProperty.class));

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
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testGetValuePropertyTwoApplicable() throws DatabindingFailedException {
		final VDomainModelReference reference = mock(VFeaturePathDomainModelReference.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter2 = mock(DomainModelReferenceConverter.class);
		final IValueProperty expectedResultProperty = mock(IValueProperty.class);

		when(converter1.isApplicable(reference)).thenReturn(5d);
		when(converter1.convertToValueProperty(reference)).thenReturn(expectedResultProperty);
		when(converter2.isApplicable(reference)).thenReturn(1d);
		when(converter2.convertToValueProperty(reference)).thenReturn(mock(SimpleValueProperty.class));

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
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testGetValuePropertyAllConsidered() throws DatabindingFailedException {
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
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetValuePropertyNull() throws DatabindingFailedException {
		databindingService.getValueProperty(null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableList(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testGetObservableList() throws DatabindingFailedException {
		final Realm realm = mock(Realm.class);
		databindingService = spy(new EMFFormsDatabindingImpl(realm));
		final VDomainModelReference reference = mock(VDomainModelReference.class);
		final EObject eObject = mock(EObject.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final IListProperty expectedResultProperty = mock(IListProperty.class);
		final IObservableList expectedObservableList = mock(IObservableList.class);

		when(converter1.isApplicable(reference)).thenReturn(0d);
		when(converter1.convertToListProperty(reference)).thenReturn(expectedResultProperty);
		when(expectedResultProperty.observe(realm, eObject)).thenReturn(expectedObservableList);

		databindingService.addDomainModelReferenceConverter(converter1);
		final IObservableList resultObservableList = databindingService.getObservableList(reference, eObject);

		verify(databindingService).getListProperty(reference);
		verify(expectedResultProperty).observe(realm, eObject);
		assertEquals(expectedObservableList, resultObservableList);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableList(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 * <p>
	 * Tests whether the method returns the correct result for both arguments being <strong>null</strong>.
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetObservableListNullBoth() throws DatabindingFailedException {
		databindingService.getObservableList(null, null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableList(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 * <p>
	 * Tests whether the method returns the correct result for the VDomainModelReference argument being
	 * <strong>null</strong>.
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetObservableListNullDomainModelReference() throws DatabindingFailedException {
		databindingService.getObservableList(null, mock(EObject.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getObservableList(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference, org.eclipse.emf.ecore.EObject)}
	 * .
	 * <p>
	 * Tests whether the method returns the correct result for the EObject argument being <strong>null</strong>.
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetObservableListNullObject() throws DatabindingFailedException {
		databindingService.getObservableList(mock(VDomainModelReference.class), null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalStateException.class)
	public void testGetListPropertyNoApplicableConverter() throws DatabindingFailedException {
		databindingService.getListProperty(mock(VDomainModelReference.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 * Tests whether the correct converter is used when one is applicable and one is not.
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testGetListPropertyOneApplicable() throws DatabindingFailedException {
		final VDomainModelReference reference = mock(VDomainModelReference.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter2 = mock(DomainModelReferenceConverter.class);
		final IListProperty expectedResultProperty = mock(IListProperty.class);

		when(converter1.isApplicable(reference)).thenReturn(0d);
		when(converter1.convertToListProperty(reference)).thenReturn(expectedResultProperty);
		when(converter2.isApplicable(reference)).thenReturn(DomainModelReferenceConverter.NOT_APPLICABLE);
		when(converter2.convertToListProperty(reference)).thenReturn(mock(IListProperty.class));

		databindingService.addDomainModelReferenceConverter(converter1);
		databindingService.addDomainModelReferenceConverter(converter2);
		final IListProperty listProperty = databindingService.getListProperty(reference);
		assertEquals(expectedResultProperty, listProperty);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 * Tests whether the correct converter is used when there are two applicable ones with different priorities.
	 * Also tests whether the correct result is returned.
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testGetListPropertyTwoApplicable() throws DatabindingFailedException {
		final VDomainModelReference reference = mock(VFeaturePathDomainModelReference.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter2 = mock(DomainModelReferenceConverter.class);
		final IListProperty expectedResultProperty = mock(IListProperty.class);

		when(converter1.isApplicable(reference)).thenReturn(5d);
		when(converter1.convertToListProperty(reference)).thenReturn(expectedResultProperty);
		when(converter2.isApplicable(reference)).thenReturn(1d);
		when(converter2.convertToListProperty(reference)).thenReturn(mock(SimpleListProperty.class));

		databindingService.addDomainModelReferenceConverter(converter1);
		databindingService.addDomainModelReferenceConverter(converter2);
		final IListProperty listProperty = databindingService.getListProperty(reference);
		assertEquals(expectedResultProperty, listProperty);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#getValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 * Tests whether the {@link EMFFormsDatabindingImpl} considers all {@link DomainModelReferenceConverter}s, that are
	 * registered to it, for its conversions.
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testGetListPropertyAllConsidered() throws DatabindingFailedException {
		final VDomainModelReference reference = mock(VDomainModelReference.class);

		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter2 = mock(DomainModelReferenceConverter.class);
		final DomainModelReferenceConverter converter3 = mock(DomainModelReferenceConverter.class);

		databindingService.addDomainModelReferenceConverter(converter1);
		databindingService.addDomainModelReferenceConverter(converter2);
		databindingService.addDomainModelReferenceConverter(converter3);

		databindingService.getListProperty(reference);

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
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetListPropertyNull() throws DatabindingFailedException {
		databindingService.getListProperty(null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.EMFFormsDatabindingImpl#removeDomainModelReferenceConverter(org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalStateException.class)
	public void testRemoveDomainModelReferenceConverter() throws DatabindingFailedException {
		final VDomainModelReference reference = mock(VFeaturePathDomainModelReference.class);
		final DomainModelReferenceConverter converter1 = mock(DomainModelReferenceConverter.class);

		when(converter1.isApplicable(reference)).thenReturn(5d);

		databindingService.addDomainModelReferenceConverter(converter1);
		databindingService.removeDomainModelReferenceConverter(converter1);
		databindingService.getValueProperty(reference);
	}
}
