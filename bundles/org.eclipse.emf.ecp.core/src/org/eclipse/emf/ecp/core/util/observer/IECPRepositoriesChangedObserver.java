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

import org.eclipse.emf.ecp.core.ECPRepository;

/**
 * This Observer is called to notify listeners about changes of repositories.
 * 
 * @author Eike Stepper
 * @author Edgar Mueller
 * @author Eugen Neufeld
 * 
 */
// TODO dicuss whether to split it up
public interface IECPRepositoriesChangedObserver extends IECPObserver {

	/**
	 * This is called to indicate, that repositories changed.
	 * 
	 * @param oldRepositories repository list before change
	 * @param newRepositories repository list after change
	 * @throws Exception thrown when something unexpected happens
	 */
	void repositoriesChanged(ECPRepository[] oldRepositories, ECPRepository[] newRepositories) throws Exception;

	/**
	 * This is called to indicate, that objects inside the repository changed.
	 * 
	 * @param repository the repository the changes occured
	 * @param objects the objects that changed
	 * @throws Exception thrown when something unexpected happens
	 */
	void objectsChanged(ECPRepository repository, Object[] objects) throws Exception;
}
