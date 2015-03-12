/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spi.swt.core;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.swt.AbstractAdditionalSWTRenderer;

/**
 * The EMFFormsAdditionalRendererService encapsulates the tester and the access to the
 * {@link AbstractAdditionalSWTRenderer}.
 *
 * @author Eugen Neufeld
 *
 * @param <VELEMENT>
 */
public interface EMFFormsAdditionalRendererService<VELEMENT extends VElement> {

	/**
	 * Check whether the provided {@link VElement} can be rendered by the {@link AbstractAdditionalSWTRenderer} provided
	 * by {@link #getRendererInstance(VElement, ViewModelContext)}.
	 *
	 * @param vElement The {@link VElement} to check
	 * @return true if the AbstractAdditionalSWTRenderer fits, false otherwise
	 */
	boolean isApplicable(VElement vElement);

	/**
	 * Returns a renderer.
	 *
	 * @param vElement The {@link VElement} to create the renderer instance for
	 * @param viewModelContext The {@link ViewModelContext} to use for the renderer instance
	 * @return The AbstractAdditionalSWTRenderer
	 */
	AbstractAdditionalSWTRenderer<VELEMENT> getRendererInstance(VELEMENT vElement, ViewModelContext viewModelContext);
}
