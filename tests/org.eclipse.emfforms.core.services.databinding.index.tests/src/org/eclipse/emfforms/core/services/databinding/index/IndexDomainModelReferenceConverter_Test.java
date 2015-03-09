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
package org.eclipse.emfforms.core.services.databinding.index;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.IEMFListProperty;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.ecp.view.spi.indexdmr.model.VIndexDomainModelReference;
import org.eclipse.emf.ecp.view.spi.indexdmr.model.VIndexdmrFactory;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emfforms.core.services.databinding.testmodel.test.model.TestPackage;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for {@link IndexDomainModelReferenceConverter}.
 *
 * @author Lucas Koehler
 *
 */
public class IndexDomainModelReferenceConverter_Test {

	private IndexDomainModelReferenceConverter converter;

	/**
	 * Set up executed before every test case.
	 */
	@Before
	public void setUp() {
		converter = new IndexDomainModelReferenceConverter();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test
	public void testIsApplicable() {
		assertEquals(10d, converter.isApplicable(mock(VIndexDomainModelReference.class)), 0d);

		// The IndexDomainModelReferenceConverter is not applicable other references than
		// IndexDomainModelReferences
		assertEquals(DomainModelReferenceConverter.NOT_APPLICABLE,
			converter.isApplicable(mock(VDomainModelReference.class)), 0d);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testConvertToValueProperty() throws DatabindingFailedException {
		final VIndexDomainModelReference indexDMR = VIndexdmrFactory.eINSTANCE.createIndexDomainModelReference();
		indexDMR.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getA_B());
		indexDMR.setDomainModelEFeature(TestPackage.eINSTANCE.getB_CList());
		indexDMR.setIndex(1);

		final VFeaturePathDomainModelReference targetDMR = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetDMR.setDomainModelEFeature(TestPackage.eINSTANCE.getD_X());
		targetDMR.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getC_D());
		indexDMR.setTargetDMR(targetDMR);

		IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getC_D());
		targetValueProperty = targetValueProperty.value(TestPackage.eINSTANCE.getD_X());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getValueProperty(targetDMR)).thenReturn(targetValueProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		final IValueProperty resultProperty = converter.convertToValueProperty(indexDMR);

		final String expected = "A.b<B> => B.cList<C> index 1 => C.d<D> => D.x<EString>"; //$NON-NLS-1$
		assertEquals(expected, resultProperty.toString());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testConvertToValuePropertyNoReferencePath() throws DatabindingFailedException {
		final VIndexDomainModelReference indexDMR = VIndexdmrFactory.eINSTANCE.createIndexDomainModelReference();
		indexDMR.setDomainModelEFeature(TestPackage.eINSTANCE.getB_CList());
		indexDMR.setIndex(1);

		final VFeaturePathDomainModelReference targetDMR = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetDMR.setDomainModelEFeature(TestPackage.eINSTANCE.getD_X());
		targetDMR.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getC_D());
		indexDMR.setTargetDMR(targetDMR);

		IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getC_D());
		targetValueProperty = targetValueProperty.value(TestPackage.eINSTANCE.getD_X());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getValueProperty(targetDMR)).thenReturn(targetValueProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		final IValueProperty resultProperty = converter.convertToValueProperty(indexDMR);

		final String expected = "B.cList<C> index 1 => C.d<D> => D.x<EString>"; //$NON-NLS-1$
		assertEquals(expected, resultProperty.toString());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = DatabindingFailedException.class)
	public void testConvertToValuePropertyNoFeature() throws DatabindingFailedException {
		final VIndexDomainModelReference indexReference = VIndexdmrFactory.eINSTANCE.createIndexDomainModelReference();
		converter.convertToValueProperty(indexReference);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed (expected for this test case)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToValuePropertyNull() throws DatabindingFailedException {
		converter.convertToValueProperty(null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToValuePropertyWrongReferenceType() throws DatabindingFailedException {
		converter.convertToValueProperty(mock(VDomainModelReference.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testConvertToListProperty() throws DatabindingFailedException {
		final VIndexDomainModelReference indexDMR = VIndexdmrFactory.eINSTANCE.createIndexDomainModelReference();
		indexDMR.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getA_B());
		indexDMR.setDomainModelEFeature(TestPackage.eINSTANCE.getB_CList());
		indexDMR.setIndex(1);

		final VFeaturePathDomainModelReference targetDMR = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetDMR.setDomainModelEFeature(TestPackage.eINSTANCE.getD_YList());
		targetDMR.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getC_D());
		indexDMR.setTargetDMR(targetDMR);

		final IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getC_D());
		final IEMFListProperty targetListProperty = targetValueProperty.list(TestPackage.eINSTANCE.getD_YList());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getListProperty(targetDMR)).thenReturn(targetListProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		final IListProperty resultProperty = converter.convertToListProperty(indexDMR);

		final String expected = "A.b<B> => B.cList<C> index 1 => C.d<D> => D.yList[]<EInt>"; //$NON-NLS-1$
		assertEquals(expected, resultProperty.toString());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testConvertToListPropertyNoReferencePath() throws DatabindingFailedException {
		final VIndexDomainModelReference indexDMR = VIndexdmrFactory.eINSTANCE.createIndexDomainModelReference();
		indexDMR.setDomainModelEFeature(TestPackage.eINSTANCE.getB_CList());
		indexDMR.setIndex(1);

		final VFeaturePathDomainModelReference targetDMR = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetDMR.setDomainModelEFeature(TestPackage.eINSTANCE.getD_YList());
		targetDMR.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getC_D());
		indexDMR.setTargetDMR(targetDMR);

		final IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getC_D());
		final IEMFListProperty targetListProperty = targetValueProperty.list(TestPackage.eINSTANCE.getD_YList());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getListProperty(targetDMR)).thenReturn(targetListProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		final IListProperty resultProperty = converter.convertToListProperty(indexDMR);

		final String expected = "B.cList<C> index 1 => C.d<D> => D.yList[]<EInt>"; //$NON-NLS-1$
		assertEquals(expected, resultProperty.toString());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToListPropertyNull() throws DatabindingFailedException {
		converter.convertToListProperty(null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToListPropertyWrongReferenceType() throws DatabindingFailedException {
		converter.convertToListProperty(mock(VDomainModelReference.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.IndexDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed (expected for this test case)
	 */
	@Test(expected = DatabindingFailedException.class)
	public void testConvertToListPropertyNoFeature() throws DatabindingFailedException {
		final VIndexDomainModelReference indexReference = VIndexdmrFactory.eINSTANCE.createIndexDomainModelReference();
		converter.convertToListProperty(indexReference);
	}
}
