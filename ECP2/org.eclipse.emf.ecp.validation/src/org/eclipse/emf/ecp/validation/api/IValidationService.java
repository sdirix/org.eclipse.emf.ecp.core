/*******************************************************************************
 * Copyright (c) 2008-2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.validation.api;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

/**
 * Validation service interface.
 *  
 * @author emueller
 */
public interface IValidationService {
	
	/**
	 * Validates the given {@link EObject}s.
	 * 
	 * @param eObjects
	 * 			a collection of {@link EObject}s to be validated
	 */
	void validate(Collection<EObject> eObjects);
	
	/**
	 * Validates the given {@link EObject}s.
	 * 
	 * @param eObjects
	 * 			a collection of {@link EObject}s to be validated
	 * @param excludedTypes
	 * 			a collection of types which are ignored within the containment hierarchy of an {@link EObject} 
	 */
	void validate(Collection<EObject> eObjects, Set<? extends Class<?>> excludedTypes);
	
	/**
	 * Validates the given {@link EObject}.
	 * 
	 * @param eObject
	 * 			the {@link EObject} to be validated 
	 */
	void validate(EObject eObject);

	/**
	 * Validates the given {@link EObject}.
	 * 
	 * @param eObject
	 * 			the {@link EObject} to be validated
     * @param excludedTypes
	 * 			a collection of types which are ignored within the containment hierarchy of an {@link EObject} 
	 */
	void validate(EObject eObject, Set<? extends Class<?>> excludedTypes);
	
	/**
	 * Returns the severity for the given {@link EObject}.
	 * 
	 * @param eObject
	 * 			the {@link EObject} whose severity should be returned
	 * @return the severity for the given {@link EObject}
	 */
	Integer getSeverity(Object eObject);
	
	/**
	 * Returns the highest severity.
	 * 
	 * @return the highest severity
	 */
	Integer getHighestSeverity();
	
	/**
	 * Removes the severity of the given {@link EObject}.
	 * 
	 * @param eObject
	 * 			the {@link EObject} whose severity should be removed
	 */
	void remove(EObject eObject);

	/**
	 * Removes the severity of the given {@link EObject}.
	 * 
	 * @param eObject
	 * 			the {@link EObject} whose severity should be removed
	 * @param excludedTypes
 	 * 			a collection of types which are ignored within the containment hierarchy of an {@link EObject} 
	 */
	void remove(EObject eObject, Set<? extends Class<?>> excludedTypes);
}
