/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
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
	public EEnumWidget(DataBindingContext dbc, EditingDomain editingDomain,
		IItemPropertyDescriptor itemPropertyDescriptor, EObject modelElement, EStructuralFeature feature) {
		super(dbc, editingDomain);
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

	// ALTERNATIVe
	// EStructuralFeature feature = propDescriptor.getFeature();
	// ComboViewer combo = new ComboViewer(composite, SWT.DROP_DOWN);
	// combo.setContentProvider(ArrayContentProvider.getInstance());
	// combo.setLabelProvider(new LabelProvider() {
	// @Override
	// public String getText(Object element) {
	// return ((EEnumLiteral)element).getName();
	// }
	// });
	//
	// final EEnum eEnum = (EEnum) feature.getEType();
	// combo.setInput(eEnum.getELiterals());
	// combo.setSelection(new StructuredSelection(obj.eGet(feature)));
	// combo.getCombo().setLayoutData(
	// new GridData(SWT.FILL, SWT.NULL, true, false));
	//
	// IObservableValue observeNumber = ViewersObservables
	// .observeSingleSelection(combo);
	// IObservableValue observeValue = EMFEditObservables.observeValue(
	// editingDomain, obj, feature);
	// dataBindingContext.bindValue(observeNumber, observeValue,new UpdateValueStrategy(){
	//
	// @Override
	// public Object convert(Object value) {
	// if(value instanceof EEnumLiteral){
	// return ((EEnumLiteral)value).getInstance();
	// }
	// return super.convert(value);
	// }
	//
	// },new UpdateValueStrategy(){
	// @Override
	// public Object convert(Object value) {
	// if(value instanceof Enumerator){
	// Enumerator enumerator=(Enumerator)value;
	// return eEnum.getEEnumLiteralByLiteral(enumerator.getLiteral());
	// }
	// return super.convert(value);
	// }
	// });
	// return combo.getCombo();

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
