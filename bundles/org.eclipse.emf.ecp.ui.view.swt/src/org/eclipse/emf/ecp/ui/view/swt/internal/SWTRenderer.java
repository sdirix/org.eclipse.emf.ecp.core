/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.internal;

import org.eclipse.emf.ecp.internal.ui.view.renderer.ControlRenderer;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.swt.widgets.Control;

/**
 * Common interface for all SWT renderers.
 * 
 * 
 * the type of the SWT control
 */
public interface SWTRenderer<R extends Renderable> extends ControlRenderer<R, Control> {

	/**
	 * Variant constant for indicating RAP controls.
	 */
	static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant";

}
