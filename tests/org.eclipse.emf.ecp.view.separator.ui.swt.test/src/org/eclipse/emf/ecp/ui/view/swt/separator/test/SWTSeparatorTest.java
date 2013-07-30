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
package org.eclipse.emf.ecp.ui.view.swt.separator.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderers;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.separator.model.Separator;
import org.eclipse.emf.ecp.view.separator.model.SeparatorFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DatabindingClassRunner.class)
public class SWTSeparatorTest {

	/**
	 * 
	 */
	private static final String SEPARATOR_NAME = "separator";

	@Test
	public void testSeparator() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption,
		ECPProjectWithNameExistsException {

		// setup model
		View view = ViewFactory.eINSTANCE.createView();
		Separator separator = SeparatorFactory.eINSTANCE.createSeparator();
		separator.setName(SEPARATOR_NAME);
		view.getChildren().add(separator);

		// setup ui
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		// shell.setLayout(new GridLayout());
		// Composite parent = new Composite(shell, SWT.NONE);
		// parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// parent.setLayout(new GridLayout());

		ECPControlContext context = ViewTestHelper.createECPControlContext(view, shell);

		// test SWTRenderer
		Node<View> node = NodeBuilders.INSTANCE.build(view, context);
		Node<?> sepNode = node.getChildren().get(0);
		ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);
		Control control = SWTRenderers.INSTANCE.render(shell, sepNode, adapterFactoryItemDelegator);
		Control renderedControl = shell.getChildren()[0];
		assertEquals("Rendered Control and control returned by renderer are not the same", control, renderedControl);
		assertTrue(renderedControl instanceof Label);
		Label label = (Label) renderedControl;
		assertEquals("Rendered Separator does not have correct name", SEPARATOR_NAME, label.getText());
		assertEquals("org_eclipse_emf_ecp_ui_seperator", renderedControl.getData("org.eclipse.rap.rwt.customVariant"));

	}
}
