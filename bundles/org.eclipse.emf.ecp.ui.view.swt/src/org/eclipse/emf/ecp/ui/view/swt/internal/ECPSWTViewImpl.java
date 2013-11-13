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
package org.eclipse.emf.ecp.ui.view.swt.internal;

import org.eclipse.emf.ecp.internal.ui.view.RendererContext;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Jonas
 * 
 */
public class ECPSWTViewImpl implements ECPSWTView {

	private final Composite composite;
	private final RendererContext<?> rendererContext;
	private final ViewModelContext viewContext;

	/**
	 * @param composite the composite containing the view
	 * @param rendererContext the rendererContext of the view
	 * @param viewContext the view context of the view
	 */
	public ECPSWTViewImpl(Composite composite, RendererContext<?> rendererContext, ViewModelContext viewContext) {
		this.composite = composite;
		this.rendererContext = rendererContext;
		this.viewContext = viewContext;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.ECPSWTView#getSWTControl()
	 */
	public Control getSWTControl() {
		return composite;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.ECPSWTView#dispose()
	 */
	public void dispose() {
		rendererContext.dispose();
		viewContext.dispose();

	}

}
