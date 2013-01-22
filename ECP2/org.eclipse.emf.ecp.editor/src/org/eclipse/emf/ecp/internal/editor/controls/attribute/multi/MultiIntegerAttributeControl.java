/**
 * 
 */
package org.eclipse.emf.ecp.internal.editor.controls.attribute.multi;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget;
import org.eclipse.emf.ecp.internal.editor.widgets.IntegerWidget;

/**
 * @author Eugen Neufeld
 */
public class MultiIntegerAttributeControl extends MultiMEAttributeControl {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.editor.mecontrols.multiattributecontrol.MultiMEAttributeControl#getAttributeWidget(org.eclipse
	 * .emf.databinding.EMFDataBindingContext)
	 */
	@Override
	protected ECPAttributeWidget getAttributeWidget(EMFDataBindingContext dbc) {
		return new IntegerWidget(dbc, getContext().getEditingDomain(), getModelElement());
	}

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
	 * @see org.eclipse.emf.ecp.editor.mecontrols.multiattributecontrol.MultiMEAttributeControl#getDefaultValue()
	 */
	@Override
	protected Object getDefaultValue() {
		return 0;
	}
}
