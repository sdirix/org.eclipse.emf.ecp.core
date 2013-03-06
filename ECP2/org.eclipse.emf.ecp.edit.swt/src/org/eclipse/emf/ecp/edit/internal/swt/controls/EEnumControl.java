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
package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * This class defines a Control which is used for displaying {@link EStructuralFeature}s which have a enum
 * value.
 * 
 * @author Eugen Neufeld
 * 
 */
public class EEnumControl extends SingleControl {

	private ComboViewer combo;

	/**
	 * Constructor for a eenum control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public EEnumControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
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
		combo.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_swt_enum");
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
