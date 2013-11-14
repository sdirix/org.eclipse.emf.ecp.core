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

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.Player;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author jfaltermeier
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class RendererContextTest {

	private Player player;
	private Shell shell;
	private VView view;

	@Before
	public void before() {
		player = BowlingFactory.eINSTANCE.createPlayer();
		player.setName("Gustav Gans");
		player.setDateOfBirth(new Date());
		player.setNumberOfVictories(3);
		player.setWinLossRatio(new BigDecimal("0.3"));
		player.getEMails().clear();
	}

	// @Test
	// public void testSelectionChanged() throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
	// final Display display = Display.getDefault();
	// shell = new Shell(display);
	// shell.setText("DirectoryDialog");
	// shell.setLayout(new GridLayout());
	// final Composite parent = new Composite(shell, SWT.NONE);
	// parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	// parent.setLayout(new GridLayout());
	// view = VViewFactory.eINSTANCE.createView();
	//
	// final ModelRenderer<org.eclipse.swt.widgets.Control> renderer = ModelRenderer.INSTANCE
	// .getRenderer();
	//
	// final ECPControlContext context = new DefaultControlContext(player, view);
	//
	//
	// final List<VElement> result = new ArrayList<VElement>();
	//
	// final RendererContext<org.eclipse.swt.widgets.Control> rendererContext = renderer.render(view, parent);
	// rendererContext.addSelectionChangedListener(new SelectedNodeChangedListener() {
	// public <T extends VElement> void selectionChanged(T selectedRenderable) {
	// result.add(selectedRenderable);
	// }
	// });
	//
	// rendererContext.childSelected(root);
	//
	// assertEquals("Result list is empty. Listener not called?!", 1, result.size());
	// assertEquals("Unexpected selection", view, result.get(0));
	// }

}
