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
 * Johannes Faltermeier - refactorings
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ide.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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

	private static final String ECORE_PLUGIN_URI = "platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"; //$NON-NLS-1$
	private static final String ECORE_RESOURCE_URI = "platform:/resource/org.eclipse.emf.ecore/model/Ecore.ecore"; //$NON-NLS-1$

	/**
	 * Contains mapping between an ecore path and the platform resource URIs of all the EPackages that ecore requires.
	 */
	private static final Map<String, Set<String>> ECOREPATH_TO_WORKSPACEURIS = new HashMap<String, Set<String>>();

	/**
	 * The number of open view model editors for an ecore path.
	 */
	private static final Map<String, Integer> ECOREPATH_TO_REGISTEREDCOUNT = new HashMap<String, Integer>();

	/**
	 * A set of all namespace uris that were registerd by the tooling.
	 */
	private static final Set<String> ALL_NSURIS_REGISTERED_BY_TOOLING = new HashSet<String>();

	/** Local EPackage Registry. */
	private static final Map<String, EPackage> WORKSPACEURI_TO_REGISTEREDPACKAGE = new LinkedHashMap<String, EPackage>();

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

		Integer previousValue = ECOREPATH_TO_REGISTEREDCOUNT.get(ecorePath);
		if (previousValue == null || previousValue < 0) {
			previousValue = 0;
		}
		ECOREPATH_TO_REGISTEREDCOUNT.put(ecorePath, ++previousValue);

		// determine workspace dependencies of this ecore
		determineWorkspaceDepedencies(ecorePath);

		// actually load the ecore reusing already loaded packages from the workspace
		final ResourceSet physicalResourceSet = new ResourceSetImpl();
		initResourceSet(physicalResourceSet, true);
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
			if (physicalResource.getContents().size() == 0) {
				continue;
			}
			final EObject eObject = physicalResource.getContents().get(0);
			final EPackage ePackage = EPackage.class.cast(eObject);

			if (isContainedInPackageRegistry(ePackage.getNsURI())) {
				continue;
			}
			final String platformResourceURI = physicalResource.getURI().toString();
			physicalResource.getContents().remove(ePackage);
			final Resource virtualResource = virtualResourceSet.createResource(URI.createURI(ePackage.getNsURI()));
			virtualResource.getContents().add(ePackage);
			EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
			ALL_NSURIS_REGISTERED_BY_TOOLING.add(ePackage.getNsURI());
			WORKSPACEURI_TO_REGISTEREDPACKAGE.put(platformResourceURI, ePackage);
		}

	}

	/**
	 * Determines the dependent EPackages present in the user's workspace for this ecore path.
	 *
	 * @param ecorePath
	 * @return
	 * @throws IOException
	 */
	private static URI determineWorkspaceDepedencies(String ecorePath) throws IOException {
		final ResourceSet physicalResourceSet = new ResourceSetImpl();
		initResourceSet(physicalResourceSet, false);
		final URI uri = URI.createPlatformResourceURI(ecorePath, false);
		final Resource tempResource = physicalResourceSet.createResource(uri);
		tempResource.load(null);
		// resolve the proxies
		int tempSize = physicalResourceSet.getResources().size();
		EcoreUtil.resolveAll(physicalResourceSet);
		while (tempSize != physicalResourceSet.getResources().size()) {
			EcoreUtil.resolveAll(physicalResourceSet);
			tempSize = physicalResourceSet.getResources().size();
		}
		for (final Resource physicalResource : physicalResourceSet.getResources()) {
			if (physicalResource.getContents().size() == 0) {
				continue;
			}
			if (!physicalResource.getURI().isPlatformResource()) {
				continue;
			}
			if (ECOREPATH_TO_WORKSPACEURIS.get(ecorePath) == null) {
				ECOREPATH_TO_WORKSPACEURIS.put(ecorePath, new HashSet<String>());
			}
			ECOREPATH_TO_WORKSPACEURIS.get(ecorePath).add(physicalResource.getURI().toString());
		}
		return uri;
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
		if (ecorePath == null || ECOREPATH_TO_WORKSPACEURIS.get(ecorePath) == null) {
			return;
		}
		int usages = ECOREPATH_TO_REGISTEREDCOUNT.get(ecorePath);
		ECOREPATH_TO_REGISTEREDCOUNT.put(ecorePath, --usages);
		if (usages > 0) {
			return;
		}
		final Set<String> workspaceURIsNeededForEcorePath = ECOREPATH_TO_WORKSPACEURIS.remove(ecorePath);
		unregisterEcore(ecorePath, workspaceURIsNeededForEcorePath);
	}

	/**
	 * Remove the ecore's {@link EPackage} from the {@link org.eclipse.emf.ecore.EPackage.Registry}.
	 * It also removes the packages of referenced ecores (if needed).
	 *
	 */
	private static void unregisterEcore(String ecorePath, Set<String> workspaceURIsNeededByEcorePath) {
		if (workspaceURIsNeededByEcorePath == null || ecorePath == null) {
			return;
		}

		// check if other ecores need a workspace URI from the set
		final Set<String> workspaceURIsToRemove = new LinkedHashSet<String>();
		for (final String workspaceURI : workspaceURIsNeededByEcorePath) {
			boolean okToRemove = true;
			for (final Set<String> otherNeededWSURIs : ECOREPATH_TO_WORKSPACEURIS.values()) {
				if (otherNeededWSURIs.contains(workspaceURI)) {
					okToRemove = false;
					break;
				}
			}
			if (okToRemove) {
				workspaceURIsToRemove.add(workspaceURI);
			}
		}

		// unregister no longer needed workspace URIs
		for (final String toRemove : workspaceURIsToRemove) {
			final EPackage pkgToRemove = WORKSPACEURI_TO_REGISTEREDPACKAGE.remove(toRemove);
			EPackage.Registry.INSTANCE.remove(pkgToRemove.getNsURI());
			ALL_NSURIS_REGISTERED_BY_TOOLING.remove(pkgToRemove.getNsURI());
		}
	}

	/**
	 * @return the EPackages which are registered in the EPackage registry by default (without the ones registered
	 *         during runtime by the tooling).
	 */
	public static Object[] getDefaultPackageRegistryContents() {

		final Set<String> packages = new HashSet<String>();
		packages.addAll(EPackage.Registry.INSTANCE.keySet());
		packages.removeAll(ALL_NSURIS_REGISTERED_BY_TOOLING);
		return packages.toArray();
	}

	/*
	 * If the Ecore.ecore is referenced with a resource uri in an ecore-file, the ecore editor is able to load this file
	 * even if the Ecore.ecore is not available in the workspace. Therefore those ecores should be regarded as valid.
	 * Hence we need to remap the resource uri to a plugin uri if the Ecore.ecore is not available in the workspace
	 * in order to resolve the proxy.
	 * Moreover when a new ecore is loaded we need to prevent EMF to resolve proxies to other platform-resource URIs
	 * with the package registry
	 */
	private static void initResourceSet(ResourceSet resourceSet, boolean withLocalRegistry) {
		if (withLocalRegistry) {
			resourceSet.getPackageRegistry().putAll(WORKSPACEURI_TO_REGISTEREDPACKAGE);
		}
		if (!resourceSet.getURIConverter().exists(
			URI.createURI(ECORE_RESOURCE_URI), null)) {

			resourceSet.getURIConverter().getURIMap()
				.put(URI.createURI(ECORE_RESOURCE_URI), URI.createURI(ECORE_PLUGIN_URI));
		}
	}
}
