/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Eugen Neufeld
 */
public class BooleanWidget extends ECPAttributeWidget {

	/**
	 * @param dbc
	 */
	public BooleanWidget(DataBindingContext dbc, EditingDomain editingDomain) {
		super(dbc, editingDomain);
	}

	private Button check;

	@Override
	public Control createWidget(final FormToolkit toolkit, final Composite composite, final int style) {
		check = toolkit.createButton(composite, "", SWT.CHECK);
		return check;
	}

	@Override
	public void bindValue(final IObservableValue modelValue, final ControlDecoration controlDecoration) {
		IObservableValue targetValue = SWTObservables.observeSelection(check);
		getDbc().bindValue(targetValue, modelValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean isEditable) {
		check.setEnabled(isEditable);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#getControl()
	 */
	@Override
	public Control getControl() {
		return check;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPWidget#dispose()
	 */
	@Override
	public void dispose() {
		check.dispose();
	}
}
