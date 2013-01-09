/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.editor;

import org.eclipse.emf.ecp.ui.composites.ICompositeProvider;

/**
 * @author Eugen Neufeld
 * 
 */
public interface IEditorCompositeProvider extends ICompositeProvider {

	/**
	 * Dispose the ModelElement controls.
	 */
	void dispose();

	/**
	 * Triggers live validation of the model attributes.
	 **/
	void updateLiveValidation();

	/**
	 * Set focus to the controls.
	 */
	void focus();

}
