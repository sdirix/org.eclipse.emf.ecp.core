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
package org.eclipse.emf.ecp.view.internal.table.swt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * Activator for this plugin.
 *
 * @author Eugen Neufeld
 *
 */
public class Activator extends Plugin {

	private static final String PLUGIN_ID = "org.eclipse.emf.ecp.view.table.ui.swt"; //$NON-NLS-1$
	private static Activator instance;

	// BEGIN SUPRESS CATCH EXCEPTION
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		instance = this;
		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		instance = null;
		super.stop(bundleContext);
	}

	// END SUPRESS CATCH EXCEPTION

	// /**
	// * Finds and returns an image for the provided path.
	// *
	// * @param path the path to get the image from
	// * @return the image or null if nothing could be found
	// */
	// public static Image getImage(String path) {
	//
	// final Image image = instance.getImageRegistryService().getImage(instance.getBundle(), path);
	//
	// instance.getBundle().getBundleContext().ungetService(instance.imageRegistryServiceReference);
	//
	// return image;
	// }
	//
	// /**
	// * Finds and returns an image for the provided {@link URL}.
	// *
	// * @param url the {@link URL} to get the image from
	// * @return the image or null if nothing could be found
	// */
	// public static Image getImage(URL url) {
	// final Image image = instance.getImageRegistryService().getImage(url);
	//
	// instance.getBundle().getBundleContext().ungetService(instance.imageRegistryServiceReference);
	//
	// return image;
	// }
	//
	// private ServiceReference<ImageRegistryService> imageRegistryServiceReference;
	//
	// private ImageRegistryService getImageRegistryService() {
	// if (imageRegistryServiceReference == null) {
	// imageRegistryServiceReference = getBundle().getBundleContext()
	// .getServiceReference(ImageRegistryService.class);
	// }
	// return getBundle().getBundleContext().getService(imageRegistryServiceReference);
	// }
	//
	/**
	 * Logs a {@link Throwable}.
	 *
	 * @param t the {@link Throwable} to log
	 */
	public static void log(Throwable t) {
		instance.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, t.getMessage(), t));
	}
	//
	// private VTViewTemplateProvider viewTemplate;
	// private ServiceReference<ReportService> reportServiceReference;
	//
	// /**
	// * Returns the current Instance of the {@link VTViewTemplateProvider}.
	// *
	// * @return the {@link VTViewTemplateProvider}
	// */
	// public VTViewTemplateProvider getVTViewTemplateProvider() {
	// if (viewTemplate == null) {
	// final ServiceReference<VTViewTemplateProvider> viewTemplateReference = instance.getBundle()
	// .getBundleContext()
	// .getServiceReference(VTViewTemplateProvider.class);
	// if (viewTemplateReference != null) {
	// viewTemplate = instance.getBundle().getBundleContext().getService(viewTemplateReference);
	// }
	// }
	// return viewTemplate;
	// }
	//
	// /**
	// * The current instance.
	// *
	// * @return the current {@link Activator} instance
	// */
	// public static Activator getInstance() {
	// return instance;
	// }
	//
	// /**
	// * Returns the {@link ReportService}.
	// *
	// * @return the {@link ReportService}
	// */
	// public ReportService getReportService() {
	// if (reportServiceReference == null) {
	// reportServiceReference = instance.getBundle().getBundleContext()
	// .getServiceReference(ReportService.class);
	// }
	// return instance.getBundle().getBundleContext().getService(reportServiceReference);
	// }
	//
	// /**
	// * Returns the {@link EMFFormsDatabinding} service.
	// *
	// * @return The {@link EMFFormsDatabinding}
	// */
	// public EMFFormsDatabinding getEMFFormsDatabinding() {
	// final ServiceReference<EMFFormsDatabinding> serviceReference = instance.getBundle().getBundleContext()
	// .getServiceReference(EMFFormsDatabinding.class);
	//
	// final EMFFormsDatabinding service = instance.getBundle().getBundleContext()
	// .getService(serviceReference);
	// instance.getBundle().getBundleContext().ungetService(serviceReference);
	//
	// return service;
	// }
	//
	// /**
	// * Returns the {@link EMFFormsLabelProvider} service.
	// *
	// * @return The {@link EMFFormsLabelProvider}
	// */
	// public EMFFormsLabelProvider getEMFFormsLabelProvider() {
	// final ServiceReference<EMFFormsLabelProvider> serviceReference = instance.getBundle().getBundleContext()
	// .getServiceReference(EMFFormsLabelProvider.class);
	//
	// final EMFFormsLabelProvider service = instance.getBundle().getBundleContext()
	// .getService(serviceReference);
	// instance.getBundle().getBundleContext().ungetService(serviceReference);
	//
	// return service;
	// }
}
