/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.categorization.swt.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.ecp.view.internal.categorization.swt.CategorizationTabRendererTester;
import org.eclipse.emf.ecp.view.internal.categorization.swt.CategorizationTreeRendererTester;
import org.eclipse.emf.ecp.view.internal.categorization.swt.CompositeCategoryDefaultRendererTester;
import org.eclipse.emf.ecp.view.internal.categorization.swt.CompositeCategoryTabRendererTester;
import org.eclipse.emf.ecp.view.internal.categorization.swt.CompositeCategoryTreeRendererTester;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.categorization.model.VCategorization;
import org.eclipse.emf.ecp.view.spi.categorization.model.VCategorizationElement;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eugen
 * 
 */
@SuppressWarnings("restriction")
public class CategorizationCorrectTesterTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testCorrectTesterForCategorizationWithDepth0() {
		final VCategorizationElement categorizationElement = mock(VCategorizationElement.class);
		when(categorizationElement.getMainCategoryDepth()).thenReturn(0);
		final CategorizationTabRendererTester tabTester = new CategorizationTabRendererTester();
		final int applicable = tabTester.isApplicable(categorizationElement, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicable);

		final CategorizationTreeRendererTester treeTester = new CategorizationTreeRendererTester();
		final int applicableTree = treeTester.isApplicable(categorizationElement, mock(ViewModelContext.class));
		assertEquals(1, applicableTree);
	}

	@Test
	public void testCorrectTesterForCategorizationWithDepth1() {
		final VCategorizationElement categorizationElement = mock(VCategorizationElement.class);
		when(categorizationElement.getMainCategoryDepth()).thenReturn(1);
		final CategorizationTabRendererTester tabTester = new CategorizationTabRendererTester();
		final int applicable = tabTester.isApplicable(categorizationElement, mock(ViewModelContext.class));
		assertEquals(1, applicable);

		final CategorizationTreeRendererTester treeTester = new CategorizationTreeRendererTester();
		final int applicableTree = treeTester.isApplicable(categorizationElement, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree);
	}

	@Test
	public void testCorrectTesterForCategorizationWithDepth2() {
		final VCategorizationElement categorizationElement = mock(VCategorizationElement.class);
		when(categorizationElement.getMainCategoryDepth()).thenReturn(2);
		final CategorizationTabRendererTester tabTester = new CategorizationTabRendererTester();
		final int applicable = tabTester.isApplicable(categorizationElement, mock(ViewModelContext.class));
		assertEquals(1, applicable);

		final CategorizationTreeRendererTester treeTester = new CategorizationTreeRendererTester();
		final int applicableTree = treeTester.isApplicable(categorizationElement, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree);
	}

	@Test
	public void testCorrectTesterForCompositeCategoryWithDepth0() {
		final VCategorizationElement categorizationElement = mock(VCategorizationElement.class);
		when(categorizationElement.getMainCategoryDepth()).thenReturn(0);
		final VCategorization categorization0 = mock(VCategorization.class);
		when(categorization0.eContainer()).thenReturn(categorizationElement);
		final VCategorization categorization1 = mock(VCategorization.class);
		when(categorization1.eContainer()).thenReturn(categorization0);
		final VCategorization categorization2 = mock(VCategorization.class);
		when(categorization2.eContainer()).thenReturn(categorization1);
		final VCategorization categorization3 = mock(VCategorization.class);
		when(categorization3.eContainer()).thenReturn(categorization2);

		final CompositeCategoryDefaultRendererTester defaultTester = new CompositeCategoryDefaultRendererTester();
		final CompositeCategoryTabRendererTester tabTester = new CompositeCategoryTabRendererTester();
		final CompositeCategoryTreeRendererTester treeTester = new CompositeCategoryTreeRendererTester();

		final int applicable0 = defaultTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(1, applicable0);

		final int applicableTab0 = tabTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab0);

		final int applicableTree0 = treeTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree0);

		final int applicable1 = defaultTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(1, applicable1);

		final int applicableTab1 = tabTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab1);

		final int applicableTree1 = treeTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree1);

		final int applicable2 = defaultTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(1, applicable2);

		final int applicableTab2 = tabTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab2);

		final int applicableTree2 = treeTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree2);

		final int applicable3 = defaultTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(1, applicable3);

		final int applicableTab3 = tabTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab3);

		final int applicableTree3 = treeTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree3);
	}

	@Test
	public void testCorrectTesterForCompositeCategoryWithDepth1() {
		final VCategorizationElement categorizationElement = mock(VCategorizationElement.class);
		when(categorizationElement.getMainCategoryDepth()).thenReturn(1);
		final VCategorization categorization0 = mock(VCategorization.class);
		when(categorization0.eContainer()).thenReturn(categorizationElement);
		final VCategorization categorization1 = mock(VCategorization.class);
		when(categorization1.eContainer()).thenReturn(categorization0);
		final VCategorization categorization2 = mock(VCategorization.class);
		when(categorization2.eContainer()).thenReturn(categorization1);
		final VCategorization categorization3 = mock(VCategorization.class);
		when(categorization3.eContainer()).thenReturn(categorization2);

		final CompositeCategoryDefaultRendererTester defaultTester = new CompositeCategoryDefaultRendererTester();
		final CompositeCategoryTabRendererTester tabTester = new CompositeCategoryTabRendererTester();
		final CompositeCategoryTreeRendererTester treeTester = new CompositeCategoryTreeRendererTester();

		final int applicable0 = defaultTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicable0);

		final int applicableTab0 = tabTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab0);

		final int applicableTree0 = treeTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(1, applicableTree0);

		final int applicable1 = defaultTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(1, applicable1);

		final int applicableTab1 = tabTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab1);

		final int applicableTree1 = treeTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree1);

		final int applicable2 = defaultTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(1, applicable2);

		final int applicableTab2 = tabTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab2);

		final int applicableTree2 = treeTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree2);

		final int applicable3 = defaultTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(1, applicable3);

		final int applicableTab3 = tabTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab3);

		final int applicableTree3 = treeTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree3);
	}

	@Test
	public void testCorrectTesterForCompositeCategoryWithDepth2() {
		final VCategorizationElement categorizationElement = mock(VCategorizationElement.class);
		when(categorizationElement.getMainCategoryDepth()).thenReturn(2);
		final VCategorization categorization0 = mock(VCategorization.class);
		when(categorization0.eContainer()).thenReturn(categorizationElement);
		final VCategorization categorization1 = mock(VCategorization.class);
		when(categorization1.eContainer()).thenReturn(categorization0);
		final VCategorization categorization2 = mock(VCategorization.class);
		when(categorization2.eContainer()).thenReturn(categorization1);
		final VCategorization categorization3 = mock(VCategorization.class);
		when(categorization3.eContainer()).thenReturn(categorization2);

		final CompositeCategoryDefaultRendererTester defaultTester = new CompositeCategoryDefaultRendererTester();
		final CompositeCategoryTabRendererTester tabTester = new CompositeCategoryTabRendererTester();
		final CompositeCategoryTreeRendererTester treeTester = new CompositeCategoryTreeRendererTester();

		final int applicable0 = defaultTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicable0);

		final int applicableTab0 = tabTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(1, applicableTab0);

		final int applicableTree0 = treeTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree0);

		final int applicable1 = defaultTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicable1);

		final int applicableTab1 = tabTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab1);

		final int applicableTree1 = treeTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(1, applicableTree1);

		final int applicable2 = defaultTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(1, applicable2);

		final int applicableTab2 = tabTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab2);

		final int applicableTree2 = treeTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree2);

		final int applicable3 = defaultTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(1, applicable3);

		final int applicableTab3 = tabTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab3);

		final int applicableTree3 = treeTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree3);
	}

	@Test
	public void testCorrectTesterForCompositeCategoryWithDepth3() {
		final VCategorizationElement categorizationElement = mock(VCategorizationElement.class);
		when(categorizationElement.getMainCategoryDepth()).thenReturn(3);
		final VCategorization categorization0 = mock(VCategorization.class);
		when(categorization0.eContainer()).thenReturn(categorizationElement);
		final VCategorization categorization1 = mock(VCategorization.class);
		when(categorization1.eContainer()).thenReturn(categorization0);
		final VCategorization categorization2 = mock(VCategorization.class);
		when(categorization2.eContainer()).thenReturn(categorization1);
		final VCategorization categorization3 = mock(VCategorization.class);
		when(categorization3.eContainer()).thenReturn(categorization2);

		final CompositeCategoryDefaultRendererTester defaultTester = new CompositeCategoryDefaultRendererTester();
		final CompositeCategoryTabRendererTester tabTester = new CompositeCategoryTabRendererTester();
		final CompositeCategoryTreeRendererTester treeTester = new CompositeCategoryTreeRendererTester();

		final int applicable0 = defaultTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicable0);

		final int applicableTab0 = tabTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(1, applicableTab0);

		final int applicableTree0 = treeTester.isApplicable(categorization0, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree0);

		final int applicable1 = defaultTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicable1);

		final int applicableTab1 = tabTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(1, applicableTab1);

		final int applicableTree1 = treeTester.isApplicable(categorization1, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree1);

		final int applicable2 = defaultTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicable2);

		final int applicableTab2 = tabTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab2);

		final int applicableTree2 = treeTester.isApplicable(categorization2, mock(ViewModelContext.class));
		assertEquals(1, applicableTree2);

		final int applicable3 = defaultTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(1, applicable3);

		final int applicableTab3 = tabTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTab3);

		final int applicableTree3 = treeTester.isApplicable(categorization3, mock(ViewModelContext.class));
		assertEquals(ECPRendererTester.NOT_APPLICABLE, applicableTree3);
	}
}
