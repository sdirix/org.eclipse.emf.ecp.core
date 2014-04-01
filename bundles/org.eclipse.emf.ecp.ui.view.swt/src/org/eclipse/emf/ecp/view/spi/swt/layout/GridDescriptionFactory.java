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
package org.eclipse.emf.ecp.view.spi.swt.layout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;

/**
 * A Factory to create {@link GridDescription GridDescriptions}.
 * 
 * @author Eugen Neufeld
 * @since 1.3
 * 
 */
public final class GridDescriptionFactory {

	/**
	 * The static Instance of the Factory.
	 */
	public static final GridDescriptionFactory INSTANCE = new GridDescriptionFactory();

	private GridDescriptionFactory() {

	}

	/**
	 * Creates an empty grid description.
	 * 
	 * @return the {@link GridDescription}
	 */
	public GridDescription createEmptyGridDescription() {
		return new GridDescription();
	}

	/**
	 * Creates a simple grid based on the number of rows and columns provided.
	 * 
	 * @param rows the number of rows in this grid
	 * @param columns the number of columns in this grid
	 * @param renderer the {@link AbstractSWTRenderer}
	 * @return the {@link GridDescription}
	 */
	public GridDescription createSimpleGrid(int rows, int columns, AbstractSWTRenderer<? extends VElement> renderer) {
		final List<GridCell> gridCells = new ArrayList<GridCell>(rows * columns);
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				gridCells.add(new GridCell(row, column, renderer));
			}
		}
		return new GridDescription(rows, columns, gridCells);
	}
}
