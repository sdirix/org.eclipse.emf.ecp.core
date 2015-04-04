/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 * Lucas Koehler - completely change it
 */
package org.eclipse.emf.ecp.view.template.controls.swt;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.ecp.view.spi.model.reporting.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * The Activator.
 *
 * @author Eugen Neufeld
 *
 */
public class Activator extends Plugin {

	/**
	 * The bundle ID.
	 */
	public static final String PLUGIN_ID = "org.eclipse.emf.ecp.view.template.controls.swt"; //$NON-NLS-1$
	private static Activator plugin;
	private static BundleContext context;

	/**
	 * The BundleContext.
	 *
	 * @return The {@link BundleContext}
	 */
	static BundleContext getContext() {
		return context;
	}

	// BEGIN SUPRESS CATCH EXCEPTION
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		plugin = this;
		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		plugin = null;
		super.stop(bundleContext);
	}

	// END SUPRESS CATCH EXCEPTION

	private ServiceReference<ReportService> reportServiceReference;

	/**
	 * The current plugin.
	 *
	 * @return the current {@link Activator} plugin
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns the {@link ReportService}.
	 *
	 * @return the {@link ReportService}
	 */
	public ReportService getReportService() {
		if (reportServiceReference == null) {
			reportServiceReference = plugin.getBundle().getBundleContext()
				.getServiceReference(ReportService.class);
		}
		return plugin.getBundle().getBundleContext().getService(reportServiceReference);
	}

	/**
	 * Returns the {@link EMFFormsDatabinding} service.
	 *
	 * @return The {@link EMFFormsDatabinding}
	 */
	public EMFFormsDatabinding getEMFFormsDatabinding() {
		final ServiceReference<EMFFormsDatabinding> serviceReference = plugin.getBundle().getBundleContext()
			.getServiceReference(EMFFormsDatabinding.class);

		final EMFFormsDatabinding service = plugin.getBundle().getBundleContext()
			.getService(serviceReference);
		plugin.getBundle().getBundleContext().ungetService(serviceReference);

		return service;
	}
}