/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;

/**
 * 
 * 
 * @author emueller
 * 
 */
public interface RenderingResultDelegator {

	/**
	 * Handles the enabled state of the renderer result the
	 * delegator is delegating to.
	 * 
	 * @param shouldEnable
	 *            {@code true}, if the delegator should enable its rendered result, {@code false} otherwise
	 */
	void enable(boolean shouldEnable);

	/**
	 * Handles the show state of the renderer result the
	 * delegator is delegating to.
	 * 
	 * @param shouldShow
	 *            {@code true}, if the delegator should show its rendered result, {@code false} otherwise
	 */
	void show(boolean shouldShow);

	/**
	 * Re-layouts the renderer result.
	 */
	void layout();

	/**
	 * Encapsulates dispose functionality.
	 */
	void cleanup();

	/**
	 * 
	 * 
	 * @param affectedObjects
	 *            the {@link EObject}s
	 */
	void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects);
}
