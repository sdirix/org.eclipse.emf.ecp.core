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
 *******************************************************************************/

package org.eclipse.emf.ecp.internal.core;

import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * This is the Activator for the ECP Core plugin.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public final class Activator extends Plugin {
	/**
	 * The constant holding the id of this plugin.
	 */
	public static final String PLUGIN_ID = "org.eclipse.emf.ecp.core";

	private static Activator instance;

	/**
	 * Default constructor.
	 */
	public Activator() {
	}

	// BEGIN SUPRESS CATCH EXCEPTION
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		instance = this;

		// File stateLocation = getStateLocation().toFile();
		// ECPRepositoryManagerImpl.INSTANCE.setFolder(new File(stateLocation, "repositories"));
		// ECPProjectManagerImpl.INSTANCE.setFolder(new File(stateLocation, "projects"));

		// ECPProviderRegistryImpl.INSTANCE.activate();
		// ECPRepositoryManagerImpl.INSTANCE.activate();
		// ECPProjectManagerImpl.INSTANCE.activate();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		ECPProjectManagerImpl.INSTANCE.deactivate();
		ECPRepositoryManagerImpl.INSTANCE.deactivate();
		ECPProviderRegistryImpl.INSTANCE.deactivate();

		instance = null;
		super.stop(bundleContext);
	}

	// END SUPRESS CATCH EXCEPTION

	public static Activator getInstance() {
		return instance;
	}

	/**
	 * Logs and Info message.
	 * 
	 * @param message the message to log
	 */
	public static void log(String message) {
		instance.getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
	}

	/**
	 * Logs a message with a specific status.
	 * 
	 * @param status the {@link IStatus} value
	 * @param message the message to log
	 */
	public static void log(int status, String message) {
		instance.getLog().log(new Status(status, PLUGIN_ID, message));
	}

	private static void log(IStatus status) {
		instance.getLog().log(status);
	}

	/**
	 * Logs a {@link Throwable}.
	 * 
	 * @param t the {@link Throwable} to log
	 * @return the message of the {@link Throwable}
	 */
	public static String log(Throwable t) {
		IStatus status = getStatus(t);
		log(status);
		return status.getMessage();
	}

	private static IStatus getStatus(Throwable t) {
		if (t instanceof CoreException) {
			CoreException coreException = (CoreException) t;
			return coreException.getStatus();
		}

		String msg = t.getLocalizedMessage();
		if (msg == null || msg.length() == 0) {
			msg = t.getClass().getName();
		}

		return new Status(IStatus.ERROR, PLUGIN_ID, msg, t);
	}

	public static ECPProjectManager getECPProjectManager() {
		ServiceReference<ECPProjectManager> serviceRef = instance.getBundle().getBundleContext()
			.getServiceReference(ECPProjectManager.class);
		return instance.getBundle().getBundleContext().getService(serviceRef);
	}

	public static ECPRepositoryManager getECPRepositoryManager() {
		ServiceReference<ECPRepositoryManager> serviceRef = instance.getBundle().getBundleContext()
			.getServiceReference(ECPRepositoryManager.class);
		return instance.getBundle().getBundleContext().getService(serviceRef);
	}

	public static ECPProviderRegistry getECPProviderRegistry() {
		ServiceReference<ECPProviderRegistry> serviceRef = instance.getBundle().getBundleContext()
			.getServiceReference(ECPProviderRegistry.class);
		return instance.getBundle().getBundleContext().getService(serviceRef);
	}
}
