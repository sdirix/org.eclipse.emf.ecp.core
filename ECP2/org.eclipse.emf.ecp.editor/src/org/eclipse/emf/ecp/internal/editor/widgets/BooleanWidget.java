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
 * The widget implementation for editing a Boolean value.
 * 
 * @author Eugen Neufeld
 */
public class BooleanWidget extends ECPAttributeWidget {

	/**
	 * Constructor for the {@link BooleanWidget}.
	 * 
	 * @param dbc the {@link DataBindingContext} to use
	 * @param editingDomain the {@link EditingDomain} to use
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
		getDataBindingContext().bindValue(targetValue, modelValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean isEditable) {
		check.setEnabled(isEditable);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.widgets.ECPAttributeWidget#getControl()
	 */
	@Override
	public Control getControl() {
		return check;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.widgets.ECPWidget#dispose()
	 */
	@Override
	public void dispose() {
		check.dispose();
	}
}
