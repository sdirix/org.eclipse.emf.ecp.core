/*******************************************************************************
 * Copyright (c) 2011-2016 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * mathias - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spi.common.converter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Mathias Schaefer <mschaefer@eclipsesource.com>
 * @since 1.10
 *
 */
public interface EStructuralFeatureValueConverter {

	/**
	 * Constant value to mark a converter implementation not applicable
	 * for the given EObject/EStructuralFeature combination.
	 */
	double NOT_APPLICABLE = -1d;

	/**
	 * Check whether this converter instance is applicable for the given feature.
	 *
	 * @param eObject (optional)
	 * @param feature the target feature
	 * @param literal the string literal
	 * @return rank of converter (higher value means higher priority)
	 */
	double isApplicable(EObject eObject, EStructuralFeature feature, String literal);

	/**
	 * Convert string to matching model value.
	 *
	 * @param eObject (option, only required for EReferences)
	 * @param feature the target feature
	 * @param literal the string literal
	 * @return converted value
	 */
	Object convertToModelValue(EObject eObject, EStructuralFeature feature, String literal);

}
