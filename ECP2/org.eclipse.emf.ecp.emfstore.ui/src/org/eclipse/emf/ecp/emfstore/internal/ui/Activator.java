package org.eclipse.emf.ecp.emfstore.internal.ui;

import org.eclipse.emf.ecp.emfstore.internal.ui.decorator.EMFStoreDirtyObserver;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.eclipse.emf.ecp.emfstore.ui"; //$NON-NLS-1$

	private static Activator instance;

	private EMFStoreDirtyObserver dirtyObserver;

	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		super.stop(context);
	}

	public static Activator getInstance() {
		return instance;
	}

	public static void log(String message) {
		instance.getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
	}

	public static void log(IStatus status) {
		instance.getLog().log(status);
	}

	public static String log(Throwable t) {
		IStatus status = getStatus(t);
		log(status);
		return status.getMessage();
	}

	public static IStatus getStatus(Throwable t) {
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

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
