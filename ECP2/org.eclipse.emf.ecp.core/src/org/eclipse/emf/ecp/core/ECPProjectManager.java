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

import org.eclipse.emf.ecp.core.util.ECPProperties;

/**
 * @author Eike Stepper
 */
public interface ECPProjectManager {
	public static final ECPProjectManager INSTANCE = org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl.INSTANCE;

	// APITODO: comment the next methods
	/**
	 * 
	 * @param provider
	 * @param name
	 * @param properties
	 * @return
	 */
	ECPProject createProject(ECPProvider provider, String name);

	ECPProject createProject(ECPProvider provider, String name, ECPProperties properties);

	ECPProject createProject(ECPRepository repository, String name, ECPProperties properties);

	/**
	 * Clones an {@link ECPProject} within the same provider.
	 * 
	 * @param project
	 * @return
	 */
	ECPProject cloneProject(ECPProject project);

	/**
	 * Document when this works
	 * 
	 * @param adaptable
	 * @return
	 */
	public ECPProject getProject(Object adaptable);

	ECPProject getProject(String name);

	public ECPProject[] getProjects();

	// APITODO: Should we change the interface to an observer bus?
	public void addObserver(IECPProjectsChangedObserver changeObserver);

	public void removeObserver(IECPProjectsChangedObserver changeObserver);
}
