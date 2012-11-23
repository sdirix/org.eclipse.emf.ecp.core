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
package org.eclipse.emf.ecp.core;

import org.eclipse.emf.ecp.core.util.ECPDeletable;
import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPPropertiesAware;

import org.eclipse.core.runtime.IAdaptable;

/**
 * @author Eike Stepper
 */
public interface ECPRepository extends ECPElement, ECPModelContext, ECPPropertiesAware, ECPDeletable, IAdaptable {
	/**
	 * The type of this ECPElement.
	 */
	String TYPE = "Repository";

	/**
	 * Returns the label for the {@link ECPRepository}.
	 * 
	 * @return the label for this repository
	 */
	String getLabel();

	/**
	 * Returns the description for this {@link ECPRepository}.
	 * 
	 * @return the description for this repository
	 */
	String getDescription();

	// TODO remove or use
	ECPProject[] getOpenProjects();
}
