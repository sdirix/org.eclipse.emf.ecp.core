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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

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
	
	public class CachedSeverityTreeNode extends CachedTreeNode<Integer> {
		
		public CachedSeverityTreeNode(Integer data) {
			super(data);
		}

		public void update() {
			
			Collection<Integer> severities = values();
			
			if (severities.size() > 0) {
				setValue(Collections.max(severities));
				return;
			}

			setValue(getDefaultValue());
		}
	}

	public void validate(Collection<EObject> eObjects) {
		for (EObject eObject : eObjects) {
			update(eObject, getSeverity(eObject), EMPTY_SET);
		}
	}

	public void validate(Collection<EObject> eObjects, Set<? extends Class<?>> excludedTypes) {
		validate(eObjects, EMPTY_SET);
	}

	public void validate(EObject eObject) {
		validate(eObject, EMPTY_SET);		
	}

	public void validate(EObject eObject, Set<? extends Class<?>> excludedTypes) {
		update(eObject, getSeverity(eObject), excludedTypes);
	}

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

	public Integer getSeverity(Object eObject) {
		return getCachedValue(eObject);
	}

	public Integer getHighestSeverity() {
		return getRootValue();
	}
}

