/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spi.core.services.mappingprovider;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.common.spi.UniqueSetting;
import org.eclipse.emf.ecp.view.spi.model.VControl;

/**
 * A service to provide {@link UniqueSetting} to {@link VControl} mappings.
 *
 * @author Lucas Koehler
 * @since 1.8
 *
 */
public interface EMFFormsMappingProvider {

	/**
	 * The constant defining the priority that a {@link EMFFormsMappingProvider} is not applicable for a
	 * {@link VControl} and {@link EObject domain object}.
	 */
	double NOT_APPLICABLE = Double.NEGATIVE_INFINITY;

	/**
	 * Returns the mapping of {@link UniqueSetting UniqueSettings} to their sets of {@link VControl VControls} for a
	 * given {@link VControl} and {@link EObject domain object}.
	 *
	 * @param vControl The {@link VControl}
	 * @param domainObject The {@link EObject domain object}
	 * @return The mapping from the {@link UniqueSetting UniqueSettings} to their sets of {@link VControl VControls}
	 */
	Set<UniqueSetting> getMappingFor(VControl vControl, EObject domainObject);

	/**
	 * Returns a double indicating if and how well this {@link EMFFormsMappingProvider} is applicable for the given
	 * {@link VControl} and {@link EObject domain object}.
	 *
	 * @param vControl The given {@link VControl}
	 * @param domainObject The {@link EObject domain object}
	 * @return The floating point value indicating the applicability
	 */
	double isApplicable(VControl vControl, EObject domainObject);
}
