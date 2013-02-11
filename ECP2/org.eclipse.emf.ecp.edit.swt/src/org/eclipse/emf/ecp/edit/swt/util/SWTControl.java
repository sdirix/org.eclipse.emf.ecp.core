package org.eclipse.emf.ecp.edit.swt.util;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.AbstractControl;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public abstract class SWTControl extends AbstractControl<Composite> {

	private IObservableValue modelValue;

	public SWTControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}

	/**
	 * Returns the {@link DataBindingContext} set in the constructor.
	 * 
	 * @return the {@link DataBindingContext}
	 */
	protected DataBindingContext getDataBindingContext() {
		return getModelElementContext().getDataBindingContext();
	}
	
	protected IObservableValue getModelValue(){
		if(modelValue!=null)
			return modelValue;
		IObservableValue model = EMFEditObservables.observeValue(getModelElementContext().getEditingDomain(),
			getModelElementContext().getModelElement(), getStructuralFeature());
		return model;
	}
	public void setObservableValue(IObservableValue modelValue){
		this.modelValue=modelValue;
	}
	
	protected Button createButtonForAction(final Action action, Composite composite) {
		Button selectButton = new Button(composite, SWT.PUSH);
		selectButton.setImage(action.getImageDescriptor().createImage());
		selectButton.setToolTipText(action.getToolTipText());
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				action.run();
			}

		});
		return selectButton;
	}
}
