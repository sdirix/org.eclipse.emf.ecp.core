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
package org.eclipse.emfforms.internal.core.services.databinding.mapping;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.IEMFListProperty;
import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.ecp.view.spi.mappingdmr.model.VMappingDomainModelReference;
import org.eclipse.emf.ecp.view.spi.mappingdmr.model.VMappingdmrFactory;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emfforms.core.services.databinding.testmodel.test.model.TestPackage;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for {@link MappingDomainModelReferenceConverter}.
 *
 * @author Lucas Koehler
 *
 */
public class MappingDomainModelReferenceConverter_Test {

	private MappingDomainModelReferenceConverter converter;

	/**
	 * Creates a new {@link MappingDomainModelReferenceConverter} for every test case
	 */
	@Before
	public void setUp() {
		converter = new MappingDomainModelReferenceConverter();
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 */
	@Test
	public void testIsApplicable() {
		assertEquals(10d, converter.isApplicable(mock(VMappingDomainModelReference.class)), 0d);

		assertEquals(DomainModelReferenceConverter.NOT_APPLICABLE,
			converter.isApplicable(mock(VDomainModelReference.class)), 0d);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testConvertToValueProperty() throws DatabindingFailedException {
		final VMappingDomainModelReference mappingReference = VMappingdmrFactory.eINSTANCE
			.createMappingDomainModelReference();
		mappingReference.setDomainModelEFeature(TestPackage.eINSTANCE.getC_EClassToA());
		mappingReference.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getB_C());
		mappingReference.setMappedClass(TestPackage.eINSTANCE.getD());

		final VFeaturePathDomainModelReference targetReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetReference.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getA_B());
		targetReference.setDomainModelEFeature(TestPackage.eINSTANCE.getB_C());
		mappingReference.setDomainModelReference(targetReference);

		IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getA_B());
		targetValueProperty = targetValueProperty.value(TestPackage.eINSTANCE.getB_C());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getValueProperty(targetReference)).thenReturn(targetValueProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		final IValueProperty resultProperty = converter.convertToValueProperty(mappingReference);

		final String expected = "B.c<C> => C.eClassToA<EClassToAMap> mapping D => A.b<B> => B.c<C>"; //$NON-NLS-1$
		assertEquals(expected, resultProperty.toString());
		verify(emfFormsDatabinding).getValueProperty(targetReference);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testConvertToValuePropertyNoReferencePaths() throws DatabindingFailedException {
		final VMappingDomainModelReference mappingReference = VMappingdmrFactory.eINSTANCE
			.createMappingDomainModelReference();
		mappingReference.setDomainModelEFeature(TestPackage.eINSTANCE.getC_EClassToA());
		mappingReference.setMappedClass(TestPackage.eINSTANCE.getD());

		final VFeaturePathDomainModelReference targetReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetReference.setDomainModelEFeature(TestPackage.eINSTANCE.getA_B());
		mappingReference.setDomainModelReference(targetReference);

		final IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getA_B());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getValueProperty(targetReference)).thenReturn(targetValueProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		final IValueProperty resultProperty = converter.convertToValueProperty(mappingReference);

		final String expected = "C.eClassToA<EClassToAMap> mapping D => A.b<B>"; //$NON-NLS-1$
		assertEquals(expected, resultProperty.toString());
		verify(emfFormsDatabinding).getValueProperty(targetReference);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalMapTypeException.class)
	public void testConvertToValuePropertyWrongMapType() throws DatabindingFailedException {
		final VMappingDomainModelReference mappingReference = VMappingdmrFactory.eINSTANCE
			.createMappingDomainModelReference();
		mappingReference.setDomainModelEFeature(TestPackage.eINSTANCE.getC_EClassToString());
		mappingReference.setMappedClass(TestPackage.eINSTANCE.getD());

		final VFeaturePathDomainModelReference targetReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetReference.setDomainModelEFeature(TestPackage.eINSTANCE.getA_B());
		mappingReference.setDomainModelReference(targetReference);

		final IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getA_B());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getValueProperty(targetReference)).thenReturn(targetValueProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		converter.convertToValueProperty(mappingReference);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToValuePropertyWrongReference() throws DatabindingFailedException {
		converter.convertToValueProperty(mock(VDomainModelReference.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = DatabindingFailedException.class)
	public void testConvertToValuePropertyNoFeature() throws DatabindingFailedException {
		final VMappingDomainModelReference mappingReference = VMappingdmrFactory.eINSTANCE
			.createMappingDomainModelReference();
		converter.convertToValueProperty(mappingReference);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testConvertToListProperty() throws DatabindingFailedException {
		final VMappingDomainModelReference mappingReference = VMappingdmrFactory.eINSTANCE
			.createMappingDomainModelReference();
		mappingReference.setDomainModelEFeature(TestPackage.eINSTANCE.getC_EClassToA());
		mappingReference.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getB_C());
		mappingReference.setMappedClass(TestPackage.eINSTANCE.getD());

		final VFeaturePathDomainModelReference targetReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetReference.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getA_B());
		targetReference.setDomainModelEFeature(TestPackage.eINSTANCE.getB_C());
		mappingReference.setDomainModelReference(targetReference);

		final IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getA_B());
		final IEMFListProperty targetListProperty = targetValueProperty.list(TestPackage.eINSTANCE.getB_CList());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getListProperty(targetReference)).thenReturn(targetListProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		final IListProperty resultProperty = converter.convertToListProperty(mappingReference);

		final String expected = "B.c<C> => C.eClassToA<EClassToAMap> mapping D => A.b<B> => B.cList[]<C>"; //$NON-NLS-1$
		assertEquals(expected, resultProperty.toString());
		verify(emfFormsDatabinding).getListProperty(targetReference);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test
	public void testConvertToListPropertyNoReferencePath() throws DatabindingFailedException {
		final VMappingDomainModelReference mappingReference = VMappingdmrFactory.eINSTANCE
			.createMappingDomainModelReference();
		mappingReference.setDomainModelEFeature(TestPackage.eINSTANCE.getC_EClassToA());
		mappingReference.setMappedClass(TestPackage.eINSTANCE.getD());

		final VFeaturePathDomainModelReference targetReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetReference.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getA_B());
		targetReference.setDomainModelEFeature(TestPackage.eINSTANCE.getB_C());
		mappingReference.setDomainModelReference(targetReference);

		final IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getA_B());
		final IEMFListProperty targetListProperty = targetValueProperty.list(TestPackage.eINSTANCE.getB_CList());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getListProperty(targetReference)).thenReturn(targetListProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		final IListProperty resultProperty = converter.convertToListProperty(mappingReference);

		final String expected = "C.eClassToA<EClassToAMap> mapping D => A.b<B> => B.cList[]<C>"; //$NON-NLS-1$
		assertEquals(expected, resultProperty.toString());
		verify(emfFormsDatabinding).getListProperty(targetReference);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalMapTypeException.class)
	public void testConvertToListPropertyWrongMapType() throws DatabindingFailedException {
		final VMappingDomainModelReference mappingReference = VMappingdmrFactory.eINSTANCE
			.createMappingDomainModelReference();
		mappingReference.setDomainModelEFeature(TestPackage.eINSTANCE.getC_EClassToString());
		mappingReference.setMappedClass(TestPackage.eINSTANCE.getD());

		final VFeaturePathDomainModelReference targetReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		targetReference.getDomainModelEReferencePath().add(TestPackage.eINSTANCE.getA_B());
		targetReference.setDomainModelEFeature(TestPackage.eINSTANCE.getB_C());
		mappingReference.setDomainModelReference(targetReference);

		final IEMFValueProperty targetValueProperty = EMFProperties.value(TestPackage.eINSTANCE.getA_B());
		final IEMFListProperty targetListProperty = targetValueProperty.list(TestPackage.eINSTANCE.getB_CList());
		final EMFFormsDatabinding emfFormsDatabinding = mock(EMFFormsDatabinding.class);
		when(emfFormsDatabinding.getListProperty(targetReference)).thenReturn(targetListProperty);
		converter.setEMFFormsDatabinding(emfFormsDatabinding);

		converter.convertToListProperty(mappingReference);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConvertToListPropertyWrongReference() throws DatabindingFailedException {
		converter.convertToListProperty(mock(VDomainModelReference.class));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.core.services.databinding.mapping.MappingDomainModelReferenceConverter#convertToListProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)}
	 * .
	 *
	 * @throws DatabindingFailedException if the databinding failed
	 */
	@Test(expected = DatabindingFailedException.class)
	public void testConvertToListPropertyNoFeature() throws DatabindingFailedException {
		final VMappingDomainModelReference mappingReference = VMappingdmrFactory.eINSTANCE
			.createMappingDomainModelReference();
		converter.convertToListProperty(mappingReference);
	}
}
