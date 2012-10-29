/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
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

import org.eclipse.emf.ecp.ui.common.ICompositeProvider;

/**
 * @author Eugen Neufeld
 * 
 */
public interface IEditorCompositeProvider extends ICompositeProvider {

	/**
	 * 
	 */
	void dispose();

	/**
	 * 
	 */
	void updateLiveValidation();

	/**
	 * 
	 */
	void focus();

}
