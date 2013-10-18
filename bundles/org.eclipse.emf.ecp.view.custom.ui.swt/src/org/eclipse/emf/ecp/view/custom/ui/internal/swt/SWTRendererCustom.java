/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.ecp.view.custom.ui.internal.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ui.view.custom.swt.CustomControlSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.SWTRenderer;
import org.eclipse.emf.ecp.view.custom.model.VCustomControl;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * This registers the {@link CustomControlSWTRenderer} as the renderer for {@link VCustomControl} model elements.
 * 
 * @author Eugen Neufeld
 * 
 */
public final class SWTRendererCustom implements CustomSWTRenderer {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer#getCustomRenderers()
	 */
	public Map<Class<? extends Renderable>, SWTRenderer<?>> getCustomRenderers() {
		final Map<Class<? extends Renderable>, SWTRenderer<?>> map = new HashMap<Class<? extends Renderable>, SWTRenderer<?>>();
		map.put(VCustomControl.class, new CustomControlSWTRenderer());
		return map;
	}

}
