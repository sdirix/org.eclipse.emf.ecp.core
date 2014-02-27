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
package org.eclipse.emf.ecp.view.spi.layout.grid;

/**
 * @author Eugen
 * 
 */
public final class GridDescriptionFactory {

	public static GridDescriptionFactory INSTANCE = new GridDescriptionFactory();

	private GridDescriptionFactory() {

	}

	public GridDescription createSimpleGrid(int rows, int columns) {
		final GridCell[] gridCells = new GridCell[rows * columns];
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				gridCells[column * (row + 1)] = new GridCell(row, column, new GridCellDescription());
			}
		}
		return new GridDescription(rows, columns, gridCells);
	}
}
