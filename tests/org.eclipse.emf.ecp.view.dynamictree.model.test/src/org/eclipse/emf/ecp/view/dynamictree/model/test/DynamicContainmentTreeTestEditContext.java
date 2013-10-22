/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 */
package org.eclipse.emf.ecp.view.dynamictree.model.test;

import java.util.Locale;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

public class DynamicContainmentTreeTestEditContext implements ECPControlContext {

	private final EObject root;
	private final EObject data;
	private final ViewModelContext viewModelContext;
	private final EMFDataBindingContext dataBindingContext;

	public DynamicContainmentTreeTestEditContext(EObject root, ViewModelContext viewModelContext) {
		this.root = root;
		this.viewModelContext = viewModelContext;
		data = root;
		final Realm realm = SWTObservables.getRealm(Display.getCurrent());
		dataBindingContext = new EMFDataBindingContext(realm);
	}

	public DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}

	public EditingDomain getEditingDomain() {
		return null;
	}

	public EObject getModelElement() {
		return data != null ? data : root;
	}

	public void addModelElement(EObject eObject, EReference eReference) {

	}

	public EObject getNewElementFor(EReference eReference) {
		return null;
	}

	public EObject getExistingElementFor(EReference eReference) {
		return null;
	}

	public void openInNewContext(EObject eObject) {

	}

	public boolean isRunningAsWebApplication() {
		return false;
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	public ViewModelContext getViewContext() {
		return viewModelContext;
	}

	public ECPControlContext createSubContext(EObject eObject) {
		return new DynamicContainmentTreeTestEditContext(eObject, viewModelContext);
	}

}
