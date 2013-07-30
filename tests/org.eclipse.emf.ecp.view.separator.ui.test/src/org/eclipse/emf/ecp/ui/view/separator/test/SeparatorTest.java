/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.ui.view.separator.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.separator.model.Separator;
import org.eclipse.emf.ecp.view.separator.model.SeparatorFactory;

import org.junit.Test;

public class SeparatorTest {

	@Test
	public void testSeparator() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		// setup model
		View view = ViewFactory.eINSTANCE.createView();
		Separator separator = SeparatorFactory.eINSTANCE.createSeparator();
		separator.setName("separator");
		view.getChildren().add(separator);

		// Test NodeBuidlers
		Node<View> node = NodeBuilders.INSTANCE.build(view, null);
		assertEquals("No Node has been instanciated", 1, node.getChildren().size());
		Node<?> childNode = node.getChildren().get(0);
		assertTrue(childNode.getRenderable() instanceof Separator);

	}
}
