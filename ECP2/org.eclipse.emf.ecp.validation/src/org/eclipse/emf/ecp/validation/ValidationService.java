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
package org.eclipse.emf.ecp.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.validation.api.IValidationService;

/**
 * Implementation of a validation service.
 * 
 * @author emueller
 *
 */
public final class ValidationService implements IValidationService {

	private Map<Object, Integer> severityCache = new HashMap<Object, Integer>();
	private Integer highestSeverity = Diagnostic.OK;
	
	private static Set<? extends Class<?>> emptySet = Collections.emptySet();
	
	/**
	 * Private constructor.
	 */
	private ValidationService() {

	}

	/**
	 * {@inheritDoc}
	 */
	public void validate(Collection<EObject> eObjects) {
		for (EObject eObject : eObjects) {
			validate(eObject);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void validate(Collection<EObject> eObjects, Set<? extends Class<?>> excludedTypes) {
		validate(eObjects, emptySet);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void validate(EObject eObject) {
		validate(eObject, emptySet);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void validate(EObject eObject, Set<? extends Class<?>> excludedTypes) {
		
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate((EObject) eObject);
		Integer severity = findHighestSeverity(diagnostic);
		
		severityCache.put(eObject, severity);

		if (highestSeverity < severity) {
			highestSeverity = severity;
		}
		
		// revalidate up to root element
		EObject parent = ((EObject) eObject).eContainer();
		
		while (parent != null && !isExcludedType(excludedTypes, parent.getClass())) {
			
			Integer parentSeverity = severityCache.get(parent);
			
			if (parentSeverity != null && parentSeverity <= severity) {
				break;
			}
			
			severityCache.put(parent, severity);
			parent = parent.eContainer();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getHighestSeverity() {
		return highestSeverity;
	}

	/**
	 * {@inheritDoc}
	 */
	public void putSeverity(Object eObject, Integer severity) {
		severityCache.put(eObject, severity);
	}
		
	/**
	 * {@inheritDoc}
	 */
	public Integer getSeverity(Object eObject){
		return severityCache.get(eObject);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeSeverityFor(EObject eObject, Set<? extends Class<?>> excludedTypes) {
		
		Integer lostSeverity = severityCache.get(eObject);
		severityCache.remove(eObject);
		
		if (lostSeverity >= highestSeverity) {
			highestSeverity = computeNewSeverity(excludedTypes);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeSeverity(EObject eObject) {
		removeSeverityFor(eObject, emptySet);
	}

	private Integer computeNewSeverity(Set<? extends Class<?>> excludedTypes) {
		Set<?> newKeySet = new LinkedHashSet<Object>(severityCache.keySet());
		
		for (Object obj : severityCache.keySet()) {
			if (isExcludedType(excludedTypes, obj.getClass())) {
				newKeySet.remove(obj);
			}
		}
		
		Vector<Integer> severities = new Vector<Integer>(newKeySet.size());
		
		for (Object key : newKeySet) {
			severities.add(severityCache.get(key));
		}
		
		return Collections.max(severities);
	}
	
	private Integer findHighestSeverity(Diagnostic diagnostic) {
		
		Integer severity = diagnostic.getSeverity();
		
		for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
			if (childDiagnostic.getSeverity() >= severity) {
				severity = childDiagnostic.getSeverity();
			}
		}
		
		return severity;
	}
	
	private boolean isExcludedType(Set<? extends Class<?>> excludedTypes, Class<?> clazz) {
		for (Class<?> type : excludedTypes) {
			if (type.isAssignableFrom(clazz)) {
				return true;
			}
		}
		
		return false;
	}
}

