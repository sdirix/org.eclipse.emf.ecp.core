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
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public interface ECPProject extends ECPElement, ECPModelContext, ECPRepositoryAware, ECPPropertiesAware, ECPCloseable,
	ECPDeletable, IEditingDomainProvider, IAdaptable {
	public static final String TYPE = "Project";

	// TODO check whether not to remove
	public EList<EObject> getElements();

	/**
	 * Returns <code>true</code> if this project is shared with a {@link ECPRepository repository}, <code>false</code>
	 * otherwise. Same as calling <code>getRepository() != null</code>.
	 */
	public boolean isShared();

	/**
	 * filter for {@link EPackage}s that are not supported by the provider.
	 * 
	 * @return {@link Collection} of unsupported {@link EPackage}s
	 */
	public Collection<EPackage> getUnsupportedEPackages();

	/**
	 * Set the filtered {@link EPackage}s. New model elements can only be created from {@link EPackage}s contained in
	 * the
	 * filteredPackages and the {@link #setFilteredEClasses(Set)}.
	 * 
	 * @param filteredPackages
	 */
	public void setFilteredPackages(Set<EPackage> filteredPackages);

	/**
	 * Get the currrent filtered {@link EPackage}s. If no filter is set, then all known {@link EPackage}s are returned.
	 * 
	 * @return {@link Set} of {@link EPackage}s that should be available, or all known EPackages if no filter
	 */
	public Set<EPackage> getFilteredPackages();

	/**
	 * Get the current filtered {@link EClass}es. If no filter is set, then an empty {@link Set} is returned
	 * 
	 * @return {@link Set} of {@link EClass}es that should be available, or empty if no filter
	 */
	Set<EClass> getFilteredEClasses();

	/**
	 * Set the filtered {@link EClass}es.
	 * 
	 * @param filteredEClasses the classes that should be available in new model element dialog
	 */
	void setFilteredEClasses(Set<EClass> filteredEClasses);

	/**
	 * Get all possible {@link EObject}s from the provider so that a reference can be created for the modelElement based
	 * on the type of the {@link EReference}.
	 * 
	 * @param modelElement
	 *            - the {@link EObject} for which the references should be set.
	 * @param eReference
	 *            - the {@link EReference} to be set.
	 * @return {@link Iterator} over all {@link EObject} that are suitable as reference
	 */
	public Iterator<EObject> getLinkElements(EObject modelElement, EReference eReference);

	public void doSave();

	public boolean isDirty();

	public boolean hasAutosave();

	public void delete(EObject... eObjects);

	public void delete(Collection<EObject> eObjects);

	// /**
	// * @param ecpRepository
	// */
	// void setRepository(ECPRepository ecpRepository);

	/**
	 * @param eObject the {@link EObject} to add
	 */
	public void addModelElement(EObject eObject);
}
