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

import org.eclipse.emf.ecp.editor.mecontrols.widgets.DateTimeWidget;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget;

import org.eclipse.core.databinding.DataBindingContext;

import java.util.Date;

/**
 * An SWT-based date widget with an additional time field, which are both controlled by spinners.
 * 
 * @author Hunnilee
 * @author Eugen Neufeld
 */
public class MESWTDateAndTimeControl extends MEAttributeControl {
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return Date.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.MEAttributeControl#getAttributeWidget(org.eclipse.emf.databinding.
	 * EMFDataBindingContext)
	 */
	@Override
	protected ECPAttributeWidget getAttributeWidget(DataBindingContext dbc) {
		return new DateTimeWidget(dbc, getModelElement(), getStructuralFeature(), getContext().getEditingDomain());
	}

}
