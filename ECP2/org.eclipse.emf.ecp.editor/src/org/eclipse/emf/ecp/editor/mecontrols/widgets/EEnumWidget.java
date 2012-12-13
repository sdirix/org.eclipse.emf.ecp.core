/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Eugen Neufeld
 */
public class EEnumWidget extends ECPAttributeWidget {

	private IItemPropertyDescriptor itemPropertyDescriptor;

	private EObject modelElement;

	private EStructuralFeature feature;

	private ComboViewer combo;

	/**
	 * @param dbc
	 */
	public EEnumWidget(DataBindingContext dbc, IItemPropertyDescriptor itemPropertyDescriptor, EObject modelElement,
		EStructuralFeature feature) {
		super(dbc);
		this.modelElement = modelElement;
		this.itemPropertyDescriptor = itemPropertyDescriptor;
		this.feature = feature;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#createWidget(org.eclipse.ui.forms.widgets.
	 * FormToolkit,
	 * org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	public Control createWidget(FormToolkit toolkit, Composite composite, int style) {
		final IItemLabelProvider labelProvider = itemPropertyDescriptor.getLabelProvider(modelElement);

		combo = new ComboViewer(composite);
		combo.setContentProvider(new ArrayContentProvider());
		combo.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				return labelProvider.getText(element);
			}

		});
		combo.setInput(feature.getEType().getInstanceClass().getEnumConstants());
		return combo.getControl();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean isEditable) {
		combo.getControl().setEnabled(isEditable);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#bindValue(org.eclipse.core.databinding.observable
	 * .value
	 * .IObservableValue)
	 */
	@Override
	public void bindValue(IObservableValue modelValue, final ControlDecoration controlDecoration) {
		IObservableValue target = ViewersObservables.observeSingleSelection(combo);
		getDbc().bindValue(target, modelValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#getControl()
	 */
	@Override
	public Control getControl() {
		return combo.getControl();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPWidget#dispose()
	 */
	@Override
	public void dispose() {
		combo.getCombo().dispose();
	}

}
