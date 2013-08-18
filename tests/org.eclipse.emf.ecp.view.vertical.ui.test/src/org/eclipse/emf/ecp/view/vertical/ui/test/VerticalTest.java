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
package org.eclipse.emf.ecp.view.vertical.ui.test;

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
public class VerticalTest {

	@Test
	public void testVerticalWithoutChildren() {
		final HierarchyViewModelHandle verticalHandle = createVerticalWithoutChildren();
		final Node<Renderable> node = buildNode(verticalHandle.getRoot());
		assertEquals(1, ViewTestHelper.countNodes(node));
		assertEquals(verticalHandle.getRoot(), node.getRenderable());
	}

	@Test
	public void testVerticalWithTwoControlsAsChildren() {
		final HierarchyViewModelHandle verticalHandle = createVerticalWithTwoControlsAsChildren();

		final Node<Renderable> node = buildNode(verticalHandle.getRoot());
		assertEquals(3, ViewTestHelper.countNodes(node));
		assertEquals(verticalHandle.getRoot(), node.getRenderable());
		assertEquals(verticalHandle.getFirstChild(), node.getChildren().get(0).getRenderable());
		assertEquals(verticalHandle.getSecondChild(), node.getChildren().get(1).getRenderable());
	}

	@Test
	public void testVerticalWithTwoVerticalAsChildrenAndControlAsSubChildren() {
		final HierarchyViewModelHandle verticalHandle = createVerticalWithTwoVerticalAsChildrenAndControlAsSubChildren();

		final Node<Renderable> node = buildNode(verticalHandle.getRoot());
		assertEquals(7, ViewTestHelper.countNodes(node));
		assertEquals(verticalHandle.getRoot(), node.getRenderable());
		assertEquals(verticalHandle.getFirstChild(), node.getChildren().get(0).getRenderable());
		assertEquals(verticalHandle.getSecondChild(), node.getChildren().get(1).getRenderable());
		assertEquals(verticalHandle.getFirstFirstChild(), node.getChildren().get(0).getChildren().get(0)
			.getRenderable());
		assertEquals(verticalHandle.getFirstSecondChild(), node.getChildren().get(0).getChildren().get(1)
			.getRenderable());
		assertEquals(verticalHandle.getSecondFirstChild(), node.getChildren().get(1).getChildren().get(0)
			.getRenderable());
		assertEquals(verticalHandle.getSecondSecondChild(), node.getChildren().get(1).getChildren().get(1)
			.getRenderable());
	}

	public static HierarchyViewModelHandle createVerticalWithTwoVerticalAsChildrenAndControlAsSubChildren() {
		final HierarchyViewModelHandle verticalHandle = createVerticalWithoutChildren();
		verticalHandle.addFirstChildToRoot(createVertical());
		verticalHandle.addSecondChildToRoot(createVertical());
		verticalHandle.addFirstChildToFirstChild(createControl());
		verticalHandle.addSecondChildToFirstChild(createControl());
		verticalHandle.addFirstChildToSecondChild(createControl());
		verticalHandle.addSecondChildToSecondChild(createControl());
		return verticalHandle;
	}

	public static HierarchyViewModelHandle createVerticalWithTwoControlsAsChildren() {
		final HierarchyViewModelHandle verticalHandle = createVerticalWithoutChildren();
		final Control control1 = createControl();
		verticalHandle.addFirstChildToRoot(control1);
		final Control control2 = createControl();
		verticalHandle.addSecondChildToRoot(control2);
		return verticalHandle;
	}

	private static Control createControl() {
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(EcorePackage.eINSTANCE.getEClassifier_InstanceClassName());
		return control;
	}

	public static HierarchyViewModelHandle createVerticalWithoutChildren() {
		final Renderable vertical = createVertical();
		return new HierarchyViewModelHandle(vertical);
	}

	public static Renderable createVertical() {
		return ViewFactory.eINSTANCE.createColumn();
	}

	private Node<Renderable> buildNode(final Renderable renderable) {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		return ViewTestHelper.build(renderable, eClass);
	}

}
