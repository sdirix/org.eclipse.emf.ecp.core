/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.ModelRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.ui.view.SelectedNodeChangedListener;
import org.eclipse.emf.ecp.ui.view.swt.internal.DefaultControlContext;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.Player;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author jfaltermeier
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class RendererContextTest {

	private Player player;
	private Shell shell;
	private Node<View> root;

	@Before
	public void before() {
		player = BowlingFactory.eINSTANCE.createPlayer();
		player.setName("Gustav Gans");
		player.setDateOfBirth(new Date());
		player.setNumberOfVictories(3);
		player.setWinLossRatio(new BigDecimal("0.3"));
		player.getEMails().clear();
	}

	@Test
	public void testSelectionChanged() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Display display = Display.getDefault();
		shell = new Shell(display);
		shell.setText("DirectoryDialog");
		shell.setLayout(new GridLayout());
		final Composite parent = new Composite(shell, SWT.NONE);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parent.setLayout(new GridLayout());
		final View view = ViewFactory.eINSTANCE.createView();

		final ModelRenderer<org.eclipse.swt.widgets.Control> renderer = ModelRenderer.INSTANCE
			.getRenderer();

		final ECPControlContext context = new DefaultControlContext(player, view);
		root = NodeBuilders.INSTANCE.build(view, context);

		final List<Renderable> result = new ArrayList<Renderable>();

		final RendererContext<org.eclipse.swt.widgets.Control> rendererContext = renderer.render(root, parent);
		rendererContext.addSelectionChangedListener(new SelectedNodeChangedListener() {
			public <T extends Renderable> void selectionChanged(T selectedRenderable) {
				result.add(selectedRenderable);
			}
		});

		rendererContext.childSelected(root);

		assertEquals("Result list is empty. Listener not called?!", 1, result.size());
		assertEquals("Unexpected selection", view, result.get(0));
	}

}
