/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt;

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * SWT categorization renderer.
 * 
 * @author emueller
 * @author Eugen Neufeld
 * 
 */
// TODO: do we need to set a custom variant
public class SWTCategorizationRenderer extends AbstractSWTRenderer<Categorization> {

	/** Singleton renderer instance. **/
	public static final SWTCategorizationRenderer INSTANCE = new SWTCategorizationRenderer();

	@Override
	public List<RenderingResultRow<Control>> renderSWT(Node<Categorization> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Composite parent = getParentFromInitData(initData);
		final Composite categoryComposite = new Composite(parent, SWT.NONE);
		categoryComposite.setBackground(parent.getBackground());

		categoryComposite.setLayout(getLayoutHelper().getColumnLayout(1, false));

		node.addRenderingResultDelegator(withSWT(categoryComposite));

		final Categorization categorization = Categorization.class.cast(node.getRenderable());
		final Label headingLbl = new Label(categoryComposite, SWT.NONE);
		final Label whatToDoLbl = new Label(categoryComposite, SWT.NONE);
		headingLbl.setText(categorization.getName());
		whatToDoLbl.setText(Messages.Categorization_Selection);

		return createResult(categoryComposite);
	}
}
