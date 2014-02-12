/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.validation;

import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecp.view.spi.context.ViewModelService;

/**
 * @author Eugen
 * 
 */
public interface ValidationService extends ViewModelService {
	/**
	 * Adds a validation provider to the list of known validation providers.
	 * 
	 * @param validationProvider the {@link ValidationProvider} to add
	 */
	void addValidationProvider(ValidationProvider validationProvider);

	/**
	 * Removes a validation provider from the list of known validation providers.
	 * 
	 * @param validationProvider the {@link ValidationProvider} to remove
	 */
	void removeValidationProvider(ValidationProvider validationProvider);

	/**
	 * Registers a listener that will receive {@link Diagnostic}s with severity higher than {@link Diagnostic#OK}. After
	 * registration the listener's {@link ViewValidationListener#onNewValidation(Set)} will be called with current
	 * results.
	 * 
	 * @param listener the listener to register
	 */
	public void registerValidationListener(ViewValidationListener listener);

	/**
	 * Deregisters the given listener.
	 * 
	 * @param listener the listener to deregister
	 */
	public void deregisterValidationListener(ViewValidationListener listener);
}
