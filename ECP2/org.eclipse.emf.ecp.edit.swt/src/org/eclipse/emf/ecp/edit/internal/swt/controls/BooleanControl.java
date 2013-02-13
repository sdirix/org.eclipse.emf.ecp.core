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

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * This class defines a BooleanControl which is used for displaying {@link EStructuralFeature}s which have a Boolean
 * value.
 * 
 * @author Eugen Neufeld
 * 
 */
public class BooleanControl extends SingleControl {

	private Button check;

	/**
	 * Constructor for a boolean control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link EditModelElementContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public BooleanControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, EditModelElementContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
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
