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

import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;

/**
 * A {@link GridCell} has a row, a column and a renderer it is rendered by.
 * 
 * @author Eugen Neufeld
 * @since 1.3
 * 
 */
public class GridCell {

	private final int row;
	private final int column;
	private AbstractSWTRenderer<? extends VElement> renderer;

	/**
	 * Default constructor to create a grid cell.
	 * 
	 * @param row the row of the cell
	 * @param column the column of the cell
	 * @param renderer the {@link AbstractSWTRenderer} that renderes the cell
	 */
	public GridCell(int row, int column, AbstractSWTRenderer<? extends VElement> renderer) {
		super();
		this.row = row;
		this.column = column;
		this.renderer = renderer;
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
	 * @return the renderer
	 */
	public AbstractSWTRenderer<? extends VElement> getRenderer() {
		return renderer;
	}

	/**
	 * @param renderer the renderer to set
	 */
	public void setRenderer(AbstractSWTRenderer<? extends VElement> renderer) {
		this.renderer = renderer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + (renderer == null ? 0 : renderer.hashCode());
		result = prime * result + row;
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final GridCell other = (GridCell) obj;
		if (column != other.column) {
			return false;
		}
		if (renderer == null) {
			if (other.renderer != null) {
				return false;
			}
		} else if (!renderer.equals(other.renderer)) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
		return true;
	}

}
