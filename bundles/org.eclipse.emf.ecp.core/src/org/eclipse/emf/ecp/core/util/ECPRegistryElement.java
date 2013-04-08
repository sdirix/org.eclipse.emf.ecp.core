/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
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
 * Common super interface for provider and repositories.
 * 
 * 
 * @author Eike Stepper
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface ECPRegistryElement extends ECPElement {
	/**
	 * Returns the label.
	 * 
	 * @return the label
	 */
	String getLabel();

	/**
	 * Return the description.
	 * 
	 * @return the description
	 */
	String getDescription();
}
