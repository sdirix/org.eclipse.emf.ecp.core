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

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget;
import org.eclipse.emf.ecp.internal.editor.widgets.EEnumWidget;

/**
 * This is the standard Control to enum values.
 * 
 * @author shterev
 * @author Nikolay Kasyanov
 * @author Eugen Neufeld
 */
public class MEEnumControl extends AttributeControl {
	/**
	 * @return
	 */
	@Override
	protected Class<?> getFeatureClass(EStructuralFeature feature) {
		return feature.getEType().getClass();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return EEnum.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.MEAttributeControl#getAttributeWidget(org.eclipse.emf.databinding.
	 * EMFDataBindingContext)
	 */
	@Override
	protected ECPAttributeWidget getWidget() {
		return new EEnumWidget(getContext().getDataBindingContext(), getContext().getEditingDomain(),
			getItemPropertyDescriptor(), getModelElement(), getStructuralFeature());
	}
}
