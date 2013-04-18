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
package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.AbstractControl;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * This class defines a SWTCOntrol which is an abstract class defining an {@link AbstractControl} for SWT.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class SWTControl extends AbstractControl<Composite> {

	/**
	 * RAP theming variable to set
	 */
	protected static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant";

	protected static final String VALIDATION_ERROR_ICON = "icons/validation_error.png";//$NON-NLS-1$

	private IObservableValue modelValue;

	/**
	 * Constructor for a swt control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public SWTControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	/**
	 * Returns the {@link DataBindingContext} set in the constructor.
	 * 
	 * @return the {@link DataBindingContext}
	 */
	protected DataBindingContext getDataBindingContext() {
		return getModelElementContext().getDataBindingContext();
	}

	/**
	 * The model value used for databinding. It is either the set one or the calculated.
	 * 
	 * @return the {@link IObservableValue}
	 */
	protected IObservableValue getModelValue() {
		if (modelValue != null) {
			return modelValue;
		}
		IObservableValue model = EMFEditObservables.observeValue(getModelElementContext().getEditingDomain(),
			getModelElementContext().getModelElement(), getStructuralFeature());
		return model;
	}

	/**
	 * Allows the user to set the {@link IObservableValue} to use in the control during databinding.
	 * 
	 * @param modelValue the set {@link IObservableValue}
	 */
	public void setObservableValue(IObservableValue modelValue) {
		this.modelValue = modelValue;
	}

	/**
	 * A helper method which creates a button for an action on a composite.
	 * 
	 * @param action the action to create a button for
	 * @param composite the composite to create the button onto
	 * @return the created button
	 */
	protected Button createButtonForAction(final Action action, final Composite composite) {
		Button selectButton = new Button(composite, SWT.PUSH);
		selectButton.setImage(action.getImageDescriptor().createImage());
		selectButton.setToolTipText(action.getToolTipText());
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				action.run();
				composite.layout();
			}

		});
		return selectButton;
	}

	/**
	 * Triggers the control to perform the databinding.
	 */
	protected abstract void bindValue();
}
