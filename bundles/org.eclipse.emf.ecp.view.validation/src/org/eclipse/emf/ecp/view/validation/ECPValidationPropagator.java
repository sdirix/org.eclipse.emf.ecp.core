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

import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * This interface is used to compute and propagate validation results in the view model.
 * 
 * @author jfaltermeier
 * 
 */
public interface ECPValidationPropagator {

	/**
	 * Checks whether this propagator can handle the propagation for the type of the given Renderable.
	 * 
	 * @param renderable the Renderable for which the validation result is to be computed
	 * @return <code>true</code> if this class can handle the propagation, <code>false</code> if the default propagation
	 *         should be used.
	 */
	boolean canHandle(Renderable renderable);

	/**
	 * Computes the validation result for the given {@link Renderable} from its child results.
	 * 
	 * @param renderable the Renderable for which the validation result is to be computed
	 */
	void propagate(Renderable renderable);
}
