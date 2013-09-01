/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.provider.xmi;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecp.internal.view.model.provider.xmi.Activator;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * @author Jonas
 *         Manages the view models provided by the file extension point.
 * 
 */
public final class ViewModelFileExtensionsManager {

	private ViewModelFileExtensionsManager() {
	}

	private static ViewModelFileExtensionsManager instance;

	/**
	 * @return the iNSTANCE
	 */
	public static ViewModelFileExtensionsManager getInstance() {
		if (instance == null) {
			instance = new ViewModelFileExtensionsManager();
			instance.init();
		}
		return instance;
	}

	/**
	 * 
	 */
	private void init() {
		// TODO Auto-generated method stub

	}

	/**
	 * Loads a resource containing a view model.
	 * 
	 * @param xmiPath a URI containing the path to the file
	 * @return the loaded resource
	 */
	public static Resource loadResource(URI xmiPath) {
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Map<String, Object> extensionToFactoryMap = resourceSet
			.getResourceFactoryRegistry().getExtensionToFactoryMap();
		extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
			new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ViewPackage.eNS_URI,
			ViewPackage.eINSTANCE);
		final Resource resource = resourceSet.createResource(xmiPath);
		try {
			resource.load(null);
		} catch (final IOException exception) {
			Activator.log(exception);
		}
		return resource;
	}

	/**
	 * Disposed the instance.
	 */
	public static void dispose() {
		instance = null;
	}

}
