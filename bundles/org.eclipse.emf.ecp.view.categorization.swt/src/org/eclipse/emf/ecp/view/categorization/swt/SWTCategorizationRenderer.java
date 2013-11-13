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
package org.eclipse.emf.ecp.view.categorization.swt;

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.Messages;
import org.eclipse.emf.ecp.view.categorization.model.VCategorization;
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
public class SWTCategorizationRenderer extends AbstractSWTRenderer<VCategorization> {

	/** Singleton renderer instance. **/
	public static final SWTCategorizationRenderer INSTANCE = new SWTCategorizationRenderer();

	@Override
	public List<RenderingResultRow<Control>> renderSWT(Node<VCategorization> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Composite parent = getParentFromInitData(initData);
		final Composite categoryComposite = new Composite(parent, SWT.NONE);
		categoryComposite.setBackground(parent.getBackground());
		categoryComposite.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_categorization");

		categoryComposite.setLayout(getLayoutHelper().getColumnLayout(1, false));

		node.addRenderingResultDelegator(withSWT(categoryComposite));

		final VCategorization categorization = VCategorization.class.cast(node.getRenderable());
		final Label headingLbl = new Label(categoryComposite, SWT.NONE);
		headingLbl.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_categorization_title");
		final Label whatToDoLbl = new Label(categoryComposite, SWT.NONE);
		whatToDoLbl.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_categorization_message");
		headingLbl.setText(categorization.getName());
		whatToDoLbl.setText(Messages.Categorization_Selection);

		return createResult(categoryComposite);
	}
}
