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
package org.eclipse.emf.ecp.ui.view.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.junit.Test;

/**
 * @author Jonas
 * 
 */
public class ViewTest {

	@Test
	public void testEmptyView() {
		// setup model
		final View view = ViewFactory.eINSTANCE.createView();
		// Test NodeBuidlers
		final Node<Renderable> node = build(view);
		assertEquals(1, countNodes(node));
		assertEquals("Unknown Node has been instanciated", 0, node.getChildren().size());
	}

	@Test
	public void testViewWithCategories() {
		final View view = ViewFactory.eINSTANCE.createView();
		final Category category = ViewFactory.eINSTANCE.createCategory();
		view.getCategorizations().add(category);
		final Node<Renderable> node = build(view);
		assertEquals(2, countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 1, node.getChildren().size());
		final Node<?> childNode = node.getChildren().get(0);
		assertEquals(category, childNode.getRenderable());
	}

	/**
	 * @param node
	 * @return
	 */
	private int countNodes(Node<?> node) {
		return ViewTestHelper.countNodes(node);
	}

	@Test
	public void testViewWithChildren() {
		final View view = ViewFactory.eINSTANCE.createView();
		final Column control = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(control);
		final Node<Renderable> node = build(view);
		assertEquals(2, countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 1, node.getChildren().size());
		final Node<?> childNode = node.getChildren().get(0);
		assertEquals(control, childNode.getRenderable());
	}

	@Test
	public void testViewWithChildrenAndCategories() {
		final View view = ViewFactory.eINSTANCE.createView();
		final Column control = ViewFactory.eINSTANCE.createColumn();
		final Category category = ViewFactory.eINSTANCE.createCategory();
		view.getCategorizations().add(category);
		view.getChildren().add(control);
		final Node<Renderable> node = build(view);
		assertEquals(2, countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 1, node.getChildren().size());
		final Node<?> childNode = node.getChildren().get(0);
		assertEquals(category, childNode.getRenderable());
	}

	/**
	 * @param view
	 * @return
	 */
	private Node<Renderable> build(View view) {
		return ViewTestHelper.build(view, null);
	}

}
