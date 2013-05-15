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
 *******************************************************************************/
package org.eclipse.emf.ecp.core.util.observer;

/**
 * @author Eugen Neufeld
 * 
 */
public interface ECPObserverBus {
	/**
	 * Registers an observer for all observer interfaces implemented by the object or its super classes.
	 * 
	 * @param observer observer object
	 */
	void register(ECPObserver observer);

	/**
	 * Unregisters an observer for all observer interfaces implemented by the object or its super classes.
	 * 
	 * @param observer observer object
	 */
	void unregister(ECPObserver observer);

	/**
	 * This method allows you to notify all observers.
	 * 
	 * @param <T> class of observer
	 * @param clazz class of observer
	 * @return call object
	 */
	<T extends ECPObserver> T notify(Class<T> clazz);
}
