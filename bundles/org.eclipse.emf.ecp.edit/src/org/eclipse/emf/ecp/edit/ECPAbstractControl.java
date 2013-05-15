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
 ******************************************************************************/
package org.eclipse.emf.ecp.edit;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * The {@link ECPAbstractControl} is the abstract class describing a control.
 * This class provides the necessary common access methods.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPAbstractControl implements ECPControl {

	private final ECPControlContext modelElementContext;
	private final IItemPropertyDescriptor itemPropertyDescriptor;
	private final EStructuralFeature feature;
	private final boolean showLabel;
	private final boolean embedded;

	/**
	 * The Constructor containing all common parameters.
	 * 
	 * @param showLabel whether a label should be created for this control
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor}
	 * @param feature the {@link EStructuralFeature}
	 * @param modelElementContext the {@link ECPControlContext}
	 * @param embedded whether this control will be embedded in another control e.g. multicontrol
	 */
	public ECPAbstractControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		this.feature = feature;
		this.modelElementContext = modelElementContext;
		this.itemPropertyDescriptor = itemPropertyDescriptor;
		this.showLabel = showLabel;
		this.embedded = embedded;
	}

	/**
	 * Returns the {@link ECPControlContext} to use.
	 * 
	 * @return the {@link ECPControlContext}
	 */
	protected ECPControlContext getModelElementContext() {
		return modelElementContext;
	}

	/**
	 * Return the {@link EStructuralFeature} of this control.
	 * 
	 * @return the {@link EStructuralFeature}
	 */
	protected EStructuralFeature getStructuralFeature() {
		return feature;
	}

	/**
	 * Return the {@link IItemPropertyDescriptor} describing this {@link EStructuralFeature}.
	 * 
	 * @return the {@link IItemPropertyDescriptor}
	 */
	protected IItemPropertyDescriptor getItemPropertyDescriptor() {
		return itemPropertyDescriptor;
	}

	/**
	 * Whether this control should be editable.
	 * 
	 * @return true if the {@link IItemPropertyDescriptor#canSetProperty(Object)} returns true, false otherwise
	 */
	protected boolean isEditable() {
		return itemPropertyDescriptor.canSetProperty(modelElementContext.getModelElement());
	}

	/**
	 * Checks whether this {@link EStructuralFeature} has an explicit unset state.
	 * 
	 * @return true if {@link EStructuralFeature#isUnsettable()} is true, false otherwise
	 */
	protected boolean hasUnsetState() {
		return feature.isUnsettable();
	}

	/**
	 * Whether a control is embedded. An embedded control can be rendered in an other fashion then an not embedded
	 * version.
	 * 
	 * @return true if the control is embedded in another control
	 */
	protected boolean isEmbedded() {
		return embedded;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean showLabel() {
		return showLabel;
	}

}
