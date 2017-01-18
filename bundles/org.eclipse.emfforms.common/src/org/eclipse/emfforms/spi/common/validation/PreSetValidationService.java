/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spi.common.validation;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Pre-set validation service that checks whether a given value
 * complies with constraints defined by a given feature.
 *
 * @since 1.12
 */
public interface PreSetValidationService extends IFeatureConstraint {

	/**
	 * Add a custom {@link IFeatureConstraint}.
	 *
	 * @param eDataType a custom {@link EDataType}
	 * @param constraint the behavioral validation constraint
	 */
	void addConstraintValidator(EDataType eDataType, IFeatureConstraint constraint);

	/**
	 * Validate a given value against loose constraints defined by the {@link EDataType} of the
	 * {@link EStructuralFeature}.
	 *
	 * @param eStructuralFeature the feature that defines any constraints
	 * @param value the value to be validated
	 * @return a {@link Diagnostic} describing any potential errors
	 */
	Diagnostic validateLoose(EStructuralFeature eStructuralFeature, Object value);
}
