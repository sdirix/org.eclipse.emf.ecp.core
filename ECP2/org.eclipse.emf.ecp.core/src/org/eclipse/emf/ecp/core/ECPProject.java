/*******************************************************************************
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.ecp.core;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.util.ECPCloseable;
import org.eclipse.emf.ecp.core.util.ECPDeletable;
import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPPropertiesAware;
import org.eclipse.emf.ecp.core.util.ECPRepositoryAware;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;

import org.eclipse.core.runtime.IAdaptable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * This class describes a Project. A project has a name, a label. It has zero or one {@link ECPRepository} and one
 * {@link ECPProvider}.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 * @author Jonas Helming
 */
public interface ECPProject extends ECPElement, ECPModelContext, ECPRepositoryAware, ECPPropertiesAware, ECPCloseable,
	ECPDeletable, IEditingDomainProvider, IAdaptable {
	/**
	 * The type of the ECPElement.
	 */
	String TYPE = "Project";

	/**
	 * Returns the list of the direct content objects; each is of type Object.
	 * The contents may be directly modified.
	 * Adding an object will remove it from the previous container;
	 * 
	 * @return A list of {@link Object}
	 */
	EList<Object> getElements();

	/**
	 * Returns a collection of {@link EPackage}s which are not supported by the provider. EObjects from these packages
	 * cannot be created within the project.
	 * 
	 * @return {@link Collection} of unsupported {@link EPackage}s
	 */
	Collection<EPackage> getUnsupportedEPackages();

	/**
	 * Set the visible {@link EPackage}s. New model elements can only be created from {@link EPackage}s contained in
	 * the visiblePackages and the {@link #setVisibleEClasses(Set)}.
	 * 
	 * @param visiblePackages the {@link EPackage}s to be visible
	 */
	void setVisiblePackages(Set<EPackage> visiblePackages);

	/**
	 * Get the currently visible {@link EPackage}s. If no filter is set, then all known {@link EPackage}s are returned.
	 * 
	 * @return {@link Set} of {@link EPackage}s that should be available, or all known EPackages
	 */
	Set<EPackage> getVisiblePackages();

	/**
	 * Get the currently visible {@link EClass}es. If no visible {@link EClass}es are set, then an empty {@link Set} is
	 * returned.
	 * 
	 * @return {@link Set} of {@link EClass}es that should be available, or empty.
	 */
	Set<EClass> getVisibleEClasses();

	/**
	 * Set the visible {@link EClass}es.
	 * 
	 * @param visibleEClasses the classes that should be available
	 */
	void setVisibleEClasses(Set<EClass> visibleEClasses);

	/**
	 * Get all possible {@link EObject}s from the provider to which a reference can be added from a certain
	 * {@link EObject} based
	 * on the type of the {@link EReference}.
	 * 
	 * @param eObject
	 *            - the {@link EObject} for which the reference should be set.
	 * @param eReference
	 *            - the {@link EReference} to be set.
	 * @return {@link Iterator} over all {@link EObject} that can be added as a reference
	 */
	Iterator<EObject> getReferenceCandidates(EObject eObject, EReference eReference);

	/**
	 * Saves the properties, such as visible packages or the name of the project into the workspace.
	 */
	void saveProperties();

	/**
	 * Saves the currently pending changes of the mode. This method delegates to the provider.
	 */
	void saveModel();

	/**
	 * Checks whether the model, associated with this project is dirty.
	 * 
	 * @return true if model is dirty, false otherwise
	 */
	boolean isModelDirty();

	/**
	 * Deletes a collection of {@link EObject}s by delegating the task to the provider.
	 * 
	 * @param eObjects the collection of {@link EObject}s to delete
	 */
	void delete(Collection<EObject> eObjects);

	/**
	 * This method checks whether the provided object is the model root of the project.
	 * 
	 * @param object the object to check
	 * @return true if the object is the root of the model of this project, false otherwise
	 */
	boolean isModelRoot(Object object);
}
