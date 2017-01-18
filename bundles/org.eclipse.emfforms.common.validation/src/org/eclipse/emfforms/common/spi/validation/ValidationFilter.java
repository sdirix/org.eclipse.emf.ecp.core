/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mat Hansen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.common.spi.validation;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;

/**
 * Filter interface which allows to skip validation for certain {@link EObject}s
 * or to ignore {@link Diagnostic}s reported by EValidators and {@link Validator}s.
 *
 * @author Mat Hansen <mhansen@eclipsesource.com>
 */
public interface ValidationFilter {

	/**
	 * Return true if the given {@link EObject} should NOT be validated.
	 *
	 * @param eObject the {@link EObject} about to be validated
	 * @return true if the given {@link EObject} should be skipped during validation
	 */
	boolean skipValidation(EObject eObject);

	/**
	 * Return true if the given {@link Diagnostic} should be ignored.
	 * Ignored {@link Diagnostic}s will not appear in the output of {@link ValidationService#validate(EObject)}.
	 *
	 * @param eObject the {@link EObject} that has been validated
	 * @param diagnostic the {@link Diagnostic} as a result of the validation of the given {@link EObject}
	 * @return true if the given {@link Diagnostic} should not be included in the output
	 *         of {@link ValidationService#validate(EObject)}
	 */
	boolean ignoreDiagnostic(EObject eObject, Diagnostic diagnostic);

}
