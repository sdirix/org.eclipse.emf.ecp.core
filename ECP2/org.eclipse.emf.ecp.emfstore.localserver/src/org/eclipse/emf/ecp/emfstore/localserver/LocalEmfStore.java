/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Tobias Verhoeven - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.emfstore.localserver;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.emf.emfstore.server.EmfStoreController;
import org.eclipse.emf.emfstore.server.exceptions.FatalEmfStoreException;

/**
 * The LocalEmfStore contains static methods to manually start the EmfStrore.
 * 
 * @author Tobias Verhoeven
 */
public final class LocalEmfStore {

	/**
	 * Instantiates a new local emf store.
	 */
	private LocalEmfStore() {};

	/**
	 * Starts a EMFStore instance.
	 */
	public static void start() {
		try {
			EmfStoreController.runAsNewThread();
		} catch (FatalEmfStoreException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} 
	}

	/**
	 * Start an EMFStore instance if the preference value 
	 * org.eclipse.emf.ecp.emfstore.localserver/STARTUP is true.
	 */
	public static void startIfNeeded() {
		if (shouldStart()) {
			start();
		}
	}

	/**
	 * Should start.
	 *
	 * @return  returns weather a call to startIfNeeded will start the EmfStore by reading
	 * the preference com.integrationcentral.emfstorelocal/STARTUP
	 */
	public static boolean shouldStart() {
		IPreferencesService service = Platform.getPreferencesService();	
		return service.getBoolean("com.integrationcentral.emfstorelocal", "STARTUP",true,null);
	}
}
