/*******************************************************************************
 * Copyright (c) 2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.validation.connector;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.ui.common.IExcludedObjectsCallback;
import org.eclipse.emf.ecp.validation.api.IValidationService;
import org.eclipse.emf.ecp.validation.api.IValidationServiceProvider;

/**
 * Validation service provider.
 * 
 * @author emueller
 */
public class ValidationServiceProvider implements IValidationServiceProvider {
	
	private Map<Object, IValidationService> mapping;
	
	/**
	 * Constructor.
	 */
	public ValidationServiceProvider() {
		mapping = new HashMap<Object, IValidationService>();
	}

	/**
	 * {@inheritDoc}
	 */
	public IValidationService getValidationService(Object object) {
		if(!ECPProject.class.isInstance(object)){
			return null;
		}
		final ECPProject project=(ECPProject)object;
		if (!mapping.containsKey(project)) {
			IValidationService validationService = new ValidationService(new IExcludedObjectsCallback() {
				
				public boolean isExcluded(Object object) {
					return project.isModelRoot(object);
				}
			});
			mapping.put(project, validationService);
			return validationService;
		}
		
		return mapping.get(project);
	}

}
