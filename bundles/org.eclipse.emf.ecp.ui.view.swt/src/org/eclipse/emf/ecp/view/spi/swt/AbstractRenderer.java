/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.swt;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * Common super class for renderer.
 * 
 * @author Eugen Neufeld
 * @param <VELEMENT> the {@link VElement} this renderer is applicable for
 * @since 1.3
 * 
 */
public abstract class AbstractRenderer<VELEMENT extends VElement> {
	/**
	 * Variant constant for indicating RAP controls.
	 */
	protected static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant"; //$NON-NLS-1$
	private VELEMENT vElement;
	private ViewModelContext viewModelContext;

	/**
	 * Initialize the control. This can only be called once.
	 * 
	 * @param vElement the {@link VElement} to be rendered
	 * @param viewContext the {@link ViewModelContext} to use
	 */
	public void init(final VELEMENT vElement, final ViewModelContext viewContext) {
		if (this.vElement != null) {
			return;
		}
		this.vElement = vElement;
		this.viewModelContext = viewContext;
	}

	/**
	 * The {@link ViewModelContext} to use.
	 * 
	 * @return the {@link ViewModelContext}
	 */
	protected final ViewModelContext getViewModelContext() {
		return viewModelContext;
	}

	/**
	 * The {@link VElement} instance to use.
	 * 
	 * @return the {@link VElement}
	 */
	public final VELEMENT getVElement() {
		return vElement;
	}

	/**
	 * Disposes all resources used by the renderer.
	 * Don't forget to call super.dispose if overwriting this method.
	 */
	protected void dispose() {
		vElement = null;
		viewModelContext = null;
	}
}
