/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.internal.editor.widgets;

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
 * The widget implementation for editing an Enum value.
 * 
 * @author Eugen Neufeld
 */
public class EEnumWidget extends ECPAttributeWidget {

	private IItemPropertyDescriptor itemPropertyDescriptor;

	private EObject modelElement;

	private EStructuralFeature feature;

	private ComboViewer combo;

	/**
	 * Constructor for the {@link BooleanWidget}.
	 * 
	 * @param dbc the {@link DataBindingContext} to use
	 * @param editingDomain the {@link EditingDomain} to use
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param modelElement the {@link EObject} for this widget
	 * @param feature the {@link EStructuralFeature} edited by this widget
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
	 * @see org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget#createWidget(org.eclipse.ui.forms.widgets.
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
	 * @see org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean isEditable) {
		combo.getControl().setEnabled(isEditable);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget#bindValue(org.eclipse.core.databinding.observable
	 * .value
	 * .IObservableValue)
	 */
	@Override
	public void bindValue(IObservableValue modelValue, final ControlDecoration controlDecoration) {
		IObservableValue target = ViewersObservables.observeSingleSelection(combo);
		getDataBindingContext().bindValue(target, modelValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget#getControl()
	 */
	@Override
	public Control getControl() {
		return combo.getControl();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.widgets.ECPWidget#dispose()
	 */
	@Override
	public void dispose() {
		combo.getCombo().dispose();
	}

}
