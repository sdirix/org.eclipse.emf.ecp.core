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

/**
 * A {@link GridDescription} describes the grid of the renderer using a list of {@link GridCell GridCells} and the
 * number of rows and columns it has.
 * 
 * Use the {@link GridDescriptionFactory} to create {@link GridDescription GridDescriptions}.
 * 
 * @author Eugen Neufeld
 * @since 1.3
 * 
 */
public class GridDescription {

	private List<GridCell> grid;
	private int rows;

	private int columns;

	/**
	 * Creating an empty grid.
	 */
	public GridDescription() {

	}

	/**
	 * Creating a filled grid.
	 * 
	 * @param rows number of rows in this description
	 * @param columns number of columns in this description
	 * @param grid the List of {@link GridCell GridCells} describing the grid
	 */
	public GridDescription(int rows, int columns, List<GridCell> grid) {
		this.grid = grid;
		this.rows = rows;
		this.columns = columns;
	}

	/**
	 * The {@link GridCell GridCells} describing the grid.
	 * 
	 * @return the grid
	 */
	public List<GridCell> getGrid() {
		return grid;
	}

	/**
	 * Number of rows in this Grid.
	 * 
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Number of columns in this grid.
	 * 
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * List of {@link GridCell GridCells}.
	 * 
	 * @param grid the grid to set
	 */
	public void setGrid(List<GridCell> grid) {
		this.grid = grid;
	}

	/**
	 * Sets the number of rows.
	 * 
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Sets the number of columns.
	 * 
	 * @param columns the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * Creates a copy of the grid.
	 * 
	 * @return a copy of the grid
	 */
	public GridDescription copy() {
		final GridDescription gd = new GridDescription(rows, columns, new ArrayList<GridCell>(grid));
		return gd;
	}
}
