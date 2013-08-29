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
package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.swt.internal.ECPSWTViewRendererImpl;
import org.eclipse.swt.widgets.Composite;

/**
 * Renders a view which displays the attributes of an domain objects.
 * 
 * @author Jonas
 * 
 */
public interface ECPSWTViewRenderer {

	/** Provides access to the stateless renderer. */
	ECPSWTViewRenderer INSTANCE = new ECPSWTViewRendererImpl();

	/**
	 * Creates a view with the attributes of the domain object. The layout of the view can either be describes by a
	 * registered view model, or, if none view model is registered for the domainObject, will be the default layout.
	 * 
	 * @param parent the parent SWT composite to render the view on
	 * @param domainObject The domainObject to show in the view
	 * @return an ECPSWTView providing an interface to the rendered view
	 */
	ECPSWTView render(Composite parent, EObject domainObject);

}
