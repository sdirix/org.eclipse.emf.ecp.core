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
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.Messages;
import org.eclipse.emf.ecp.view.categorization.model.VCategorization;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
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
// TODO: api
@SuppressWarnings("restriction")
public class SWTCategorizationRenderer extends AbstractSWTRenderer<VCategorization> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer#renderModel(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.emf.ecp.view.model.VElement, org.eclipse.emf.ecp.view.context.ViewModelContext)
	 */
	@Override
	protected List<RenderingResultRow<Control>> renderModel(Composite parent, VCategorization vCategorization,
		ViewModelContext viewContext)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Composite categoryComposite = new Composite(parent, SWT.NONE);
		categoryComposite.setBackground(parent.getBackground());
		categoryComposite.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_categorization"); //$NON-NLS-1$

		categoryComposite.setLayout(getLayoutHelper().getColumnLayout(1, false));

		final Label headingLbl = new Label(categoryComposite, SWT.NONE);
		headingLbl.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_categorization_title"); //$NON-NLS-1$
		final Label whatToDoLbl = new Label(categoryComposite, SWT.NONE);
		whatToDoLbl.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_categorization_message"); //$NON-NLS-1$
		final String headingText = vCategorization.getName();
		headingLbl.setText(headingText == null ? "" : headingText);
		whatToDoLbl.setText(Messages.Categorization_Selection);

		return createResult(categoryComposite);
	}
}
