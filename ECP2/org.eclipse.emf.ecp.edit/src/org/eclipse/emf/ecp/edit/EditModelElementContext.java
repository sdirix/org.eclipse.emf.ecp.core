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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * Context for a modelelement.
 * 
 * @author helming
 * @author Eugen Neufeld
 */
public interface EditModelElementContext {

	/**
	 * Adds a {@link EditModelElementContextListener}.
	 * 
	 * @param modelElementContextListener
	 *            the {@link EditModelElementContextListener}
	 */
	void addModelElementContextListener(EditModelElementContextListener modelElementContextListener);

	/**
	 * Removes a {@link EditModelElementContextListener}.
	 * 
	 * @param modelElementContextListener
	 *            the {@link EditModelElementContextListener}
	 */
	void removeModelElementContextListener(EditModelElementContextListener modelElementContextListener);

	/**
	 * Returns all {@link EObject} in the context, which are of a certain type.
	 * 
	 * @param clazz
	 *            the type
	 * @param association whether to search for associations
	 * @return a {@link Collection} of {@link EObject} Iterator
	 */
	Collection<EObject> getAllModelElementsbyClass(EClass clazz, boolean association);

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
	 * Returns the {@link EditorMetamodelContext}.
	 * 
	 * @return the {@link EditorMetamodelContext}.
	 */
	//FIXME needed?
//	EditorMetamodelContext getMetaModelElementContext();

	/**
	 * Called if the context is not used anymore. Use for cleanup.
	 */
	void dispose();

	/**
	 * Checks whether this {@link EObject} is reachable from this {@link EditModelElementContext}.
	 * 
	 * @param modelElement the {@link EObject} for which to check the reachability
	 * @return true if the object is reachable, else otherwise
	 */
	boolean contains(EObject modelElement);

	/**
	 * @param eReference
	 *            the reference to get the link elements for
	 * @return {@link Iterator} over all available {@link EObject}
	 */
	Iterator<EObject> getLinkElements(EReference eReference);

	/**
	 * Open an Editor.
	 * 
	 * @param o the {@link EObject}
	 * @param source the source
	 */
	void openEditor(EObject o, String source);

	/**
	 * Adds a model element to the underlying project.
	 * 
	 * @param eObject the {@link EObject} to add
	 */
	void addModelElement(EObject eObject);

	/**
	 * Checks whether the current {@link EditModelElementContext} has unsaved changes.
	 * 
	 * @return true if there are unsaved changes
	 */
	boolean isDirty();

	/**
	 * 
	 */
	void save();

	/**
	 * Returns the {@link EObject} of this {@link EditModelElementContext}.
	 * 
	 * @return the {@link EObject} of this context
	 */
	EObject getModelElement();

	
	EditMetaModelContext getMetaModelElementContext();
	
	void getNewModelElement(EReference reference);

}
