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
package org.eclipse.emf.ecp.spi.core.util;

/**
 * This interface is used on classes that can be disposed.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
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
	public interface DisposeListener {
		/**
		 * Callback method being used to notify listeners about a dispose.
		 * 
		 * @param disposable the object being disposed
		 * @throws DisposeException is thrown when something goes wrong
		 */
		void disposed(ECPDisposable disposable) throws DisposeException;
	}

	/**
	 * This is an Exception that is thrown during dispose actions.
	 * 
	 * @author Eugen Neufeld
	 * 
	 */
	public class DisposeException extends Exception {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructs a new exception with the specified detail message and cause.
		 * 
		 * @param message the detail message (which is saved for later retrieval by the getMessage() method).
		 * @param cause the cause (which is saved for later retrieval by the getCause() method). (A null value is
		 *            permitted, and indicates that the cause is nonexistent or unknown.)
		 */
		public DisposeException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * Constructs a new exception with the specified cause and a detail message of (cause==null ? null :
		 * cause.toString())
		 * 
		 * @param cause the cause (which is saved for later retrieval by the getCause() method).
		 */
		public DisposeException(Throwable cause) {
			super(cause);
		}

	}
}
