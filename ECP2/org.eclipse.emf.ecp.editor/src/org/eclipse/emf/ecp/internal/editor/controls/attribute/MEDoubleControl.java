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
package org.eclipse.emf.ecp.internal.editor.controls.attribute;

import org.eclipse.emf.ecp.internal.editor.widgets.DoubleWidget;
import org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget;

import org.eclipse.core.databinding.DataBindingContext;

/**
 * Standard widget to edit a double attribute.
 * 
 * @author helming
 * @author emueller
 * @author Eugen Neufeld
 */
public class MEDoubleControl extends AttributeControl {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return Double.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.MEAttributeControl#getAttributeWidget(org.eclipse.emf.databinding.
	 * EMFDataBindingContext)
	 */
	@Override
	protected ECPAttributeWidget getAttributeWidget(DataBindingContext dbc) {
		return new DoubleWidget(dbc, getContext().getEditingDomain(), getModelElement());
	}
}
