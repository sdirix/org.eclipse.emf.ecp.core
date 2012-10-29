/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas Helmin - initial API and implementation
 * Eugen Neufeld - Chnage of API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.editor;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.Collection;
import java.util.Iterator;

/**
 * Context for a modelelement.
 * 
 * @author helming
 * @author Eugen Neufeld
 */
public interface EditorModelelementContext {

	/**
	 * Adds a {@link EditorModelelementContextListener}.
	 * 
	 * @param modelElementContextListener
	 *            the {@link EditorModelelementContextListener}
	 */
	void addModelElementContextListener(EditorModelelementContextListener modelElementContextListener);

	/**
	 * Removes a {@link EditorModelelementContextListener}.
	 * 
	 * @param modelElementContextListener
	 *            the {@link EditorModelelementContextListener}
	 */
	void removeModelElementContextListener(EditorModelelementContextListener modelElementContextListener);

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
	EditorMetamodelContext getMetaModelElementContext();

	/**
	 * Called if the context is not used anymore. Use for cleanup.
	 */
	void dispose();

	/**
	 * Checks whether this {@link EObject} is reachable from this {@link EditorModelelementContext}.
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

	// TODO remove
	/**
	 * Open an Editor.
	 * 
	 * @param o the {@link EObject}
	 * @param source the source
	 */
	@Deprecated
	void openEditor(EObject o, String source);

	/**
	 * Adds a model element to the underlying project.
	 * 
	 * @param eObject the {@link EObject} to add
	 */
	void addModelElement(EObject eObject);

	/**
	 * Checks whether the current {@link EditorModelelementContext} has unsaved changes.
	 * 
	 * @return true if there are unsaved changes
	 */
	boolean isDirty();

	/**
	 * 
	 */
	void save();

	/**
	 * Returns the {@link EObject} of this {@link EditorModelelementContext}.
	 * 
	 * @return the {@link EObject} of this context
	 */
	EObject getModelElement();

}
