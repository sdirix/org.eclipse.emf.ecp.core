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
public class GridCellDescription {

	private final int spanHorizontal;
	private final int spanVertical;

	public GridCellDescription() {
		this(1, 1);
	}

	public GridCellDescription(int spanHorizontal, int spanVertical) {
		this.spanHorizontal = spanHorizontal;
		this.spanVertical = spanVertical;
	}

	/**
	 * The horizontal span.
	 * 
	 * @return the spanHorizontal
	 */
	public int getSpanHorizontal() {
		return spanHorizontal;
	}

	/**
	 * The vertical span.
	 * 
	 * @return the spanVertical
	 */
	public int getSpanVertical() {
		return spanVertical;
	}

}
