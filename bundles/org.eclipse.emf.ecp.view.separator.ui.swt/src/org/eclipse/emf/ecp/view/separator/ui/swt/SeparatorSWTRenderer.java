/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.separator.ui.swt;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.SWTRenderer;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.separator.model.VSeparator;

/**
 * The SWT renderer for separator.
 * 
 * @author Jonas
 * 
 */
public class SeparatorSWTRenderer implements CustomSWTRenderer {

	/**
	 * 
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer#getCustomRenderers()
	 */
	public Map<Class<? extends Renderable>, SWTRenderer<?>> getCustomRenderers() {
		Map<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, SWTRenderer<?>> renderers;
		renderers = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, SWTRenderer<?>>() {
			private static final long serialVersionUID = 3710963194739585330L;

			{
				put(VSeparator.class, new SWTSeparatorRenderer());
			}
		};
		return renderers;
	}

}
