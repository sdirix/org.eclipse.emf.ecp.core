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
 *******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.ecp.view.test.common.swt.SWTViewTestHelper;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonas
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class SWTViewTest {

	@Test
	public void testEmptyView() throws NoRendererFoundException,
			NoPropertyDescriptorFoundExeption {
		// setup model
		final View view = ViewFactory.eINSTANCE.createView();

		// setup ui
		Shell shell = SWTViewTestHelper.createShell();

		Control control = SWTViewTestHelper.render(view, shell);
		assertEquals("The Composite for the View has not been rendered", 1,
				shell.getChildren().length);
		assertTrue("View was not rendered as Composite",
				shell.getChildren()[0] instanceof Composite);
		assertEquals("Returned control and rendered control are not the same",
				control, shell.getChildren()[0]);
	}
}
