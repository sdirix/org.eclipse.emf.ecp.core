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
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
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
		final HorizontalHandle horizontalHandle = createHorizontalWithoutChildren();
		final Node<Renderable> node = buildNode(horizontalHandle.getHorizontal());
		assertEquals(1, ViewTestHelper.countNodes(node));
		assertEquals(horizontalHandle.getHorizontal(), node.getRenderable());
	}

	@Test
	public void testHorizontalWithTwoControlsAsChildren() {
		final HorizontalHandle horizontalHandle = createHorizontalWithTwoControlsAsChildren();

		final Node<Renderable> node = buildNode(horizontalHandle.getHorizontal());
		assertEquals(3, ViewTestHelper.countNodes(node));
		assertEquals(horizontalHandle.getHorizontal(), node.getRenderable());
		assertEquals(horizontalHandle.getFirstChild(), node.getChildren().get(0).getRenderable());
		assertEquals(horizontalHandle.getSecondChild(), node.getChildren().get(1).getRenderable());
	}

	@Test
	public void testHorizontalWithTwoHorizontalAsChildrenAndControlAsSubChildren() {
		// TODO: Implement
		final HorizontalHandle horizontalHandle = createHorizontalWithTwoHorizontalAsChildrenAndControlAsSubChildren();

		final Node<Renderable> node = buildNode(horizontalHandle.getHorizontal());
		assertEquals(3, ViewTestHelper.countNodes(node));
		assertEquals(horizontalHandle.getHorizontal(), node.getRenderable());
		assertEquals(horizontalHandle.getFirstChild(), node.getChildren().get(0).getRenderable());
		assertEquals(horizontalHandle.getSecondChild(), node.getChildren().get(1).getRenderable());
	}

	/**
	 * @return
	 */
	private HorizontalHandle createHorizontalWithTwoHorizontalAsChildrenAndControlAsSubChildren() {
		// final HorizontalHandle horizontalHandle = createHorizontalWithTwoControlsAsChildren();
		// horizontalHandle.addFirstToFirstChild()
		return null;
	}

	/**
	 * @return
	 */
	private HorizontalHandle createHorizontalWithTwoControlsAsChildren() {
		final HorizontalHandle horizontalHandle = createHorizontalWithoutChildren();
		final Control control1 = ViewFactory.eINSTANCE.createControl();
		horizontalHandle.addFirstChildToRoot(control1);
		final Control control2 = ViewFactory.eINSTANCE.createControl();
		horizontalHandle.addSecondChildToRoot(control2);
		return horizontalHandle;
	}

	/**
	 * @return
	 */
	private HorizontalHandle createHorizontalWithoutChildren() {
		final Renderable horizontal = createHorizontal();
		return new HorizontalHandle(horizontal);
	}

	private Renderable createHorizontal() {
		return ViewFactory.eINSTANCE.createColumnComposite();
	}

	private Node<Renderable> buildNode(final Renderable renderable) {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		return ViewTestHelper.build(renderable, eClass);
	}

}
