/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VElement;

/**
 * An ECPValidationSubProcessor can be registered in order to handle special cases in the view model.
 * E.g. a control displaying a table can register itself as the control to be notified if the children of the
 * domainObject have validation errors.
 * 
 * @author Eugen Neufeld
 * 
 */
public interface ECPValidationSubProcessor {

	/**
	 * Process the passed {@link VElement}.
	 * 
	 * @param domainObject the domain {@link EObject}
	 * @param parentRenderable the {@link VElement} to process
	 * @param validationRegistry callback to the {@link ValidationRegistry} to be able to use the
	 *            {@link ValidationRegistry#getDomainToControlMapping(EObject, VElement)} method
	 * @return a map from {@link EObject} to its {@link org.eclipse.emf.ecp.view.model.VControl Controls}
	 */
	Map<EObject, Set<VControl>> processRenderable(EObject domainObject, VElement parentRenderable,
		ValidationRegistry validationRegistry);
}
