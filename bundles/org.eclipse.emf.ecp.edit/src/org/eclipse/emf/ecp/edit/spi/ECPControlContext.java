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
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.spi;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.context.ViewModelContext;

/**
 * Context for a modelelement.
 * 
 * @author helming
 * @author Eugen Neufeld
 */
public interface ECPControlContext {

	/**
	 * Returns the {@link EObject} of this {@link ECPControlContext}.
	 * 
	 * @return the {@link EObject} of this context
	 */
	EObject getModelElement();

	/**
	 * Returns the view context associated with the context.
	 * 
	 * @return the view context
	 */
	ViewModelContext getViewContext();
}
