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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * By providing an own implementation, the layout of the application can be influenced.
 * 
 * @author Eugen Neufeld
 */
public interface LayoutProvider {

	/**
	 * The Layout for aligning controls vertically in columns.
	 * 
	 * @param numColumns the number of columns to create
	 * @param equalWidth whether the columns should be equal width
	 * @return the layout to use
	 */
	Layout getColumnLayout(int numColumns, boolean equalWidth);

	/**
	 * The LayoutData to use.
	 * 
	 * @param gridCell the current {@link GridCell}
	 * @param controlGridDescription the {@link GridDescription} of the rendered {@link VElement}
	 * @param currentRowGridDescription the {@link GridDescription} of the current row
	 * @param fullGridDescription the {@link GridDescription} of the whole container
	 * @param vElement the {@link VElement} which is currently rendered
	 * @param control the rendered {@link Control}
	 * @return the Object being the layout data to set
	 */
	Object getLayoutData(GridCell gridCell, GridDescription controlGridDescription,
		GridDescription currentRowGridDescription, GridDescription fullGridDescription, VElement vElement,
		Control control);

	/**
	 * A simple spanning Layout-
	 * 
	 * @param spanX the horizontal span
	 * @param spanY the vertical span
	 * @return the created Layout Data
	 */
	Object getSpanningLayoutData(int spanX, int spanY);
}