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
 * Convenience adapter implementation for a {@link RenderingResultDelegator} that does nothing.
 * 
 * 
 * @author emueller
 * 
 */
public class RenderingResultDelegatorAdapter implements RenderingResultDelegator {

	public void enable(boolean shouldEnable) {
	}

	public void show(boolean shouldShow) {
	}

	public void layout() {
	}

	public void cleanup() {
	}

	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
	}
}
