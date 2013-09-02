package org.eclipse.emf.ecp.view.editor.controls;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.ECPProjectContentChangedObserver;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.provider.xmi.ViewModelFileExtensionsManager;
import org.eclipse.emf.ecp.workspace.internal.core.WorkspaceProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.emf.ecp.view.editor.controls"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor.
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		/**
		 * Listen to changes in file projects. If a view is affected, reload the view models provided over file
		 * extension point.
		 */
		ECPUtil.getECPObserverBus().register(new ECPProjectContentChangedObserver() {

			public Collection<Object> objectsChanged(ECPProject project, Collection<Object> objects) {
				if (project.getProvider().getName().equals(WorkspaceProvider.NAME)) {
					for (final Object object : objects) {
						if (object instanceof View) {
							ViewModelFileExtensionsManager.dispose();
						}
					}
				}
				return new ArrayList<Object>();

			}

		});
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
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
