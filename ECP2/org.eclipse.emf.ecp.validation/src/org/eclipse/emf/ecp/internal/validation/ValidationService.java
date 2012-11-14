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
package org.eclipse.emf.ecp.internal.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.ui.common.AbstractCachedTree;
import org.eclipse.emf.ecp.ui.common.CachedTreeNode;
import org.eclipse.emf.ecp.validation.api.IValidationService;

/**
 * Implementation of a validation service.
 * 
 * @author emueller
 *
 */
public final class ValidationService extends AbstractCachedTree<Integer> implements IValidationService {
	
	/**
	 * Tree node that caches the severity of its children.
	 */
	public class CachedSeverityTreeNode extends CachedTreeNode<Integer> {
		
		/**
		 * Constructor.
		 * 
		 * @param severity
		 * 			the initial severity value of this node
		 */
		public CachedSeverityTreeNode(Integer severity) {
			super(severity);
		}

		/**
		 * {@inheritDoc}
		 */
		public void update() {
			
			Collection<Integer> severities = values();
			
			if (severities.size() > 0) {
				setValue(Collections.max(severities));
				return;
			}

			setValue(getDefaultValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void validate(Collection<EObject> eObjects) {
		validate(eObjects, EMPTY_SET);
	}

	/**
	 * {@inheritDoc}
	 */
	public void validate(Collection<EObject> eObjects, Set<? extends Object> excludedObjects) {
		for(EObject eObject : eObjects) {
			validate(eObject, excludedObjects);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void validate(EObject eObject) {
		validate(eObject, EMPTY_SET);		
	}

	/**
	 * {@inheritDoc}
	 */
	public void validate(EObject eObject, Set<? extends Object> excludedObjects) {
		update(eObject, getSeverity(eObject), excludedObjects);
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getSeverity(Object eObject) {
		return getCachedValue(eObject);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Integer getHighestSeverity() {
		return getRootValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getDefaultValue() {
		return Diagnostic.OK;
	}

	private Integer getSeverity(EObject object) {
		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(object);
		Integer newSeverity = findHighestSeverity(diagnostic);
		return newSeverity;
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

	@Override
	public CachedTreeNode<Integer> createdCachedTreeNode(Integer value) {
		return new CachedSeverityTreeNode(value);
	}

}

