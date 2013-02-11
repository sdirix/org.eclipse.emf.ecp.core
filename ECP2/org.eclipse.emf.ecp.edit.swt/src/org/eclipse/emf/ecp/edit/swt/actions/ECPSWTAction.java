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
package org.eclipse.emf.ecp.edit.swt.actions;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.editor.util.ECPAction;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.jface.action.Action;

/**
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPSWTAction extends Action implements ECPAction{
	private EditModelElementContext modelElementContext;
	private EStructuralFeature feature;
	private IItemPropertyDescriptor itemPropertyDescriptor;

	public ECPSWTAction(EditModelElementContext modelElementContext, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature) {
		this.modelElementContext = modelElementContext;
		this.feature = feature;
		this.itemPropertyDescriptor = itemPropertyDescriptor;
	}
	
	public EditModelElementContext getModelElementContext() {
		return modelElementContext;
	}

	public EStructuralFeature getFeature() {
		return feature;
	}

	public IItemPropertyDescriptor getItemPropertyDescriptor() {
		return itemPropertyDescriptor;
	}
}
