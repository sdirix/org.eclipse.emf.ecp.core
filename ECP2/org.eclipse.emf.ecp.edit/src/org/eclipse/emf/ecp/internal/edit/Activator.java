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
package org.eclipse.emf.ecp.internal.edit;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
/**
 * Activator class.
 * @author Eugen Neufeld
 *
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	/**
	 * Convenient method to return the {@link BundleContext}.
	 * @return the current {@link BundleContext}
	 */
	static BundleContext getContext() {
		return context;
	}

	/**
	 * @see BundleActivator#start(org.osgi.framework.BundleContext)
	 * @generated
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/**
	 * @see BundleActivator#stop(org.osgi.framework.BundleContext)
	 * @generated
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
