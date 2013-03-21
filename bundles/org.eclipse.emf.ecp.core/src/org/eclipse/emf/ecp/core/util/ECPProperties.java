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

import org.eclipse.emf.ecp.core.util.observer.IECPPropertiesChangedObserver;

import java.util.Map;

/**
 * {@link ECPProperties} are used to store key - value - pairs.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface ECPProperties {
	/**
	 * Adds a key-value-pair.
	 * 
	 * @param key the key of the property
	 * @param value the value of the property
	 */
	void addProperty(String key, String value);

	/**
	 * Removes a property.
	 * 
	 * @param key of the property to remove
	 */
	void removeProperty(String key);

	/**
	 * Get the value of a property identified by this key.
	 * 
	 * @param key the key of the property to find
	 * @return the value of the property identified by the key or null
	 */
	String getValue(String key);

	/**
	 * All stored keys.
	 * 
	 * @return an array of all keys
	 */
	String[] getKeys();

	/**
	 * All Properties.
	 * 
	 * @return an array of Key-Value-Pairs
	 */
	Map.Entry<String, String>[] getProperties();

	/**
	 * Whether any properties are stored.
	 * 
	 * @return true if at least 1 property was added, false otherwise
	 */
	boolean hasProperties();

	/**
	 * Creates a copy of the current set of properties.
	 * 
	 * @return the copy of the current properties
	 */

	ECPProperties copy();

	/**
	 * Adds an {@link IECPPropertiesChangedObserver} which will be notified if a property changes.
	 * 
	 * @param changeObserver the observer to add
	 */
	void addObserver(IECPPropertiesChangedObserver changeObserver);

	/**
	 * Removes an {@link IECPPropertiesChangedObserver} from the collection of observer which will be notified if a
	 * property changes.
	 * 
	 * @param changeObserver the observer to remove
	 */
	void removeObserver(IECPPropertiesChangedObserver changeObserver);

}
