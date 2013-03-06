/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
	 * Open an Editor.
	 * 
	 * @param o the {@link EObject}
	 */
	void openEditor(EObject o);

	/**
	 * Adds a model element to the underlying project.
	 * 
	 * @param eObject the {@link EObject} to add
	 */
	void addModelElement(EObject eObject,EReference eReference);

	/**
	 * Returns the {@link EObject} of this {@link ECPControlContext}.
	 * 
	 * @return the {@link EObject} of this context
	 */
	EObject getModelElement();

	/**
	 * @param eReference
	 *            the reference to create the element for
	 * @return the created EObject, this EObject is not contained in the tree
	 */
	EObject getNewElementFor(EReference eReference);

	EObject getExistingElementFor(EReference eReference);
}
