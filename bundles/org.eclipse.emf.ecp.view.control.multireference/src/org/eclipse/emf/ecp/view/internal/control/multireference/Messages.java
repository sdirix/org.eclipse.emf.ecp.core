/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.control.multireference;

import org.eclipse.osgi.util.NLS;

/**
 * Messages file.
 * 
 * @author Eugen Neufeld
 * 
 */
public final class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.emf.ecp.view.control.multireference.messages"; //$NON-NLS-1$
	/**
	 * Add Existing Tooltip.
	 */
	public static String MultiReferenceSWTRenderer_addExistingTooltip;
	/**
	 * Add New Tooltip.
	 */
	public static String MultiReferenceSWTRenderer_addNewTooltip;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
