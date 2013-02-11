package org.eclipse.emf.ecp.internal.edit.swt.controls;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class IntegerControl extends AbstractTextControl<Integer> {

	public IntegerControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}
	@Override
	protected Integer convertStringToModel(String s) {
		return Integer.parseInt(s);
	}

	@Override
	protected boolean validateString(String s) {
		try {
			// TODO regex?
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}

	}

	@Override
	protected String convertModelToString(Integer t) {
		return Integer.toString(t);
	}

	@Override
	protected void postValidate(String text) {
		try {
			setUnvalidatedString(Integer.toString(Integer.parseInt(text)));
		} catch (NumberFormatException e) {
			setUnvalidatedString(Integer.toString(getDefaultValue()));
		}
	}

	@Override
	protected Integer getDefaultValue() {
		return 0;
	}
}
