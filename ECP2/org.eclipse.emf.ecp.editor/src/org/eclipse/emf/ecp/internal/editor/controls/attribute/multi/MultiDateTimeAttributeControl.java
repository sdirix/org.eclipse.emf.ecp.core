/**
 * 
 */
package org.eclipse.emf.ecp.internal.editor.controls.attribute.multi;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecp.internal.editor.widgets.DateTimeWidget;
import org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget;

import java.util.Date;

/**
 * @author Eugen Neufeld
 */
public class MultiDateTimeAttributeControl extends MultiMEAttributeControl {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.editor.mecontrols.multiattributecontrol.MultiMEAttributeControl#getAttributeWidget(org.eclipse
	 * .emf.databinding.EMFDataBindingContext)
	 */
	@Override
	protected ECPAttributeWidget getAttributeWidget(EMFDataBindingContext dbc) {
		return new DateTimeWidget(dbc, getModelElement(), getStructuralFeature(), getContext().getEditingDomain());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return Date.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.multiattributecontrol.MultiMEAttributeControl#getDefaultValue()
	 */
	@Override
	protected Object getDefaultValue() {
		return new Date();
	}

}
