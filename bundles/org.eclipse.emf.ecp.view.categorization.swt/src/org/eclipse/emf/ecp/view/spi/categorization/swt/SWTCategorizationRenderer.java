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
package org.eclipse.emf.ecp.view.spi.categorization.swt;

import org.eclipse.emf.ecp.view.internal.categorization.swt.Messages;
import org.eclipse.emf.ecp.view.spi.categorization.model.VCategorization;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescriptionFactory;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription()
	 */
	@Override
	public GridDescription getGridDescription() {
		return GridDescriptionFactory.INSTANCE.createSimpleGrid(1, 1);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#renderControl(int, org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.emf.ecp.view.spi.model.VElement, org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	protected Control renderControl(GridCell cell, Composite parent) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final Composite categoryComposite = new Composite(parent, SWT.NONE);
		categoryComposite.setBackground(parent.getBackground());
		categoryComposite.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_categorization"); //$NON-NLS-1$

		categoryComposite.setLayout(getLayoutHelper().getColumnLayout(1, false));

		final Label headingLbl = new Label(categoryComposite, SWT.NONE);
		headingLbl.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_categorization_title"); //$NON-NLS-1$
		final Label whatToDoLbl = new Label(categoryComposite, SWT.NONE);
		whatToDoLbl.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_categorization_message"); //$NON-NLS-1$
		final String headingText = getVElement().getName();
		headingLbl.setText(headingText == null ? "" : headingText); //$NON-NLS-1$
		whatToDoLbl.setText(Messages.Categorization_Selection);
		return categoryComposite;
	}

}
