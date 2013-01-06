/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.fieldassist.ControlDecoration;

/**
 * @author Eugen Neufeld
 */
public abstract class ECPAttributeWidget extends ECPWidget {
	private final DataBindingContext dbc;
	private final EditingDomain editingDomain;

	public ECPAttributeWidget(DataBindingContext dbc, EditingDomain editingDomain) {
		this.dbc = dbc;
		this.editingDomain = editingDomain;
	}

	public abstract void bindValue(final IObservableValue modelValue, final ControlDecoration controlDecoration);

	/**
	 * @return the dbc
	 */
	protected DataBindingContext getDbc() {
		return dbc;
	}

	/**
	 * @return the editingDomain
	 */
	protected EditingDomain getEditingDomain() {
		return editingDomain;
	}
}
