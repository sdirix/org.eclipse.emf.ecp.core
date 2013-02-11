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

package org.eclipse.emf.ecp.edit;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * This abstract class describes an ECPWidget. This class is used in controls. An {@link ECPWidget} needs an
 * {@link IItemPropertyDescriptor}, an {@link EStructuralFeature} and an {@link EditModelElementContext}.
 * 
 * @param <COMPOSITE> the super type of all composites used to create this widget onto. e.g. SWT should use {@link org.eclipse.swt.Composite} here 
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPWidget<COMPOSITE> {

	private IItemPropertyDescriptor itemPropertyDescriptor;
	private EStructuralFeature feature;
	private EditModelElementContext modelElementContext;

	/**
	 * The constructor for a {@link ECPWidget}.
	 * 
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor}
	 * @param feature the {@link EStructuralFeature}
	 * @param modelElementContext the {@link EditModelElementContext}
	 */
	public ECPWidget(IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		EditModelElementContext modelElementContext) {
		this.itemPropertyDescriptor = itemPropertyDescriptor;
		this.feature = feature;
		this.modelElementContext = modelElementContext;
	}

	/**
	 * Returns the {@link IItemPropertyDescriptor} to use.
	 * 
	 * @return the {@link IItemPropertyDescriptor}
	 */
	protected IItemPropertyDescriptor getItemPropertyDescriptor() {
		return itemPropertyDescriptor;
	}

	/**
	 * Return an {@link EStructuralFeature} to use.
	 * 
	 * @return the {@link EStructuralFeature}
	 */
	protected EStructuralFeature getFeature() {
		return feature;
	}

	/**
	 * Returns the {@link EditModelElementContext} to use.
	 * 
	 * @return the {@link EditModelElementContext}
	 */
	protected EditModelElementContext getModelElementContext() {
		return modelElementContext;
	}

	/**
	 * Create a new Widget.
	 * 
	 * @param composite the parent composite to create the widget onto
	 */
	public abstract void createWidget(final COMPOSITE composite);

	/**
	 * Sets the state of the widget to be either editable or not.
	 * 
	 * @param isEditable whether to set the widget editable
	 */
	public abstract void setEditable(boolean isEditable);


	/**
	 * Called in order to dispose content of the widget.
	 */
	public abstract void dispose();

	//TODO move inside
	/**
	 * Called to set the databinding for this control.
	 * 
	 * @param modelValue the modelValue to use
	 */
	public abstract void bindValue(final IObservableValue modelValue);
}
