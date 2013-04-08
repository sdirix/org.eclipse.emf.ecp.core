/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.core.util.observer;

import org.eclipse.emf.ecp.core.ECPProject;

import java.util.Collection;

/**
 * This Observer is called to notify listeners about changes of the objects in a project.
 * 
 * @author Eugen Neufeld
 * 
 */
public interface ECPProjectObjectsChangedObserver extends ECPProjectManagerObserver {

	/**
	 * Return array of affected Objects.
	 * 
	 * @param project
	 * @param objects
	 * @return array of affected objects.
	 * @throws Exception
	 */
	Collection<Object> objectsChanged(ECPProject project, Collection<Object> objects) throws Exception;
}
