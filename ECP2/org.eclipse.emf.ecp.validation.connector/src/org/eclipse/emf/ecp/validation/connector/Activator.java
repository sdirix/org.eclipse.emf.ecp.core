/*******************************************************************************
 * Copyright (c) 2008-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.validation.connector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.observer.ECPObserverBus;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectObjectsChangedObserver;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedUIObserver;
import org.eclipse.emf.ecp.validation.api.IValidationService;
import org.eclipse.emf.ecp.validation.api.IValidationServiceProvider;
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

	/**
	 * The shared instance.
	 */
	private static Activator plugin;

	private IValidationServiceProvider validationServiceProvider;

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

		validationObserver = new ValidationObserver();
		ECPObserverBus.getInstance().register(validationObserver);
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
	 * @param project
	 *            the project for which to return the validation service
	 * 
	 * @return the validation service
	 */
	public IValidationService getValidationService(ECPProject project) {
		return getValidationServiceProvider().getValidationService(project);
	}
	
	private IValidationServiceProvider getValidationServiceProvider() {
		if (validationServiceProvider == null) {
			// Register directly with the service
			ServiceReference<IValidationServiceProvider> reference = context
				.getServiceReference(IValidationServiceProvider.class);
			validationServiceProvider = (IValidationServiceProvider) context.getService(reference);
		}
		return validationServiceProvider;
	}
	
	/**
	 * Project change observer that validates changed objects.
	 */
	private class ValidationObserver implements IECPProjectsChangedUIObserver, IECPProjectObjectsChangedObserver {

		// BEGIN SUPRESS CATCH EXCEPTION
		public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {
			List<ECPProject> newProjectList = Arrays.asList(newProjects);
			for (ECPProject project : oldProjects) {
				if (!newProjectList.contains(project)) {
					getValidationServiceProvider().deleteValidationService(project);
				}
			}
		}

		public void projectChanged(ECPProject project, boolean opened) throws Exception {
		}

		public void objectsChanged(ECPProject project, Object[] objects, boolean structural) throws Exception {
		}

		public Object[] objectsChanged(ECPProject project, Object[] objects) throws Exception {
			Set<EObject> allAffectedElements = new HashSet<EObject>();
			
			for (Object object : objects) {
				if (object instanceof EObject) {
					EObject eObject = (EObject) object;
					
					if (eObject.eContainer() == null) {
						// if an object was deleted it must be removed from the cachedTree.
						for (EObject childObject : eObject.eContents()) {
							getValidationService(project).remove(childObject);
						}
						getValidationService(project).remove(eObject);
					} else {
					  Set<EObject> affected = getValidationService(project).validate(eObject);
					  allAffectedElements.addAll(affected);
					}
				}
			}
			return allAffectedElements.toArray();
		}		
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
