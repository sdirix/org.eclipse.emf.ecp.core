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
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

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
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public BooleanControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	@Override
	protected void fillControlComposite(Composite composite) {
		check = new Button(composite, SWT.CHECK);
		check.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_boolean"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEditable(boolean isEditable) {
		check.setEnabled(isEditable);
	}

	@Override
	public void dispose() {
		check.dispose();
		super.dispose();
	}

	@Override
	public Binding bindValue() {
		IObservableValue targetValue = SWTObservables.observeSelection(check);
		return getDataBindingContext().bindValue(targetValue, getModelValue());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetLabelText()
	 */
	@Override
	protected String getUnsetLabelText() {
		return ControlMessages.BooleanControl_NoBooleanSetClickToSetBoolean;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetButtonTooltip()
	 */
	@Override
	protected String getUnsetButtonTooltip() {
		return ControlMessages.BooleanControl_UnsetBoolean;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getControlForTooltip()
	 */
	@Override
	protected Control[] getControlsForTooltip() {
		return new Control[] { check };
	}
}
