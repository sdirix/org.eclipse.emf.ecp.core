/**
 * 
 */
package org.eclipse.emf.ecp.internal.editor.widgets;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.databinding.DataBindingContext;

/**
 * @author Eugen Neufeld
 */
public class IntegerWidget extends AbstractTextWidget<Integer> {

	/**
	 * @param dbc
	 */
	public IntegerWidget(DataBindingContext dbc, EditingDomain editingDomain, EObject eObject) {
		super(dbc, editingDomain, eObject);
	}

	@Override
	protected Integer convertStringToModel(String s) {
		return Integer.parseInt(s);
	}

	@Override
	protected boolean validateString(String s) {
		// TODO: perform validation
		return true;
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
