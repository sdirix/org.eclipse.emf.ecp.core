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
package org.eclipse.emf.ecp.edit.internal.swt.actions;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.action.Action;

/**
 * An abstract action used by ecp.
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPSWTAction extends Action{
	private ECPControlContext modelElementContext;
	private EStructuralFeature feature;
	private IItemPropertyDescriptor itemPropertyDescriptor;

	/**
	 * The constructor of all ecp actions.
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 */
	public ECPSWTAction(ECPControlContext modelElementContext, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature) {
		this.modelElementContext = modelElementContext;
		this.feature = feature;
		this.itemPropertyDescriptor = itemPropertyDescriptor;
	}
	/**
	 * The set {@link ECPControlContext}.
	 * @return the {@link ECPControlContext}
	 */
	protected ECPControlContext getModelElementContext() {
		return modelElementContext;
	}
	/**
	 * The set {@link EStructuralFeature}.
	 * @return the {@link EStructuralFeature}
	 */
	protected EStructuralFeature getFeature() {
		return feature;
	}
	/**
	 * The set {@link IItemPropertyDescriptor}.
	 * @return the {@link IItemPropertyDescriptor}
	 */
	protected IItemPropertyDescriptor getItemPropertyDescriptor() {
		return itemPropertyDescriptor;
	}
}
