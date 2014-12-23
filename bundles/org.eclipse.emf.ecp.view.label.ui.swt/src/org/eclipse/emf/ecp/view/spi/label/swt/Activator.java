/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.label.swt;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * @author eugen
 * @since 1.5
 *
 */
public class Activator extends Plugin {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.ecp.view.label.ui.swt"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private VTViewTemplateProvider viewTemplate;

	/**
	 * Returns the current Instance of the {@link VTViewTemplateProvider}.
	 *
	 * @return the {@link VTViewTemplateProvider}
	 */
	public VTViewTemplateProvider getVTViewTemplateProvider() {
		if (viewTemplate == null) {
			final ServiceReference<VTViewTemplateProvider> viewTemplateReference = plugin.getBundle()
				.getBundleContext()
				.getServiceReference(VTViewTemplateProvider.class);
			if (viewTemplateReference != null) {
				viewTemplate = plugin.getBundle().getBundleContext().getService(viewTemplateReference);
			}
		}
		return viewTemplate;
	}

}
