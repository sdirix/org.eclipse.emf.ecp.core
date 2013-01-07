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
 * Edgar Mueller - change to IECPObserver
 * Eugen Neufeld - JavaDoc
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.core.util.observer;

import java.util.Map;

/**
 * This Observer is called to notify listeners about changes of properties.
 * 
 * @author Eike Stepper
 * @author Edgar Mueller
 * @author Eugen Neufeld
 * 
 */
public interface IECPPropertiesChangedObserver extends IECPObserver {

	/**
	 * This is called to indicate, that properties changed.
	 * 
	 * @param oldProperties the properties before changing
	 * @param newProperties the properties after changing
	 */
	void propertiesChanged(Map.Entry<String, String>[] oldProperties, Map.Entry<String, String>[] newProperties);
}
