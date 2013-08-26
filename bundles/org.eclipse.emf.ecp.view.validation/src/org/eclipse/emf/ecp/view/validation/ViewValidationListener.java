/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;

/**
 * @author jfaltermeier
 * 
 */
public interface ViewValidationListener {

	/**
	 * Returns validation results if the validation severity is higher than {@link Diagnostic#OK}.
	 * 
	 * @param validationResults all diagnostics
	 */
	void onValidationErrors(Set<Diagnostic> validationResults);
}
