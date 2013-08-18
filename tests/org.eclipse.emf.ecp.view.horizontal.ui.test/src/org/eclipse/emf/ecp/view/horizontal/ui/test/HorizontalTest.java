/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.horizontal.ui.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.test.HierarchyViewModelHandle;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonas
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class HorizontalTest {

	@Test
	public void testHorizontalWithoutChildren() {
		final HierarchyViewModelHandle horizontalHandle = createHorizontalWithoutChildren();
		final Node<Renderable> node = buildNode(horizontalHandle.getRoot());
		assertEquals(1, ViewTestHelper.countNodes(node));
		assertEquals(horizontalHandle.getRoot(), node.getRenderable());
	}

	@Test
	public void testHorizontalWithTwoControlsAsChildren() {
		final HierarchyViewModelHandle horizontalHandle = createHorizontalWithTwoControlsAsChildren();

		final Node<Renderable> node = buildNode(horizontalHandle.getRoot());
		assertEquals(3, ViewTestHelper.countNodes(node));
		assertEquals(horizontalHandle.getRoot(), node.getRenderable());
		assertEquals(horizontalHandle.getFirstChild(), node.getChildren().get(0).getRenderable());
		assertEquals(horizontalHandle.getSecondChild(), node.getChildren().get(1).getRenderable());
	}

	@Test
	public void testHorizontalWithTwoHorizontalAsChildrenAndControlAsSubChildren() {
		final HierarchyViewModelHandle horizontalHandle = createHorizontalWithTwoHorizontalAsChildrenAndControlAsSubChildren();

		final Node<Renderable> node = buildNode(horizontalHandle.getRoot());
		assertEquals(7, ViewTestHelper.countNodes(node));
		assertEquals(horizontalHandle.getRoot(), node.getRenderable());
		assertEquals(horizontalHandle.getFirstChild(), node.getChildren().get(0).getRenderable());
		assertEquals(horizontalHandle.getSecondChild(), node.getChildren().get(1).getRenderable());
		assertEquals(horizontalHandle.getFirstFirstChild(), node.getChildren().get(0).getChildren().get(0)
			.getRenderable());
		assertEquals(horizontalHandle.getFirstSecondChild(), node.getChildren().get(0).getChildren().get(1)
			.getRenderable());
		assertEquals(horizontalHandle.getSecondFirstChild(), node.getChildren().get(1).getChildren().get(0)
			.getRenderable());
		assertEquals(horizontalHandle.getSecondSecondChild(), node.getChildren().get(1).getChildren().get(1)
			.getRenderable());
	}

	public static HierarchyViewModelHandle createHorizontalWithTwoHorizontalAsChildrenAndControlAsSubChildren() {
		final HierarchyViewModelHandle horizontalHandle = createHorizontalWithoutChildren();
		horizontalHandle.addFirstChildToRoot(createHorizontal());
		horizontalHandle.addSecondChildToRoot(createHorizontal());
		horizontalHandle.addFirstChildToFirstChild(createControl());
		horizontalHandle.addSecondChildToFirstChild(createControl());
		horizontalHandle.addFirstChildToSecondChild(createControl());
		horizontalHandle.addSecondChildToSecondChild(createControl());
		return horizontalHandle;
	}

	public static HierarchyViewModelHandle createHorizontalWithTwoControlsAsChildren() {
		final HierarchyViewModelHandle horizontalHandle = createHorizontalWithoutChildren();
		final Control control1 = createControl();
		horizontalHandle.addFirstChildToRoot(control1);
		final Control control2 = createControl();
		horizontalHandle.addSecondChildToRoot(control2);
		return horizontalHandle;
	}

	private static Control createControl() {
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(EcorePackage.eINSTANCE.getEClassifier_InstanceClassName());
		return control;
	}

	public static HierarchyViewModelHandle createHorizontalWithoutChildren() {
		final Renderable horizontal = createHorizontal();
		return new HierarchyViewModelHandle(horizontal);
	}

	public static Renderable createHorizontal() {
		return ViewFactory.eINSTANCE.createColumnComposite();
	}

	private Node<Renderable> buildNode(final Renderable renderable) {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		return ViewTestHelper.build(renderable, eClass);
	}

}
