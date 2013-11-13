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

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.ecp.view.model.VViewFactory;
import org.eclipse.emf.ecp.view.vertical.model.VVerticalFactory;
import org.eclipse.emf.ecp.view.vertical.model.VVerticalLayout;
import org.junit.Test;

/**
 * @author Jonas
 * 
 */

public class ViewTest {

	@Test
	public void testEmptyView() {
		// setup model
		final VView view = VViewFactory.eINSTANCE.createView();
		// Test NodeBuidlers
		final Node<VElement> node = build(view);
		assertEquals(1, countNodes(node));
		assertEquals("Unknown Node has been instanciated", 0, node.getChildren().size());
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
		final VView view = VViewFactory.eINSTANCE.createView();
		// final Control control = ViewFactory.eINSTANCE.createControl();
		final VVerticalLayout control = VVerticalFactory.eINSTANCE.createVerticalLayout();
		// final VFeaturePathDomainModelReference createVFeaturePathDomainModelReference = ViewFactory.eINSTANCE
		// .createVFeaturePathDomainModelReference();
		// createVFeaturePathDomainModelReference.setDomainModelEFeature(EcorePackage.eINSTANCE.getENamedElement_Name());
		// control.setDomainModelReference(createVFeaturePathDomainModelReference);
		view.getChildren().add(control);
		final Node<VElement> node = build(view);
		assertEquals(2, countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 1, node.getChildren().size());
		final Node<?> childNode = node.getChildren().get(0);
		assertEquals(control, childNode.getRenderable());
	}

	@Test
	public void testViewWithChildrenAndCategories() {
		final VView view = VViewFactory.eINSTANCE.createView();
		final VControl control = VViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference createVFeaturePathDomainModelReference = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		createVFeaturePathDomainModelReference.setDomainModelEFeature(EcorePackage.eINSTANCE.getENamedElement_Name());
		control.setDomainModelReference(createVFeaturePathDomainModelReference);
		view.getChildren().add(control);
		final Node<VElement> node = ViewTestHelper.build(view, null);
		assertEquals(2, countNodes(node));
		assertEquals(view, node.getRenderable());
		assertEquals("Incorrect number of nodes have been instanciated", 1, node.getChildren().size());
		final Node<?> childNode = node.getChildren().get(0);
		assertEquals(control, childNode.getRenderable());
	}

	/**
	 * @param view
	 * @return
	 */
	private Node<VElement> build(VView view) {
		return ViewTestHelper.build(view, null);
	}

}
