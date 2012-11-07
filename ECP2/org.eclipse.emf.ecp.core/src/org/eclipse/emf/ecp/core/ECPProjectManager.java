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
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedObserver;

/**
 * @author Eike Stepper
 */
public interface ECPProjectManager {

	public static final ECPProjectManager INSTANCE = org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl.INSTANCE;

	public ECPProject createProject(ECPProvider provider, String name, ECPProperties properties);

	public ECPProject createProject(ECPRepository repository, String name, ECPProperties properties);

	public ECPProject cloneProject(ECPProject project);

	public ECPProject getProject(Object adaptable);

	public ECPProject getProject(String name);

	public ECPProject[] getProjects();

	public boolean hasProjects();

	public void addObserver(IECPProjectsChangedObserver changeObserver);

	public void removeObserver(IECPProjectsChangedObserver changeObserver);
}
