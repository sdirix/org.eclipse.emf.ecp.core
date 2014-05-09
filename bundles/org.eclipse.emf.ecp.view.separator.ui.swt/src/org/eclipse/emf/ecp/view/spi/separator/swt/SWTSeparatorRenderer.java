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
package org.eclipse.emf.ecp.view.spi.separator.swt;

import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.separator.model.VSeparator;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.layout.SWTGridDescription;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescriptionFactory;
import org.eclipse.emf.ecp.view.spi.swt.layout.SWTGridCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Renders an {@link VSeparator} to a SWT {@link Label}.
 * 
 * @since 1.2
 * 
 */
public class SWTSeparatorRenderer extends AbstractSWTRenderer<VSeparator> {
	private SWTGridDescription rendererGridDescription;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#dispose()
	 */
	@Override
	protected void dispose() {
		rendererGridDescription = null;
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription(SWTGridDescription)
	 */
	@Override
	public SWTGridDescription getGridDescription(SWTGridDescription gridDescription) {
		if (rendererGridDescription == null) {
			rendererGridDescription = GridDescriptionFactory.INSTANCE.createSimpleGrid(1, 1, this);
			final SWTGridCell gridCell = rendererGridDescription.getGrid().get(0);
			gridCell.setVerticalGrab(false);
			gridCell.setVerticalFill(false);
			gridCell.setHorizontalFill(true);
			gridCell.setHorizontalGrab(true);
		}
		return rendererGridDescription;
	}

	@Override
	protected Control renderControl(SWTGridCell cell, Composite parent)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Label label = new Label(parent, SWT.NONE);
		if (getVElement().getName() != null) {
			label.setText(getVElement().getName());
		}
		label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_ui_seperator"); //$NON-NLS-1$

		return label;

	}

}
