/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.group.ui.swt.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ui.view.swt.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.group.model.Group;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * The Class SWTRendererGroup.
 */
public class SWTRendererGroup implements CustomSWTRenderer {

	/**
	 * Instantiates a new SWT renderer group.
	 */
	public SWTRendererGroup() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.CustomSWTRenderer#getCustomRenderers()
	 */
	public Map<Class<? extends Renderable>, SWTRenderer<?>> getCustomRenderers() {
		final Map<Class<? extends Renderable>, SWTRenderer<?>> map = new HashMap<Class<? extends Renderable>, SWTRenderer<?>>();
		map.put(Group.class, SWTGroupRenderer.INSTANCE);
		return map;
	}

}
