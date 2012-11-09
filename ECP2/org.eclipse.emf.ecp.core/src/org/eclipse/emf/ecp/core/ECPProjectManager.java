/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.core;

import org.eclipse.emf.ecp.core.util.ECPProjectAware;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedObserver;

/**
 * @author Eike Stepper
 */
public interface ECPProjectManager {

	public static final ECPProjectManager INSTANCE = org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl.INSTANCE;

	/**
	 * Method to construct an offline Project and notify listeners about this add.
	 * 
	 * @param provider the {@link ECPProvider} of this project
	 * @param name the name of the project
	 * @param properties the project properties
	 * @return created {@link ECPProject}
	 */

	ECPProject createProject(ECPProvider provider, String name, ECPProperties properties);

	/**
	 * Method to construct an online Project and notify listeners about this add.
	 * 
	 * @param repository the {@link ECPRepository} of this project
	 * @param name the name of the project
	 * @param properties the project properties
	 * @return created {@link ECPProject}
	 */
	ECPProject createProject(ECPRepository repository, String name, ECPProperties properties);

	/**
	 * Clones an {@link ECPProject} within the same provider.
	 * 
	 * @param project the {@link ECPProject} to clone
	 * @return the clone of the {@link ECPProject}
	 */
	ECPProject cloneProject(ECPProject project);

	/**
	 * This method checks whether the adaptable is {@link ECPProjectAware} and else uses the AdapterUtil to adapt to a
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

	void addObserver(IECPProjectsChangedObserver changeObserver);

	void removeObserver(IECPProjectsChangedObserver changeObserver);
}
