/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.mecontrols;

import org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.StringWidget;

import org.eclipse.core.databinding.DataBindingContext;

/**
 * Standard widgets to edit a single line text attribute.
 * 
 * @author helming
 * @author emueller
 * @author Eugen Neufeld
 */
public class METextControl extends MEAttributeControl {

	private ECPAttributeWidget widget;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.MEAttributeControl#getAttributeWidget(org.eclipse.emf.databinding.
	 * EMFDataBindingContext)
	 */
	@Override
	protected ECPAttributeWidget getAttributeWidget(DataBindingContext dbc) {
		widget = new StringWidget(dbc);
		return widget;
	}

	/**
	 * This sets the keyboard focus in Text control.
	 **/
	public void setFocus() {
		widget.getControl().setFocus();
	}
}
