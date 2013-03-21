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
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit;

import org.eclipse.emf.ecore.EObject;

/**
 * Listens to the changes of a context. notify ui provider to close
 * 
 * @author Eugen Neufeld
 * @author helming
 */
public interface EditModelElementContextListener {

	/**
	 * Called if a model element is deleted. Is only called for the root node if
	 * a tree of model elements is deleted.
	 * @param deleted the {@link EObject} that was deleted
	 */
	void onModelElementDeleted(EObject deleted);

	/**
	 * Call if the context gets deleted.
	 */
	void onContextDeleted();

}
