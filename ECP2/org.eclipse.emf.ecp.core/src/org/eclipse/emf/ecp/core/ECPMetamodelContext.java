/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas Helming - initial API and implementation
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.core;

import org.eclipse.emf.ecore.EClass;

import java.util.Set;

/**
 * @author Jonas Helming
 */
public interface ECPMetamodelContext {

	/**
	 * @return the classes which can be contained on the root level
	 */
	Set<EClass> getAllRootEClasses();

	/**
	 * @param eClass
	 *            the eClass to check
	 * @return if the class is non domain
	 */
	boolean isNonDomainElement(EClass eClass);

	/**
	 * @return if this context is guessed
	 */
	boolean isGuessed();

}
