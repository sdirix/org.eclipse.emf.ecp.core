/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.validation;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;

/**
 * Service for passing input to the validation view.
 * 
 * @author jfaltermeier
 * 
 */
public interface ECPValidationViewService {

	/**
	 * Passes the given diagnostic to all registered {@link ECPValidationViewServiceListener}s.
	 * 
	 * @param diagnostic the diagnostic to display
	 */
	void setInput(Diagnostic diagnostic);

	/**
	 * Passes the given diagnostics to all registered {@link ECPValidationViewServiceListener}s.
	 * 
	 * @param diagnostic the diagnostics to display
	 */
	void setInput(Diagnostic[] diagnostic);

	/**
	 * Passes the given diagnostics to all registered {@link ECPValidationViewServiceListener}s.
	 * 
	 * @param diagnostic the diagnostics to display
	 */
	void setInput(List<Diagnostic> diagnostic);

	/**
	 * Registers a listener that gets informed whenever the input changes.
	 * 
	 * @param listener the listener to be registered
	 */
	void register(ECPValidationViewServiceListener listener);

	/**
	 * Deregisters a listener.
	 * 
	 * @param listener the listener to be deregistered
	 */
	void deregister(ECPValidationViewServiceListener listener);

	/**
	 * Listener interface for getting informed on input changes of the validation view.
	 * 
	 * @author jfaltermeier
	 * 
	 */
	public interface ECPValidationViewServiceListener {
		/**
		 * Called whenever the input changes.
		 * 
		 * @param diagnostic the diagnostic. May be an array a list or a single Diagnostic.
		 */
		void inputChanged(Object diagnostic);
	}

}
