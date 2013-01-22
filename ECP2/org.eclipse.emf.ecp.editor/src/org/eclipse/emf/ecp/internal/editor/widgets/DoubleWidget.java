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
public class DoubleWidget extends AbstractTextWidget<Double> {
	/**
	 * @param dbc
	 */
	public DoubleWidget(DataBindingContext dbc, EditingDomain editingDomain, EObject eObject) {
		super(dbc, editingDomain, eObject);
	}

	@Override
	protected Double convertStringToModel(String s) {
		return Double.parseDouble(s);
	}

	@Override
	protected boolean validateString(String s) {
		/*
		 * Do not perform any validation here, since a double can be represented with characters which include 'E', 'f'
		 * or
		 * 'd'. Furthermore if values become to be, 'Infinity' is also a valid value.
		 */
		return true;
	}

	@Override
	protected String convertModelToString(Double t) {
		return Double.toString(t);
	}

	@Override
	protected void postValidate(String text) {
		try {
			setUnvalidatedString(Double.toString(Double.parseDouble(text)));
		} catch (NumberFormatException e) {
			setUnvalidatedString(Double.toString(getDefaultValue()));
		}
	}

	@Override
	protected Double getDefaultValue() {
		return 0.0;
	}
}
