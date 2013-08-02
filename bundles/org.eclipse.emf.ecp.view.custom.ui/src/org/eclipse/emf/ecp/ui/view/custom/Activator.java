package org.eclipse.emf.ecp.ui.view.custom;

import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

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

	private static ServiceReference<ECPControlFactory> controlFactoryReference;

	public static ECPControlFactory getECPControlFactory() {
		if (controlFactoryReference == null) {
			controlFactoryReference = context
				.getServiceReference(ECPControlFactory.class);
		}
		return context.getService(controlFactoryReference);
	}

	public static void ungetECPControlFactory() {
		if (controlFactoryReference == null) {
			return;
		}
		context.ungetService(controlFactoryReference);
		controlFactoryReference = null;
	}
}
