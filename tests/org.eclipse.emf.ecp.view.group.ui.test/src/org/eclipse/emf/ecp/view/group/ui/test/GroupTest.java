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
package org.eclipse.emf.ecp.view.group.ui.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonas
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class GroupTest {

	@Test
	public void testOneGroupinView() {
		// setup model
		final View view = createViewWithOneGroup();
		final Group group = (Group) view.getChildren().get(0);
		// Test NodeBuidlers
		final Node<Renderable> node = buildNode(view);
		assertEquals(2, ViewTestHelper.countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 1, node.getChildren().size());
		final Node<?> childNode = node.getChildren().get(0);
		assertEquals(group, childNode.getRenderable());
	}

	private Node<Renderable> buildNode(final View view) {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		return ViewTestHelper.build(view, eClass);
	}

	/**
	 * @return a View with one Group in it
	 */
	public static View createViewWithOneGroup() {
		final View view = ViewFactory.eINSTANCE.createView();
		final Group group = createGroup();
		view.getChildren().add(group);
		return view;
	}

	@Test
	public void testTwoGroupsinView() {
		// setup model
		final View view = createViewWithTwoGroups();
		final Group group = (Group) view.getChildren().get(0);
		final Group group2 = (Group) view.getChildren().get(1);

		// Test NodeBuidlers
		final Node<Renderable> node = buildNode(view);
		assertEquals(3, ViewTestHelper.countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 2, node.getChildren().size());
		final Node<?> childNode = node.getChildren().get(0);
		assertEquals(group, childNode.getRenderable());
		final Node<?> childNode2 = node.getChildren().get(1);
		assertEquals(group2, childNode2.getRenderable());
	}

	/**
	 * @return A view with two groups as children
	 */
	public static View createViewWithTwoGroups() {
		final View view = ViewFactory.eINSTANCE.createView();
		final Group group = createGroup();
		view.getChildren().add(group);
		final Group group2 = createGroup();
		view.getChildren().add(group2);
		return view;
	}

	@Test
	public void testTwoGroupsHierachicalinView() {
		// setup model
		final View view = createViewWithTwoHierachicalGroups();
		final Group group = (Group) view.getChildren().get(0);
		final Group subGroup = (Group) group.getComposites().get(0);

		// Test NodeBuidlers
		final Node<Renderable> node = buildNode(view);
		assertEquals(3, ViewTestHelper.countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 1, node.getChildren().size());
		final Node<?> childNode = node.getChildren().get(0);
		assertEquals(group, childNode.getRenderable());
		final Node<?> subChildNode = childNode.getChildren().get(0);
		assertEquals(subGroup, subChildNode.getRenderable());
	}

	/**
	 * @return A View with two groups, one is the subgroup of the first one
	 */
	public static View createViewWithTwoHierachicalGroups() {
		final View view = ViewFactory.eINSTANCE.createView();
		final Group group = createGroup();
		view.getChildren().add(group);
		final Group subGroup = createGroup();
		group.getComposites().add(subGroup);
		return view;
	}

	/**
	 * @return
	 */
	private static Group createGroup() {
		return ViewFactory.eINSTANCE.createGroup();
	}

}
