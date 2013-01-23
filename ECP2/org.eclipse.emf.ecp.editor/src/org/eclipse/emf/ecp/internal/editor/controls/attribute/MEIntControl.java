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

import org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget;
import org.eclipse.emf.ecp.internal.editor.widgets.IntegerWidget;

/**
 * Standard widgets to edit a integer attribute.
 * 
 * @author helming
 * @author emueller
 * @author Eugen Neufeld
 */
public class MEIntControl extends AttributeControl {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return Integer.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.MEAttributeControl#getAttributeWidget(org.eclipse.emf.databinding.
	 * EMFDataBindingContext)
	 */
	@Override
	protected ECPAttributeWidget getWidget() {
		return new IntegerWidget(getContext().getDataBindingContext(), getContext().getEditingDomain(),
			getModelElement());
	}

}
