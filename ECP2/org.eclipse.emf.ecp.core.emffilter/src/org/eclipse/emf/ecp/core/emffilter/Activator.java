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
package org.eclipse.emf.ecp.core.emffilter;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator implements BundleActivator {

	private static BundleContext context;
	
	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	static BundleContext getContext() {
		return context;
	}
	// BEGIN SUPRESS CATCH EXCEPTION
	/**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	// END SUPRESS CATCH EXCEPTION
}
