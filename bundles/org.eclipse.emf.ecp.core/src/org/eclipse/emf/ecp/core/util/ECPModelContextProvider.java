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
 * Eugen Neufeld - JavaDoc
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.core.util;

/**
 * The {@link ECPModelContextProvider} can return the {@link ECPModelContainer} of a certain Object.
 * This class should not be mixed up with {@link ECPModelContextAware} which can only return the {@link ECPModelContainer}
 * it knows.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public interface ECPModelContextProvider {
	/**
	 * Returns the first {@link ECPModelContainer} that can be found for the provided Object.
	 * 
	 * @param element the element to search the {@link ECPModelContainer} for
	 * @return the {@link ECPModelContainer} of this element
	 */
	ECPModelContainer getModelContext(Object element);
}
