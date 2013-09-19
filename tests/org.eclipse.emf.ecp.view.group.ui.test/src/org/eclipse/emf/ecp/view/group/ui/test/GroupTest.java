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
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.group.model.Group;
import org.eclipse.emf.ecp.view.group.model.GroupFactory;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
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

	@Test
	public void testTwoGroupsWithTwoControlsInView() {
		// setup model
		final View view = createViewWithTwoGroupsWithTwoControls();
		final Group group1 = (Group) view.getChildren().get(0);
		final Control c11 = (Control) group1.getComposites().get(0);
		final Control c12 = (Control) group1.getComposites().get(1);
		final Group group2 = (Group) view.getChildren().get(1);
		final Control c21 = (Control) group2.getComposites().get(0);
		final Control c22 = (Control) group2.getComposites().get(1);

		// Test NodeBuidlers
		final Node<Renderable> node = buildNode(view);
		assertEquals(7, ViewTestHelper.countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 2, node.getChildren().size());
		final Node<?> childNode1 = node.getChildren().get(0);
		assertEquals(group1, childNode1.getRenderable());
		final Node<?> sub1ChildNode1 = childNode1.getChildren().get(0);
		assertEquals(c11, sub1ChildNode1.getRenderable());
		final Node<?> sub1ChildNode2 = childNode1.getChildren().get(1);
		assertEquals(c12, sub1ChildNode2.getRenderable());

		final Node<?> childNode2 = node.getChildren().get(1);
		assertEquals(group2, childNode2.getRenderable());
		final Node<?> sub2ChildNode1 = childNode2.getChildren().get(0);
		assertEquals(c21, sub2ChildNode1.getRenderable());
		final Node<?> sub2ChildNode2 = childNode2.getChildren().get(1);
		assertEquals(c22, sub2ChildNode2.getRenderable());
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

	public static View createViewWithTwoGroupsWithTwoControls() {
		final View view = ViewFactory.eINSTANCE.createView();
		final Group group1 = createGroup();
		view.getChildren().add(group1);
		group1.getComposites().add(createControl());
		group1.getComposites().add(createControl());
		final Group group2 = createGroup();
		view.getChildren().add(group2);
		group2.getComposites().add(createControl());
		group2.getComposites().add(createControl());
		return view;
	}

	/**
	 * @return
	 */
	private static Group createGroup() {
		return GroupFactory.eINSTANCE.createGroup();
	}

	private static Control createControl() {
		final Control control = ViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference modelReference = ViewFactory.eINSTANCE
			.createVFeaturePathDomainModelReference();
		modelReference.setDomainModelEFeature(EcorePackage.eINSTANCE.getEClassifier_InstanceClassName());
		control.setDomainModelReference(modelReference);
		return control;
	}

}
