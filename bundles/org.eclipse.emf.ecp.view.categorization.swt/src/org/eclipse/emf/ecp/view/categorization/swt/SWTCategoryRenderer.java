/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.view.categorization.swt;

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.SWTRenderers;
import org.eclipse.emf.ecp.view.categorization.model.VCategory;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

// TODO: do we need to set a custom variant?
public class SWTCategoryRenderer extends AbstractSWTRenderer<VCategory> {
	public static final SWTCategoryRenderer INSTANCE = new SWTCategoryRenderer();

	@Override
	public List<RenderingResultRow<Control>> renderSWT(Node<VCategory> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Composite parent = getParentFromInitData(initData);
		final Composite categoryComposite = new Composite(parent, SWT.NONE);
		categoryComposite.setBackground(parent.getBackground());

		categoryComposite.setLayout(getLayoutHelper().getColumnLayout(2, false));

		node.addRenderingResultDelegator(withSWT(categoryComposite));

		final Node<? extends VElement> childNode = node.getChildren().get(0);

		final List<RenderingResultRow<Control>> resultRows = SWTRenderers.INSTANCE.render(categoryComposite, childNode,
			adapterFactoryItemDelegator);

		setLayoutDataForResultRows(resultRows);

		return createResult(categoryComposite);
	}
}
