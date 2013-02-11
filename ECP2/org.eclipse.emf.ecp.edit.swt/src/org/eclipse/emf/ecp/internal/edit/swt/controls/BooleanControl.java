package org.eclipse.emf.ecp.internal.edit.swt.controls;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class BooleanControl extends SingleControl {

	private Button check;

	public BooleanControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}

	@Override
	protected void fillInnerComposite(Composite composite) {
		check = new Button(composite, SWT.CHECK);
		
	}
	
	@Override
	public void setEditable(boolean isEditable) {
		check.setEnabled(isEditable);
	}
	@Override
	public void dispose() {
		check.dispose();
		super.dispose();
	}

	@Override
	public void bindValue() {
		IObservableValue targetValue = SWTObservables.observeSelection(check);
		getDataBindingContext().bindValue(targetValue, getModelValue());
	}
}
