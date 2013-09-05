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
package org.eclipse.emf.ecp.edit;

import org.eclipse.emf.common.util.Diagnostic;

/**
 * The {@link ECPControl} is the interface describing a control.
 * 
 * @author Eugen Neufeld
 * 
 */
public interface ECPControl {
	/**
	 * This method should be triggered when this Control is disposed.
	 */
	void dispose();

	/**
	 * Handle live validation.
	 * 
	 * @param diagnostic of type Diagnostic
	 * **/
	void handleValidation(Diagnostic diagnostic);

	/**
	 * Reset the validation status 'ok'.
	 * **/
	void resetValidation();

	/**
	 * Whether a label should be shown for this control.
	 * 
	 * @return true if a label should be created, false otherwise
	 */
	boolean showLabel();

}
