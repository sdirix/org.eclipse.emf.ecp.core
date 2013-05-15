package org.eclipse.emf.ecp.validation.test;

import org.eclipse.emf.ecp.validation.api.IValidationServiceProvider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

	private static IValidationServiceProvider validationService;
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	public static IValidationServiceProvider getValidationService() {
		if (validationService == null) {
			ServiceReference<?> eventServiceReference = context.getServiceReference(IValidationServiceProvider.class.getName());
			if (eventServiceReference != null) {
				validationService = (IValidationServiceProvider) context.getService(eventServiceReference);
			}
		}
		return validationService;
	}

}
