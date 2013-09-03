/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.internal;

import java.util.Locale;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPControlHelper;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Jonas
 * 
 */
public class DefaultControlContext implements ECPControlContext {

	private final EObject eObject;
	private final EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();
	private final ViewModelContextImpl viewContext;
	private final View view;

	/**
	 * @param eObject the domain object
	 * @param view the view model
	 */
	public DefaultControlContext(EObject eObject, View view) {
		this.eObject = eObject;
		this.view = view;
		viewContext = new ViewModelContextImpl(view, getModelElement());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#createSubContext(org.eclipse.emf.ecore.EObject)
	 */
	public ECPControlContext createSubContext(EObject eObject) {
		return new DefaultControlContext(eObject, view);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#getDataBindingContext()
	 */
	public DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#getEditingDomain()
	 */
	public EditingDomain getEditingDomain() {
		return AdapterFactoryEditingDomain.getEditingDomainFor(eObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#getModelElement()
	 */
	public EObject getModelElement() {
		return eObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#addModelElement(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EReference)
	 */
	public void addModelElement(EObject eObject, EReference eReference) {
		ECPControlHelper.addModelElementInReference(getModelElement(), eObject, eReference, getEditingDomain());

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#getNewElementFor(org.eclipse.emf.ecore.EReference)
	 */
	public EObject getNewElementFor(EReference eReference) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#getExistingElementFor(org.eclipse.emf.ecore.EReference)
	 */
	public EObject getExistingElementFor(EReference eReference) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#openInNewContext(org.eclipse.emf.ecore.EObject)
	 */
	public void openInNewContext(EObject eObject) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#isRunningAsWebApplication()
	 */
	public boolean isRunningAsWebApplication() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#getLocale()
	 */
	public Locale getLocale() {
		return Locale.getDefault();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#getViewContext()
	 */
	public ViewModelContext getViewContext() {
		return viewContext;
	}

}
