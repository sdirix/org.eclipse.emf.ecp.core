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
 * Edgar Mueller - change to ECPObserver
 * Eugen Neufeld - JavaDoc
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.core.util.observer;

import org.eclipse.emf.ecp.core.ECPProject;

import java.util.Collection;

/**
 * This Observer is called to notify listeners about changes of a project.
 * 
 * @author Eike Stepper
 * @author Edgar Mueller
 * @author Eugen Neufeld
 * 
 */
// TODO dicuss whether to split it up
public interface ECPProjectsChangedUIObserver extends ECPObserver {

	/**
	 * This is called when a project changes, so if it is either added or removed.
	 * 
	 * @param oldProjects the collection of projects before the change
	 * @param newProjects the collection of projects after the change
	 * @throws Exception thrown when something unexpected happens
	 */
	void projectsChanged(Collection<ECPProject> oldProjects, Collection<ECPProject> newProjects) throws Exception;

	/**
	 * This is called when a project is opened or closed.
	 * 
	 * @param project the {@link ECPProject} that changed
	 * @param opened whether it was opened or closed
	 * @throws Exception thrown when something unexpected happens
	 */
	void projectChanged(ECPProject project, boolean opened) throws Exception;

	/**
	 * This is called when objects of a project changed.
	 * 
	 * @param project the project where the changed occured.
	 * @param objects the objects that changed
	 * @param structural whether the change was structural
	 * @throws Exception thrown when something unexpected happens
	 */
	void objectsChanged(ECPProject project, Collection<Object> objects, boolean structural) throws Exception;
}
