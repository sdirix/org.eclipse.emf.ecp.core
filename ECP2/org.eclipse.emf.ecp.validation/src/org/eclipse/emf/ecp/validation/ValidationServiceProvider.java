/*******************************************************************************
 * Copyright (c) 2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.validation;

import java.util.HashMap;
import java.util.Map;

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
	public IValidationService getValidationService(Object obj) {
		
		if (!mapping.containsKey(obj)) {
			IValidationService validationService = new ValidationService();
			mapping.put(obj, validationService);
			return validationService;
		}
		
		return mapping.get(obj);
	}

}
