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
 * Eugen Neufeld - javaDoc
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.core;

import org.eclipse.emf.ecp.core.util.ECPProperties;

import java.util.Collection;

/**
 * The ECPRepositoryManager contains all ECPRepositories and manages their lifecycle.
 * 
 * @author Eike Stepper
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ECPRepositoryManager {

	/**
	 * This method returns a {@link ECPRepository} from an adaptable.
	 * 
	 * @param adaptable the adaptable to adapt
	 * @return {@link ECPRepository} or null if the adaptable could not be adapted
	 */

	ECPRepository getRepository(Object adaptable);

	/**
	 * This method returns a repository by its name.
	 * 
	 * @param name the name of the repository
	 * @return the {@link ECPRepository} or null if no repository with such name exists.
	 */

	ECPRepository getRepository(String name);

	/**
	 * Returns all known repositories.
	 * 
	 * @return an array of all known {@link ECPRepository ECPRepositories}
	 */

	Collection<ECPRepository> getRepositories();

	/**
	 * This method allows the user to create a repository. If {@link ECPProvider#hasCreateRepositorySupport()} returns
	 * false an UnsupportedOperationException is thrown.
	 * 
	 * @param provider the {@link ECPProvider} of this repository
	 * @param name the name of the new repository
	 * @param label the label of the new repository
	 * @param description the description of the new repository
	 * @param properties the {@link ECPProperties} of this repository
	 * @return the created {@link ECPRepository}
	 */
	ECPRepository addRepository(ECPProvider provider, String name, String label, String description,
		ECPProperties properties);

	// /**
	// * Add an {@link ECPRepositoryManagerObserver} to be notified.
	// *
	// * @param changeObserver the observer to add
	// */
	// void addObserver(ECPRepositoryManagerObserver changeObserver);
	//
	// /**
	// * Remove an {@link ECPRepositoryManagerObserver} from the list of the providers to be notified.
	// *
	// * @param changeObserver the observer to remove
	// */
	// void removeObserver(ECPRepositoryManagerObserver changeObserver);
}
