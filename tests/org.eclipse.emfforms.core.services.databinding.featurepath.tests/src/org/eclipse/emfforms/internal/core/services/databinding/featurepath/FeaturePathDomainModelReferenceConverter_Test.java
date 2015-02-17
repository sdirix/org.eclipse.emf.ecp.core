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
package org.eclipse.emfforms.internal.core.services.databinding.featurepath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;

import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.IEMFListProperty;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter;
import org.eclipse.emfforms.core.services.databinding.testmodel.test.model.TestPackage;
import org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for {@link FeaturePathDomainModelReferenceConverter}.
 *
 * @author Lucas Koehler
 *
 */
public class FeaturePathDomainModelReferenceConverter_Test {

	private FeaturePathDomainModelReferenceConverter converter;

	/**
	 * Set up that is executed before every test.
	 */
	@Before
	public void setUp() {
		converter = new FeaturePathDomainModelReferenceConverter();
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test
	public void testIsApplicable() {
		// The FeaturePathDomainModelReferenceConverter is the standard converter for VFeaturePathDomainModelReference
		// with a low priority.
		assertEquals(0.0, converter.isApplicable(mock(VFeaturePathDomainModelReference.class)), 0d);

		// The FeaturePathDomainModelReferenceConverter is not applicable other references than
		// VFeaturePathDomainModelReferences
		assertEquals(DomainModelReferenceConverter.NOT_APPLICABLE,
			converter.isApplicable(mock(VDomainModelReference.class)), 0d);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testIsApplicableNull() {
		converter.isApplicable(null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test
	public void testConvertToValueProperty() {
		final VFeaturePathDomainModelReference pathReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		// create reference path to the attribute
		final LinkedList<EReference> referencePath = new LinkedList<EReference>();
		referencePath.add(TestPackage.eINSTANCE.getA_B());
		referencePath.add(TestPackage.eINSTANCE.getB_C());
		referencePath.add(TestPackage.eINSTANCE.getC_D());

		final EStructuralFeature feature = TestPackage.eINSTANCE.getD_X();

		pathReference.getDomainModelEReferencePath().addAll(referencePath);
		pathReference.setDomainModelEFeature(feature);

		final IValueProperty valueProperty = converter.convertToValueProperty(pathReference);

		// The converter should return an IEMFValueProperty
		assertTrue(valueProperty instanceof IEMFValueProperty);

		final IEMFValueProperty emfProperty = (IEMFValueProperty) valueProperty;

		// Check EStructuralFeature of the property.
		assertEquals(feature, emfProperty.getStructuralFeature());

		// Check correct path.
		final String expected = "A.b<B> => B.c<C> => C.d<D> => D.x<EString>"; //$NON-NLS-1$
		assertEquals(expected, emfProperty.toString());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test
	public void testConvertToValuePropertyNoReferencePath() {
		final VFeaturePathDomainModelReference pathReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();

		final EStructuralFeature feature = TestPackage.eINSTANCE.getD_X();
		pathReference.setDomainModelEFeature(feature);

		final IValueProperty valueProperty = converter.convertToValueProperty(pathReference);

		// The converter should return an IEMFValueProperty
		assertTrue(valueProperty instanceof IEMFValueProperty);

		final IEMFValueProperty emfProperty = (IEMFValueProperty) valueProperty;

		// Check EStructuralFeature of the property.
		assertEquals(feature, emfProperty.getStructuralFeature());

		// Check correct path.
		final String expected = "D.x<EString>"; //$NON-NLS-1$
		assertEquals(expected, emfProperty.toString());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToValuePropertyNull() {
		converter.convertToValueProperty(null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToValuePropertyWrongReferenceType() {
		converter.convertToValueProperty(mock(VDomainModelReference.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test
	public void testConvertToListProperty() {
		final VFeaturePathDomainModelReference pathReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		// create reference path to the list
		final LinkedList<EReference> referencePath = new LinkedList<EReference>();
		referencePath.add(TestPackage.eINSTANCE.getA_B());
		referencePath.add(TestPackage.eINSTANCE.getB_C());
		referencePath.add(TestPackage.eINSTANCE.getC_D());

		final EStructuralFeature feature = TestPackage.eINSTANCE.getD_YList();

		pathReference.getDomainModelEReferencePath().addAll(referencePath);
		pathReference.setDomainModelEFeature(feature);

		final IListProperty listProperty = converter.convertToListProperty(pathReference);

		// The converter should return an IEMFListProperty
		assertTrue(listProperty instanceof IEMFListProperty);

		final IEMFListProperty emfProperty = (IEMFListProperty) listProperty;

		// Check EStructuralFeature of the property.
		assertEquals(feature, emfProperty.getStructuralFeature());

		// Check correct path.
		final String expected = "A.b<B> => B.c<C> => C.d<D> => D.yList[]<EInt>"; //$NON-NLS-1$
		assertEquals(expected, emfProperty.toString());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test
	public void testConvertToListPropertyNoReferencePath() {
		final VFeaturePathDomainModelReference pathReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();

		final EStructuralFeature feature = TestPackage.eINSTANCE.getD_YList();
		pathReference.setDomainModelEFeature(feature);

		final IListProperty listProperty = converter.convertToListProperty(pathReference);

		// The converter should return an IEMFListProperty
		assertTrue(listProperty instanceof IEMFListProperty);

		final IEMFListProperty emfProperty = (IEMFListProperty) listProperty;

		// Check EStructuralFeature of the property.
		assertEquals(feature, emfProperty.getStructuralFeature());

		// Check correct path.
		final String expected = "D.yList[]<EInt>"; //$NON-NLS-1$
		assertEquals(expected, emfProperty.toString());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToListPropertyNull() {
		converter.convertToListProperty(null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.featurepath.FeaturePathDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToListPropertyWrongReferenceType() {
		converter.convertToListProperty(mock(VDomainModelReference.class));
	}
}
