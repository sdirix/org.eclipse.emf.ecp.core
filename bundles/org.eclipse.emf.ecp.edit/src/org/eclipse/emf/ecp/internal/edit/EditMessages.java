/*******************************************************************************
 * Copyright (c) 2013 EclipseSource.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.internal.edit;

import org.eclipse.osgi.util.NLS;

/**
 * The Class EditMessages.
 */
public final class EditMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.emf.ecp.internal.edit.messages"; //$NON-NLS-1$

	/** The Control factory impl_ cannot be loaded because bundle. */
	public static String ControlFactoryImpl_CannotBeLoadedBecauseBundle;

	/** The Control factory impl_ cannot be resolved. */
	public static String ControlFactoryImpl_CannotBeResolved;

	private EditMessages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, EditMessages.class);
	}

}
