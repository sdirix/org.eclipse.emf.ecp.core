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
package org.eclipse.emf.ecp.edit.spi;

import java.util.Locale;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * Context for a modelelement.
 * 
 * @author helming
 * @author Eugen Neufeld
 */
public interface ECPControlContext {

	/**
	 * Return the {@link DataBindingContext} to use in the editor.
	 * 
	 * @return the {@link DataBindingContext} to use
	 */
	DataBindingContext getDataBindingContext();

	/**
	 * Returns the editing domain.
	 * 
	 * @return the editing domain
	 */
	EditingDomain getEditingDomain();

	/**
	 * Returns the {@link EObject} of this {@link ECPControlContext}.
	 * 
	 * @return the {@link EObject} of this context
	 */
	EObject getModelElement();

	/**
	 * Adds a model element to the {@link EReference}.
	 * 
	 * @param eObject the {@link EObject} to add
	 * @param eReference the {@link EReference} to add the {@link EObject} to
	 */
	void addModelElement(EObject eObject, EReference eReference);

	/**
	 * Returns a new {@link EObject} that is suitable for the passed {@link EReference}.
	 * 
	 * @param eReference
	 *            the reference to create the element for
	 * @return the created EObject, this EObject is not contained in the tree
	 */
	EObject getNewElementFor(EReference eReference);

	/**
	 * Returns an existing {@link EObject} that is suitable for the passed {@link EReference}.
	 * 
	 * @param eReference the {@link EReference} to find an existing Element for
	 * @return an existing {@link EObject} that can be used in this {@link EReference}, the returned EObject must
	 *         already be in the containment tree
	 */
	EObject getExistingElementFor(EReference eReference);

	/**
	 * Opens an {@link EObject} in a new context.
	 * 
	 * @param eObject the {@link EObject} to open in a new context
	 */
	void openInNewContext(EObject eObject);

	/**
	 * Whether the current environment is a web environment (e.g. RAP) or not.
	 * 
	 * @return true if the current environment is a web environment
	 */
	boolean isRunningAsWebApplication();

	/**
	 * Returns the locale for this context.
	 * 
	 * @return the locale for this context
	 */
	Locale getLocale();

	/**
	 * Returns the view context associated with the context.
	 * 
	 * @return the view context
	 */
	ViewModelContext getViewContext();

	/**
	 * Initializes a context for a given {@link EObject} that is based on the current context.
	 * 
	 * @param eObject the {@link EObject} for which to initialize a sub context
	 * @return the initalized sub context
	 */
	ECPControlContext createSubContext(EObject eObject);
}
