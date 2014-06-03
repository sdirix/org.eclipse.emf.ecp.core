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
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * Helper methods for dealing with ecores.
 * 
 * @author Alexandra Buzila
 * 
 */
public final class EcoreHelper {

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

		// put the package in the registry
		final URI uri = URI.createPlatformResourceURI(ecorePath, false);
		final EPackage ePackage = getEPackage(uri);
		if (ePackage == null) {
			return;
		}
		if (isContainedInPackageRegistry(ePackage.getNsURI())) {
			return;
		}
		EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
		{

		}
		// load the resource
		final IResource f = ResourcesPlugin.getWorkspace().getRoot().findMember(ecorePath);
		final ResourceSet rs = new ResourceSetImpl();
		final Resource r = rs.createResource(URI.createURI(f.getLocationURI().toString()));
		r.load(null);

		// resolve the proxies
		int rsSize = rs.getResources().size();
		EcoreUtil.resolveAll(rs);
		while (rsSize != rs.getResources().size()) {
			EcoreUtil.resolveAll(rs);
			rsSize = rs.getResources().size();
		}
		final EPackage ep = (EPackage) r.getContents().get(0);
		for (final EClassifier classifier : ep.getEClassifiers()) {
			if (EClass.class.isInstance(classifier)) {
				final EClass c = (EClass) classifier;
				// register all referenced ecores
				for (final EReference ref : c.getEAllReferences()) {
					final EClass refClass = (EClass) ref.getEType();
					if (isContainedInPackageRegistry(refClass.getEPackage().getNsURI())) {
						continue;
					}
					final URI refClassPackageURI = EcoreUtil.getURI(refClass.eContainer());
					if (!refClassPackageURI.isPlatform()) {
						final IPath path = new Path(refClassPackageURI.toFileString());
						final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);

						EcoreHelper.registerEcore(file.getFullPath().toString());
					}
				}
			}
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
		final URI uri = URI.createPlatformResourceURI(ecorePath, false);
		final EPackage ePackage = getEPackage(uri);
		unregisterEcore(ePackage);
	}

	/**
	 * Remove the ecore's {@link EPackage} from the {@link org.eclipse.emf.ecore.EPackage.Registry}.
	 * It also removes the packages of referenced ecores.
	 * 
	 * @param ePackage - the ePackage to be removed.
	 * 
	 * */
	private static void unregisterEcore(EPackage ePackage) {
		if (ePackage == null) {
			return;
		}
		if (ePackage.eResource().getURI().scheme().equals("platform")) { //$NON-NLS-1$
			return;
		}

		final Registry instance = org.eclipse.emf.ecore.EPackage.Registry.INSTANCE;
		if (!instance.containsKey(ePackage.getNsURI())) {
			return;
		}
		instance.remove(ePackage.getNsURI());
		for (final EClassifier classifier : ePackage.getEClassifiers()) {
			if (EClass.class.isInstance(classifier)) {
				final EClass c = (EClass) classifier;
				// unregister all referenced packages

				// FIXME need to count usages
				for (final EReference ref : c.getEReferences()) {
					final EClass refClass = (EClass) ref.getEType();
					unregisterEcore((EPackage) refClass.eContainer());
				}
			}
		}
	}

	private static EPackage getEPackage(URI ecorePlatformResourceURI) {

		EPackage ePackage = null;

		final ResourceSet resourceSet = new ResourceSetImpl();
		final Map<String, Object> map = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl()); //$NON-NLS-1$

		// ensure URI is platform URI
		if (!ecorePlatformResourceURI.isPlatform()) {
			ecorePlatformResourceURI = URI.createPlatformResourceURI(ecorePlatformResourceURI.toString(), false);
		}

		if (!ecorePlatformResourceURI.segment(0).equals("resource")) { //$NON-NLS-1$
			return null;
		}
		final Resource ecore = resourceSet.getResource(ecorePlatformResourceURI, true);

		if (ecore != null) {

			final EList<EObject> contents = ecore.getContents();
			if (contents.size() != 1) {
				return null;
			}

			final EObject object = contents.get(0);
			if (!(object instanceof EPackage)) {
				return null;
			}

			ePackage = (EPackage) object;

			// correct hrefs
			ecore.getContents().remove(ePackage);
			final Resource resource2 = resourceSet.createResource(URI.createURI(ePackage.getNsURI()));
			resource2.getContents().add(ePackage);

		}
		return ePackage;
	}

}
