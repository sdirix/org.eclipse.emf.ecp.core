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
import static org.junit.Assert.assertNull;

import org.eclipse.emfforms.core.services.databinding.testmodel.test.model.B;
import org.eclipse.emfforms.core.services.databinding.testmodel.test.model.C;
import org.eclipse.emfforms.core.services.databinding.testmodel.test.model.TestFactory;
import org.eclipse.emfforms.core.services.databinding.testmodel.test.model.TestPackage;
import org.junit.Test;

/**
 * JUnit test cases for {@link EMFIndexedValueProperty}.
 * 
 * @author Lucas Koehler
 *
 */
public class EMFIndexedValueProperty_Test {
	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.EMFIndexedValueProperty#EMFIndexedValueProperty(int, org.eclipse.emf.ecore.EStructuralFeature)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEMFIndexedValuePropertyNegativeIndex() {
		new EMFIndexedValueProperty(-1, TestPackage.eINSTANCE.getB_CList());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.EMFIndexedValueProperty#doGetValue(java.lang.Object)}
	 * .
	 */
	@Test
	public void testDoGetValueNull() {
		final EMFIndexedValueProperty indexedValueProperty = new EMFIndexedValueProperty(0,
			TestPackage.eINSTANCE.getB_CList());
		final B b = TestFactory.eINSTANCE.createB();
		assertNull(indexedValueProperty.doGetValue(b));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.EMFIndexedValueProperty#doGetValue(java.lang.Object)}
	 * .
	 */
	@Test
	public void testDoGetValue() {
		final EMFIndexedValueProperty indexedValueProperty = new EMFIndexedValueProperty(1,
			TestPackage.eINSTANCE.getB_CList());
		final B b = TestFactory.eINSTANCE.createB();
		final C c1 = TestFactory.eINSTANCE.createC();
		final C c2 = TestFactory.eINSTANCE.createC();
		b.getCList().add(c1);
		b.getCList().add(c2);

		assertEquals(c2, indexedValueProperty.doGetValue(b));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.EMFIndexedValueProperty#doSetValue(java.lang.Object, java.lang.Object)}
	 * .
	 */
	@Test
	public void testDoSetValueObjectReplace() {
		final EMFIndexedValueProperty indexedValueProperty = new EMFIndexedValueProperty(1,
			TestPackage.eINSTANCE.getB_CList());
		final B b = TestFactory.eINSTANCE.createB();
		final C c1 = TestFactory.eINSTANCE.createC();
		final C c2 = TestFactory.eINSTANCE.createC();
		final C c3 = TestFactory.eINSTANCE.createC();
		b.getCList().add(c1);
		b.getCList().add(c2);
		indexedValueProperty.doSetValue(b, c3);

		assertEquals(indexedValueProperty.doGetValue(b), c3);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.EMFIndexedValueProperty#doSetValue(java.lang.Object, java.lang.Object)}
	 * .
	 */
	@Test
	public void testDoSetValueObjectAdd() {
		final EMFIndexedValueProperty indexedValueProperty = new EMFIndexedValueProperty(1,
			TestPackage.eINSTANCE.getB_CList());
		final B b = TestFactory.eINSTANCE.createB();
		final C c1 = TestFactory.eINSTANCE.createC();
		final C c2 = TestFactory.eINSTANCE.createC();
		b.getCList().add(c1);
		indexedValueProperty.doSetValue(b, c2);

		assertEquals(indexedValueProperty.doGetValue(b), c2);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.core.services.databinding.index.EMFIndexedValueProperty#doSetValue(java.lang.Object, java.lang.Object)}
	 * .
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testDoSetValueObjectIndexTooBig() {
		final EMFIndexedValueProperty indexedValueProperty = new EMFIndexedValueProperty(5,
			TestPackage.eINSTANCE.getB_CList());
		final B b = TestFactory.eINSTANCE.createB();
		final C c1 = TestFactory.eINSTANCE.createC();
		final C c2 = TestFactory.eINSTANCE.createC();
		b.getCList().add(c1);
		indexedValueProperty.doSetValue(b, c2);
	}
}
