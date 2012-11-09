/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.core.databinding.DataBindingContext;

/**
 * @author Eugen Neufeld
 */
public class IntegerWidget extends AbstractTextWidget<Integer> {

	/**
	 * @param dbc
	 */
	public IntegerWidget(DataBindingContext dbc) {
		super(dbc);
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
