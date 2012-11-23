/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.validation.connector;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecp.ui.common.AbstractCachedTree;
import org.eclipse.emf.ecp.ui.common.CachedTreeNode;
import org.eclipse.emf.ecp.ui.common.IExcludedObjectsCallback;
import org.eclipse.emf.ecp.validation.api.IValidationService;

/**
 * Implementation of a validation service.
 * 
 * @author emueller
 *
 */
public final class ValidationService extends AbstractCachedTree<Diagnostic> implements IValidationService {
	
	/**
	 * Constructor for the ECP ValidationService.
	 * @param callback to use
	 */
	public ValidationService(IExcludedObjectsCallback callback) {
		super(callback);
	}

	/**
	 * Tree node that caches the severity of its children.
	 */
	public class CachedSeverityTreeNode extends CachedTreeNode<Diagnostic> {
		
		/**
		 * Constructor.
		 * 
		 * @param diagnostic
		 * 			the initial diagnostic containing the severity and validation message
		 */
		public CachedSeverityTreeNode(Diagnostic diagnostic) {
			super(diagnostic);
		}

		/**
		 * {@inheritDoc}
		 */
		public void update() {
			
			Collection<Diagnostic> severities = values();
			
			if (severities.size() > 0) {
				
				Diagnostic mostSevereDiagnostic = values().iterator().next();
				
				for (Diagnostic diagnostic : severities) {
					if (diagnostic.getSeverity() > mostSevereDiagnostic.getSeverity()) {
						mostSevereDiagnostic = diagnostic;
					}
				}
				
				setValue(mostSevereDiagnostic);
				return;
			}

			setValue(getDefaultValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<EObject> validate(Collection<EObject> eObjects) {
		Set<EObject> allAffected=new HashSet<EObject>();
		for(EObject eObject : eObjects) {
			Set<EObject> affected=validate(eObject);
			allAffected.addAll(affected);
		}
		return allAffected;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<EObject> validate(EObject eObject) {
		return update(eObject, getSeverity(eObject));
	}

	/**
	 * {@inheritDoc}
	 */
	public Diagnostic getDiagnostic(Object eObject) {
		return getCachedValue(eObject);
	}

	/**
	 * {@inheritDoc}
	 */
	public Diagnostic getRootDiagnostic() {
		return getRootValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Diagnostic getDefaultValue() {
		return Diagnostic.OK_INSTANCE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CachedTreeNode<Diagnostic> createdCachedTreeNode(Diagnostic diagnostic) {
		return new CachedSeverityTreeNode(diagnostic);
	}

	private Diagnostic getSeverity(EObject object) {
		return Diagnostician.INSTANCE.validate(object);
	}
}

