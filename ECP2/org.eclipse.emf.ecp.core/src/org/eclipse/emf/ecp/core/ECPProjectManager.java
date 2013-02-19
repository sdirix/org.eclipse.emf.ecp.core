/*******************************************************************************
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.core;

import org.eclipse.emf.ecp.core.exception.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedUIObserver;

/**
 * @author Eike Stepper
 * @author Jonas
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ECPProjectManager {

	/**
	 * Instance of the ECPProjectManager.
	 */
	ECPProjectManager INSTANCE = org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl.INSTANCE;

	/**
	 * Method to construct an offline Project, this method calls
	 * {@link #createProject(ECPProvider, String, ECPProperties)} with empty properties.
	 * 
	 * @param provider the {@link ECPProvider} of this project
	 * @param name the name of the project
	 * @return created {@link ECPProject}
	 * @throws ProjectWithNameExistsException when a project with the same name already exists
	 */

	ECPProject createProject(ECPProvider provider, String name) throws ProjectWithNameExistsException;

	/**
	 * Method to construct an offline Project and notify listeners about this add.
	 * 
	 * @param provider the {@link ECPProvider} of this project
	 * @param name the name of the project
	 * @param properties the project properties
	 * @return created {@link ECPProject}
	 * @throws ProjectWithNameExistsException when a project with the same name already exists
	 */

	ECPProject createProject(ECPProvider provider, String name, ECPProperties properties)
		throws ProjectWithNameExistsException;

	/**
	 * Method to construct an shared Project, e.g. during a checkout, and notify listeners about this add.
	 * 
	 * @param repository the {@link ECPRepository} of this project
	 * @param name the name of the project
	 * @param properties the project properties
	 * @return created {@link ECPProject}
	 * @throws ProjectWithNameExistsException when a project with the same name already exists
	 */
	ECPProject createProject(ECPRepository repository, String name, ECPProperties properties)
		throws ProjectWithNameExistsException;

	/**
	 * Clones an {@link ECPProject} within the same provider.
	 * 
	 * @param project the {@link ECPProject} to clone
	 * @return the clone of the {@link ECPProject}
	 */
	ECPProject cloneProject(ECPProject project);

	/**
	 * This method checks whether the adaptable is {@link org.eclipse.emf.ecp.core.util.ECPProjectAware ECPProjectAware}
	 * and else uses the AdapterUtil to adapt to a
	 * project.
	 * 
	 * @param adaptable the Object to adapt
	 * @return the adapted {@link ECPProject}
	 */
	ECPProject getProject(Object adaptable);

	/**
	 * Searches for a project based on the provided {@link String}.
	 * 
	 * @param name of the project to search for
	 * @return the {@link ECPProject}
	 */

	ECPProject getProject(String name);

	/**
	 * Returns all known projects.
	 * 
	 * @return an array of all known projects
	 */
	ECPProject[] getProjects();

	/**
	 * This method adds a observer to the observer of the ECPProjectManager.
	 * 
	 * @param observer the {@link IECPProjectsChangedUIObserver} to add
	 */
	void addObserver(IECPProjectsChangedUIObserver observer);

	/**
	 * This method removes a observer from the observer of the ECPProjectManager.
	 * 
	 * @param observer the {@link IECPProjectsChangedUIObserver} to remove
	 */
	void removeObserver(IECPProjectsChangedUIObserver observer);
}
