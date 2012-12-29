/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 * Eugen Neufeld - JavaDoc
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.core.util;

/**
 * This interface is used on classes that can be disposed.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public interface ECPDisposable {
	/**
	 * Whether this instance is already disposed.
	 * 
	 * @return true if already disposed, false otherwise.
	 */
	boolean isDisposed();

	/**
	 * Disposes the current instance.
	 */
	void dispose();

	/**
	 * Adds a {@link DisposeListener} to this instance.
	 * 
	 * @param listener the listener to add
	 */
	void addDisposeListener(DisposeListener listener);

	/**
	 * Removed a {@link DisposeListener} from this instance.
	 * 
	 * @param listener the listener to remove
	 */
	void removeDisposeListener(DisposeListener listener);

	/**
	 * This interface defines a listener that gets notified when an object is disposed.
	 * 
	 * @author Eike Stepper
	 */
	// TODO needed or use ObserverBus?
	public interface DisposeListener {
		/**
		 * Callback method being used to notify listeners about a dispose.
		 * 
		 * @param disposable the object being disposed
		 * @throws Exception is thrown when something goes wrong
		 */
		void disposed(ECPDisposable disposable) throws Exception;
	}
}
