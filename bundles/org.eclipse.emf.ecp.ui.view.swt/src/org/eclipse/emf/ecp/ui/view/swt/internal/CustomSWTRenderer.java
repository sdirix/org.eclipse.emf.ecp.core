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
package org.eclipse.emf.ecp.ui.view.swt.internal;

import java.util.Map;

import org.eclipse.emf.ecp.view.model.VElement;

/**
 * Interface for contributing custom SWT renderers.
 * 
 * @author emuller
 * 
 */
public interface CustomSWTRenderer {

	/**
	 * Returns a map containing a mapping that specifies which {@link SWTRenderer} should
	 * be used for a certain {@link VElement}.
	 * 
	 * @return a map associating {@link VElement}s with {@link SWTRenderer}s
	 */
	Map<Class<? extends VElement>, SWTRenderer<?>> getCustomRenderers();
}
