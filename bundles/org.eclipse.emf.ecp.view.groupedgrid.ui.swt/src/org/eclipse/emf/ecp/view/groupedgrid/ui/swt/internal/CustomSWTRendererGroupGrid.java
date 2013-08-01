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
 ******************************************************************************/
package org.eclipse.emf.ecp.view.groupedgrid.ui.swt.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGrid;
import org.eclipse.emf.ecp.ui.view.swt.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * @author Eugen Neufeld
 * 
 */
public class CustomSWTRendererGroupGrid implements CustomSWTRenderer {

	/**
	 * 
	 */
	public CustomSWTRendererGroupGrid() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.CustomSWTRenderer#getCustomRenderers()
	 */
	public Map<Class<? extends Renderable>, SWTRenderer<?>> getCustomRenderers() {
		final Map<Class<? extends Renderable>, SWTRenderer<?>> result = new HashMap<Class<? extends Renderable>, SWTRenderer<?>>();
		result.put(GroupedGrid.class, GroupedGridSWTRenderer.INSTANCE);
		result.put(Control.class, SWTControlRenderer.INSTANCE);
		return result;
	}
}
