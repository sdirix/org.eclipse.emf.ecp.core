/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ide.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Helper methods for dealing with ecores.
 * 
 * @author Alexandra Buzila
 * 
 */
public final class EcoreHelper {

	/** Contains mapping between an ecore path and the ns-uris of all the EPackages that ecore registered. */
	private static final Map<String, Set<String>> registeredEPackages = new HashMap<String, Set<String>>();

	private EcoreHelper() {
	}

	/**
	 * Put an ecore's {@link EPackage} into the {@link org.eclipse.emf.ecore.EPackage.Registry}. Subsequently, register
	 * all referenced ecores.
	 * 
	 * @param ecorePath - path to the ecore
	 * @throws IOException if resource cannot be loaded
	 * 
	 * */

	public static void registerEcore(String ecorePath) throws IOException {
		if (ecorePath == null) {
			return;
		}

		final ResourceSet physicalResourceSet = new ResourceSetImpl();
		// put the package in the registry
		final URI uri = URI.createPlatformResourceURI(ecorePath, false);
		final Resource r = physicalResourceSet.createResource(uri);
		r.load(null);

		// resolve the proxies
		int rsSize = physicalResourceSet.getResources().size();
		EcoreUtil.resolveAll(physicalResourceSet);
		while (rsSize != physicalResourceSet.getResources().size()) {
			EcoreUtil.resolveAll(physicalResourceSet);
			rsSize = physicalResourceSet.getResources().size();
		}
		final ResourceSetImpl virtualResourceSet = new ResourceSetImpl();
		for (final Resource physicalResource : physicalResourceSet.getResources()) {
			// check for physical uri
			final EObject eObject = physicalResource.getContents().get(0);
			final EPackage ePackage = EPackage.class.cast(eObject);

			// add ePackage URI to local cache
			if (registeredEPackages.get(ecorePath) == null) {
				registeredEPackages.put(ecorePath, new HashSet<String>());
			}
			registeredEPackages.get(ecorePath).add(ePackage.getNsURI());
			if (isContainedInPackageRegistry(ePackage.getNsURI())) {
				continue;
			}
			physicalResource.getContents().remove(ePackage);
			final Resource virtualResource = virtualResourceSet.createResource(URI.createURI(ePackage.getNsURI()));
			virtualResource.getContents().add(ePackage);
			EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);

		}

	}

	private static boolean isContainedInPackageRegistry(String nsURI) {
		final Registry instance = EPackage.Registry.INSTANCE;
		return instance.containsKey(nsURI);
	}

	/**
	 * Remove the ecore's {@link EPackage} from the {@link org.eclipse.emf.ecore.EPackage.Registry}.
	 * It also removes the packages of referenced ecores.
	 * 
	 * @param ecorePath - the path of the ecore to be removed.
	 * 
	 * */
	public static void unregisterEcore(String ecorePath) {
		if (ecorePath == null || registeredEPackages.get(ecorePath) == null) {
			return;
		}
		final List<String> nsURIs = new ArrayList<String>(registeredEPackages.get(ecorePath));
		unregisterEcore(ecorePath, nsURIs);
	}

	/**
	 * Remove the ecore's {@link EPackage} from the {@link org.eclipse.emf.ecore.EPackage.Registry}.
	 * It also removes the packages of referenced ecores.
	 * 
	 * @param ePackage - the ePackage to be removed.
	 * 
	 * */
	private static void unregisterEcore(String ecorePath, List<String> nsURIs) {
		if (nsURIs == null || ecorePath == null) {
			return;
		}
		for (final String nsURI : nsURIs) {
			if (getUsageCount(nsURI) == 1) {
				final Registry registry = org.eclipse.emf.ecore.EPackage.Registry.INSTANCE;
				registry.remove(nsURI);
				registeredEPackages.get(ecorePath).remove(nsURI);
			}
		}
		registeredEPackages.remove(ecorePath);
	}

	/**
	 * @param nsURI
	 * @return the number of ecores which use the package with the given nsURI
	 */
	private static int getUsageCount(String nsURI) {
		int usage = 0;
		for (final String ecore : registeredEPackages.keySet()) {
			for (final String uri : registeredEPackages.get(ecore)) {
				if (nsURI.equals(uri)) {
					usage++;
				}
			}
		}
		return usage;
	}

	/**
	 * @return the EPackages which are registered in the EPackage registry by default (without the ones registered
	 *         during runtime by the tooling).
	 */
	public static Object[] getDefaultPackageRegistryContents() {

		final Set<String> packages = new HashSet<String>();
		packages.addAll(EPackage.Registry.INSTANCE.keySet());
		for (final Set<String> values : registeredEPackages.values()) {
			packages.removeAll(values);
		}

		return packages.toArray();
	}
}
