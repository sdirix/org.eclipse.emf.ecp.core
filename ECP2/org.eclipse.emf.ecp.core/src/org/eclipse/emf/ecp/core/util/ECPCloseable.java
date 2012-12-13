/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
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
 * This Interface describes objects that can be closed.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public interface ECPCloseable {
	/**
	 * Whether an object is open or not.
	 * 
	 * @return true if it is open, false otherwise
	 */
	boolean isOpen();

	/**
	 * This opens a closed object. If the object was already opened, nothing happens.
	 */
	void open();

	/**
	 * This closes an opened object. If the object was already closed, nothing happens.
	 */
	void close();
}
