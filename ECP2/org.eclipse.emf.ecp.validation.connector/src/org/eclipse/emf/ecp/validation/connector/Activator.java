/*******************************************************************************
 * Copyright (c) 2008-2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.validation.connector;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.observer.ECPObserverBus;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedObserver;
import org.eclipse.emf.ecp.validation.api.IValidationService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author emueller
 */
public class Activator extends AbstractUIPlugin {

	/**
	 * The plug-in ID.
	 */
	public static final String PLUGIN_ID = "org.eclipse.emf.ecp.validation.connector"; //$NON-NLS-1$
	
	private static Set<Class<?>> excludedTypes;
	
	/** 
	 * The shared instance.
	 */
	private static Activator plugin;

	private IValidationService validationService;

	private ValidationObserver validationObserver;

	private BundleContext context;
	
	/**
	 * The constructor.
	 */
	public Activator() {
	}

	/**
	 * {@inheritDoc}
	 */
	// BEGIN SUPRESS CATCH EXCEPTION
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.context = context;
		plugin = this;
		// Register directly with the service
		ServiceReference<IValidationService> reference = context.getServiceReference(IValidationService.class);
		validationService = (IValidationService) context.getService(reference);
		validationObserver = new ValidationObserver();
		ECPObserverBus.getInstance().register(validationObserver);
		
		excludedTypes = new HashSet<Class<?>>();
		excludedTypes.add(ECPProject.class);
		
		for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders()) {
			excludedTypes.add(provider.getContainerClass());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop(BundleContext context) throws Exception {
		ECPObserverBus.getInstance().unregister(validationObserver);
		plugin = null;
		super.stop(context);
	}
	// END SUPRESS CATCH EXCEPTION

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns the validation service.
	 * 
	 * @return the validation service
	 */
	public IValidationService getValidationService() {
		if (validationService == null) {
			ServiceReference<IValidationService> reference = context.getServiceReference(IValidationService.class);
			validationService = (IValidationService) context.getService(reference);
		}
		return validationService;
	}

	/**
	 * Project change observer that validates changed objects.
	 */
	private class ValidationObserver implements IECPProjectsChangedObserver {

		// BEGIN SUPRESS CATCH EXCEPTION
		public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {
			for (ECPProject project : newProjects) {
				getValidationService().validate(project.getElements());
			}
		}

		public void projectChanged(ECPProject project, boolean opened) throws Exception {
			getValidationService().validate(project.getElements());
		}

		public void objectsChanged(ECPProject project, Object[] objects) throws Exception {
			
			for (Object object : objects) {
				
				if (!(object instanceof EObject)) {
					continue;
				}
				
				EObject eObject = (EObject) object;
				
				if (project.contains(eObject)) {
					getValidationService().validate((EObject) object, excludedTypes);
				} else {
					getValidationService().remove(eObject, excludedTypes);
				}
			}
		}
		// END SUPRESS CATCH EXCEPTION
	}
	
	/**
	 * Returns an {@link ImageDescriptor} for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
