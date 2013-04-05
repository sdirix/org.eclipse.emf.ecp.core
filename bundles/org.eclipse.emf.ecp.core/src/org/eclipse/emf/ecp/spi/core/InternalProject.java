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
 *******************************************************************************/
package org.eclipse.emf.ecp.spi.core;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPProjectAware;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore.StorableElement;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;

import java.util.Collection;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface InternalProject extends ECPProject, ECPProjectAware, StorableElement, Cloneable {

	/**
	 * This method returns the repository this project is shared on.
	 * 
	 * @return the repository of this project or null if not shared
	 */
	InternalRepository getRepository();

	/**
	 * This method returns the provider of this project.
	 * 
	 * @return the provider of this project
	 */
	InternalProvider getProvider();

	/**
	 * This method returns the provider specific data of this project.
	 * 
	 * @return the provider specific data of this project
	 */
	Object getProviderSpecificData();

	/**
	 * This method sets the provider specific data of this project.
	 * 
	 * @param data the provider specific data of this project
	 */
	void setProviderSpecificData(Object data);

	/**
	 * This method is a callback for the provider to notify the project about changes.
	 * 
	 * @param objects the objects that have changed
	 * @param structural if the changes where structural (e.g. delete)
	 */
	void notifyObjectsChanged(Collection<Object> objects, boolean structural);

	/**
	 * This method undisposes the project based on a repository.
	 * 
	 * @param repository the repository
	 */
	void undispose(InternalRepository repository);

	/**
	 * This method is used to notify the provider about a {@link LifecycleEvent} of this project.
	 * 
	 * @param event to pass to the provider
	 */
	void notifyProvider(LifecycleEvent event);

	/**
	 * This method clones a project.
	 * 
	 * @return the cloned project
	 */
	InternalProject clone();
}
