/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 * Johannes Falterimeier - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.ecp.view.spi.core.swt.SWTContainerRenderer;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCellDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * The Class SWTViewRenderer.
 */
public class SWTViewRenderer extends SWTContainerRenderer<VView> {
	/**
	 * Default constructor.
	 */
	public SWTViewRenderer() {
		super();
	}

	/**
	 * Test constructor.
	 * 
	 * @param factory the {@link SWTRendererFactory} to use.
	 */
	SWTViewRenderer(SWTRendererFactory factory) {
		super(factory);
	}

	@Override
	protected final void setLayoutDataForControl(GridCell gridCell, GridDescription gridDescription,
		int maxNumberColumns, Set<GridCellDescription> preDescriptions, Set<GridCellDescription> postDescriptions,
		Control control) {

		if (gridCell.getColumn() + 1 == gridDescription.getColumns()) {
			GridDataFactory
				.fillDefaults()
				.align(SWT.FILL, SWT.FILL)
				.grab(true, true)
				.span(
					1 + maxNumberColumns - gridDescription.getColumns() - preDescriptions.size()
						- postDescriptions.size(), 1)
				.applyTo(control);
		} else if (gridCell.getColumn() == 0) {
			GridDataFactory.fillDefaults().grab(false, false)
				.align(SWT.FILL, SWT.CENTER)
				.applyTo(control);
		}
		else if (gridCell.getColumn() == 1) {
			GridDataFactory.fillDefaults()
				.align(SWT.CENTER, SWT.CENTER).hint(16, 17)
				.grab(false, false)
				.applyTo(control);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SWTContainerRenderer#getChildren()
	 */
	@Override
	protected Collection<VContainedElement> getChildren() {
		return getVElement().getChildren();
	}

	@Override
	protected Layout getLayout(int numControls, boolean equalWidth) {
		return GridLayoutFactory.fillDefaults().numColumns(numControls).equalWidth(equalWidth).create();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SWTContainerRenderer#getCustomVariant()
	 */
	@Override
	protected String getCustomVariant() {
		return "org_eclipse_emf_ecp_ui_layout_view"; //$NON-NLS-1$
	}
}
