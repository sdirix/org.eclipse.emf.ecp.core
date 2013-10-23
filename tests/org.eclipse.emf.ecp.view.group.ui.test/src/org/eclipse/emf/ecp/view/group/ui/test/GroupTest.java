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
import org.eclipse.emf.ecp.view.group.model.VGroup;
import org.eclipse.emf.ecp.view.group.model.VGroupFactory;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.ecp.view.model.VViewFactory;
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
		final VView view = createViewWithOneGroup();
		final VGroup group = (VGroup) view.getChildren().get(0);
		// Test NodeBuidlers
		final Node<VElement> node = buildNode(view);
		assertEquals(2, ViewTestHelper.countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 1, node.getChildren().size());
		final Node<?> childNode = node.getChildren().get(0);
		assertEquals(group, childNode.getRenderable());
	}

	private Node<VElement> buildNode(final VView view) {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		return ViewTestHelper.build(view, eClass);
	}

	/**
	 * @return a View with one Group in it
	 */
	public static VView createViewWithOneGroup() {
		final VView view = VViewFactory.eINSTANCE.createView();
		final VGroup group = createGroup();
		view.getChildren().add(group);
		return view;
	}

	@Test
	public void testTwoGroupsinView() {
		// setup model
		final VView view = createViewWithTwoGroups();
		final VGroup group = (VGroup) view.getChildren().get(0);
		final VGroup group2 = (VGroup) view.getChildren().get(1);

		// Test NodeBuidlers
		final Node<VElement> node = buildNode(view);
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
	public static VView createViewWithTwoGroups() {
		final VView view = VViewFactory.eINSTANCE.createView();
		final VGroup group = createGroup();
		view.getChildren().add(group);
		final VGroup group2 = createGroup();
		view.getChildren().add(group2);
		return view;
	}

	@Test
	public void testTwoGroupsHierachicalinView() {
		// setup model
		final VView view = createViewWithTwoHierachicalGroups();
		final VGroup group = (VGroup) view.getChildren().get(0);
		final VGroup subGroup = (VGroup) group.getChildren().get(0);

		// Test NodeBuidlers
		final Node<VElement> node = buildNode(view);
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
		final VView view = createViewWithTwoGroupsWithTwoControls();
		final VGroup group1 = (VGroup) view.getChildren().get(0);
		final VControl c11 = (VControl) group1.getChildren().get(0);
		final VControl c12 = (VControl) group1.getChildren().get(1);
		final VGroup group2 = (VGroup) view.getChildren().get(1);
		final VControl c21 = (VControl) group2.getChildren().get(0);
		final VControl c22 = (VControl) group2.getChildren().get(1);

		// Test NodeBuidlers
		final Node<VElement> node = buildNode(view);
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
	public static VView createViewWithTwoHierachicalGroups() {
		final VView view = VViewFactory.eINSTANCE.createView();
		final VGroup group = createGroup();
		view.getChildren().add(group);
		final VGroup subGroup = createGroup();
		group.getChildren().add(subGroup);
		return view;
	}

	public static VView createViewWithTwoGroupsWithTwoControls() {
		final VView view = VViewFactory.eINSTANCE.createView();
		final VGroup group1 = createGroup();
		view.getChildren().add(group1);
		group1.getChildren().add(createControl());
		group1.getChildren().add(createControl());
		final VGroup group2 = createGroup();
		view.getChildren().add(group2);
		group2.getChildren().add(createControl());
		group2.getChildren().add(createControl());
		return view;
	}

	/**
	 * @return
	 */
	private static VGroup createGroup() {
		return VGroupFactory.eINSTANCE.createGroup();
	}

	private static VControl createControl() {
		final VControl control = VViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference modelReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		modelReference.setDomainModelEFeature(EcorePackage.eINSTANCE.getEClassifier_InstanceClassName());
		control.setDomainModelReference(modelReference);
		return control;
	}

}
