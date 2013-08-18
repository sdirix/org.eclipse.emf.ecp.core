/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.view.vertical.ui.swt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.ui.view.test.HierarchyViewModelHandle;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;
import org.eclipse.emf.ecp.view.vertical.ui.test.VerticalTest;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DatabindingClassRunner.class)
public class SWTVerticalTest {

	private Shell shell;
	private EObject domainElement;

	@Before
	public void init() {
		shell = SWTViewTestHelper.createShell();
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.getESuperTypes().add(EcorePackage.eINSTANCE.getEClass());
		eClass.setInstanceClassName("Test");
		domainElement = eClass;
	}

	@Test
	public void testVerticalWithoutChildren() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final HierarchyViewModelHandle handle = VerticalTest.createVerticalWithoutChildren();
		final Control render = SWTViewTestHelper.render(handle.getRoot(), domainElement, shell);
		assertTrue(render instanceof Composite);
		final Composite composite = (Composite) render;
		assertEquals(0, composite.getChildren().length);
	}

	@Test
	public void testVerticalWithTwoControlsAsChildren() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final HierarchyViewModelHandle handle = VerticalTest.createVerticalWithTwoControlsAsChildren();
		final Control render = SWTViewTestHelper.render(handle.getRoot(), domainElement, shell);
		assertTrue(render instanceof Composite);
		final Composite composite = (Composite) render;
		assertEquals(4, composite.getChildren().length);
		assertEquals(2, SWTViewTestHelper.getNumberofColumns(composite));
		assertTrue(SWTViewTestHelper.checkIfThereIsATextControl(composite.getChildren()[1]));
		assertTrue(SWTViewTestHelper.checkIfThereIsATextControl(composite.getChildren()[3]));
	}

	@Test
	public void testVerticalWithTwoVerticalAsChildrenAndControlAsSubChildren() throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		// setup model
		final HierarchyViewModelHandle handle = VerticalTest
			.createVerticalWithTwoVerticalAsChildrenAndControlAsSubChildren();
		final Control render = SWTViewTestHelper.render(handle.getRoot(), domainElement, shell);
		assertTrue(render instanceof Composite);
		final Composite composite = (Composite) render;
		assertEquals(2, composite.getChildren().length);
		final Composite firstVertical = (Composite) composite.getChildren()[0];
		final Composite secondVertical = (Composite) composite.getChildren()[1];

		assertEquals(4, firstVertical.getChildren().length);
		assertEquals(4, secondVertical.getChildren().length);
		assertEquals(2, SWTViewTestHelper.getNumberofColumns(firstVertical));
		assertEquals(2, SWTViewTestHelper.getNumberofColumns(secondVertical));

		assertTrue(SWTViewTestHelper.checkIfThereIsATextControl(firstVertical.getChildren()[0]));
		assertTrue(SWTViewTestHelper.checkIfThereIsATextControl(secondVertical.getChildren()[0]));
		assertTrue(SWTViewTestHelper.checkIfThereIsATextControl(firstVertical.getChildren()[1]));
		assertTrue(SWTViewTestHelper.checkIfThereIsATextControl(secondVertical.getChildren()[1]));
	}
}
