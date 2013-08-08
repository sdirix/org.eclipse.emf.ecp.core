/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.swt.widgets.Control;

/**
 * @author Eugen Neufeld
 * 
 */
public class SingleColumnRow implements RenderingResultRow<Control> {

	private final Control control;

	/**
	 * A {@link RenderingResultRow} which holds one {@link Control}.
	 * 
	 * @param control the Control for the Column
	 */
	public SingleColumnRow(Control control) {
		this.control = control;
	}

	/**
	 * @return the Control
	 */
	public Control getControl() {
		return control;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow#getMainControl()
	 */
	public Control getMainControl() {
		return getControl();
	}

}
