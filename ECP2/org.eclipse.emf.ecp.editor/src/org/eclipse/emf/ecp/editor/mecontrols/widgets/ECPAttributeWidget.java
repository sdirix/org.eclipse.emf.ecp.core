/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.fieldassist.ControlDecoration;

/**
 * @author Eugen Neufeld
 */
public abstract class ECPAttributeWidget extends ECPWidget {
	private final DataBindingContext dbc;

	public ECPAttributeWidget(DataBindingContext dbc) {
		this.dbc = dbc;
	}

	public abstract void bindValue(final IObservableValue modelValue, final ControlDecoration controlDecoration);

	/**
	 * @return the dbc
	 */
	protected DataBindingContext getDbc() {
		return dbc;
	}
}
