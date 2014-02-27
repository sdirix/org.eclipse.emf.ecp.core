/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.spi.label.swt;

import org.eclipse.emf.ecp.view.spi.label.model.VLabel;
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
 * Renders an {@link VLabel} to a SWT {@link Label}.
 * 
 * @since 1.2
 * 
 */
public class SWTLabelRenderer extends AbstractSWTRenderer<VLabel> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription()
	 */
	@Override
	public GridDescription getGridDescription() {
		return GridDescriptionFactory.INSTANCE.createSimpleGrid(1, 1);
	}

	@Override
	protected Control renderControl(GridCell cell, Composite parent)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Label label = new Label(parent, SWT.NONE);
		if (getVElement().getName() != null) {
			label.setText(getVElement().getName());
		}
		label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_ui_" + getVElement().getStyle()); //$NON-NLS-1$
		return label;
	}

}
