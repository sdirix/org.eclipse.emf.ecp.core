package org.eclipse.emf.ecp.internal.edit.swt.controls;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;

public class EEnumControl extends SingleControl {

	private ComboViewer combo;
	
	public EEnumControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}

	@Override
	protected void fillInnerComposite(Composite composite) {
		final IItemLabelProvider labelProvider = getItemPropertyDescriptor().getLabelProvider(
			getModelElementContext().getModelElement());

		combo = new ComboViewer(composite);
		combo.setContentProvider(new ArrayContentProvider());
		combo.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				return labelProvider.getText(element);
			}

		});
		combo.setInput(getStructuralFeature().getEType().getInstanceClass().getEnumConstants());
	}

	@Override
	public void setEditable(boolean isEditable) {
		combo.getControl().setEnabled(isEditable);
	}

	@Override
	public void bindValue() {
		IObservableValue target = ViewersObservables.observeSingleSelection(combo);
		getDataBindingContext().bindValue(target, getModelValue());
	}

}
