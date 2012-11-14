/**
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

import org.eclipse.emf.ecp.core.util.ECPElement;

import org.eclipse.core.runtime.IAdaptable;

/**
 * @author Eike Stepper
 */
public interface ECPProvider extends ECPElement, IAdaptable {
	/**
	 * Type of the ECPElement.
	 */
	String TYPE = "Provider";

	/**
	 * Label of the ECPProvider.
	 * 
	 * @return the name of the ECPProvider
	 */
	String getLabel();

	/**
	 * Description of the ECPProvider.
	 * 
	 * @return text describing this provider
	 */
	String getDescription();

	/**
	 * Array of all {@link ECPRepository}s known to this ECPProvider.
	 * 
	 * @return repositories using this ECPProvider
	 */
	ECPRepository[] getAllRepositories();

	/**
	 * Array of all ECPProjects based on this ECPProvider which are open.
	 * 
	 * @return open projects using this ECPProvider
	 */
	ECPProject[] getOpenProjects();

	/**
	 * Check whether this new repositories can be added to this ECPProvider.
	 * 
	 * @return true if new repositories can be added.
	 */
	boolean canAddRepositories();

	/**
	 * Wether this ECPProvider can have an offline project.
	 * 
	 * @return true if offline projects are allowed, false otherwise.
	 */
	boolean hasUnsharedProjectSupport();

}
