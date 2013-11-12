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
 */
package org.eclipse.emf.ecp.view.template.service;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecp.view.template.model.VTTemplatePackage;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplate;
import org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider;
import org.osgi.framework.BundleContext;

/**
 * Implementation of the VTViewTemplateProvider.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ViewTemplateProviderImpl implements VTViewTemplateProvider {

	private VTViewTemplate registeredTemplate;

	/**
	 * Startup method for osgi service.
	 * 
	 * @param bundleContext the {@link BundleContext}
	 */
	protected void startup(BundleContext bundleContext) {

		// load from extension
		final IConfigurationElement[] controls = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.ecp.view.template"); //$NON-NLS-1$
		if (controls.length != 1) {
			return;
		}
		final IConfigurationElement e = controls[0];
		final String xmiResource = e.getAttribute("xmi"); //$NON-NLS-1$
		final Resource resource = loadResource(URI.createURI("platform:/plugin/" + e.getContributor().getName() + "/" //$NON-NLS-1$ //$NON-NLS-2$
			+ xmiResource));
		if (resource == null) {
			return;
		}
		if (resource.getContents().size() != 1) {
			return;
		}
		registeredTemplate = (VTViewTemplate) resource.getContents().get(0);

	}

	private static Resource loadResource(URI uri) {
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Map<String, Object> extensionToFactoryMap = resourceSet
			.getResourceFactoryRegistry().getExtensionToFactoryMap();
		extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
			new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(VTTemplatePackage.eNS_URI,
			VTTemplatePackage.eINSTANCE);
		final Resource resource = resourceSet.createResource(uri);
		try {
			resource.load(null);
		} catch (final IOException exception) {

		}
		return resource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.template.model.VTViewTemplateProvider#getViewTemplate()
	 */
	public VTViewTemplate getViewTemplate() {
		if (registeredTemplate != null) {
			return registeredTemplate;
		}
		return null;
	}

}
