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
public class GridDescription {

	private final GridCell[] grid;
	private final int rows;

	private final int columns;

	public GridDescription(int rows, int columns, GridCell[] grid) {
		this.grid = grid;
		this.rows = rows;
		this.columns = columns;
	}

	public GridCell[] getGrid() {
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
}
