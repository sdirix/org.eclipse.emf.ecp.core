/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.changebroker.spi;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Jonas
 * @since 1.7
 *        The Observer gets called after a delete operation has been completed
 */
public interface PostDeleteObserver extends EMFObserver {

	/**
	 * @param objectToBeDeleted The deleted {@link EObject}
	 */
	void postDelete(EObject objectToBeDeleted);

}
