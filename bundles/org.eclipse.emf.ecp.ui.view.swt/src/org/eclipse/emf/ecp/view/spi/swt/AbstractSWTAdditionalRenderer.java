/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.swt;

import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCellDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;

/**
 * The renderer for additional controls.
 * 
 * @author Eugen Neufeld
 * @param <VELEMENT> the {@link VElement} this renderer is valid for
 */
public abstract class AbstractSWTAdditionalRenderer<VELEMENT extends VElement> extends AbstractRenderer<VELEMENT> {

	/**
	 * Number of additional columns provided by this renderer.
	 * 
	 * @return the number of additional columns
	 */
	public abstract int getAdditionalColumns();

	/**
	 * Called after a control was rendered in order to allow to add a control after the currently rendered cell.
	 * 
	 * @param gridDescription the {@link GridDescription} of the actual control
	 * @param cell the current rendered {@link GridCell}
	 * @param parent the {@link Composite} to render onto
	 * @return the {@link GridCellDescription} for the rendered control or null if nothing was rendered
	 */
	public abstract GridCellDescription postCellRenderControl(GridDescription gridDescription, final GridCell cell,
		Composite parent);

	/**
	 * Called before a control was rendered in order to allow to add a control before the cell is rendered.
	 * 
	 * @param gridDescription the {@link GridDescription} of the actual control
	 * @param cell the {@link GridCell} to be rendered
	 * @param parent the {@link Composite} to render onto
	 * @return the {@link GridCellDescription} for the rendered control or null if nothing was rendered
	 */
	public abstract GridCellDescription preCellRenderControl(GridDescription gridDescription, final GridCell cell,
		Composite parent);

	/**
	 * Called by the framework to initialize listener.
	 * 
	 * @param parent the {@link Composite} used to render onto
	 */
	public final void postRender(Composite parent) {

		parent.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent event) {
				dispose();
			}
		});
	}
}
