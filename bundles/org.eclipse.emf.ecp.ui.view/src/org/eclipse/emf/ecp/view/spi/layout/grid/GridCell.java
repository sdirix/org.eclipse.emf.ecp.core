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
public class GridCell {

	private final int row;
	private final int column;
	private final GridCellDescription gridCellDescription;

	public GridCell(int row, int column, GridCellDescription gridCellDescription) {
		super();
		this.row = row;
		this.column = column;
		this.gridCellDescription = gridCellDescription;
	}

	/**
	 * The id of the row.
	 * 
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * The id of the column.
	 * 
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * The {@link GridCellDescription} of this GridCell.
	 * 
	 * @return the gridCellDescription
	 */
	public GridCellDescription getGridCellDescription() {
		return gridCellDescription;
	}
}
