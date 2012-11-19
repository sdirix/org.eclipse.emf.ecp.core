/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedUIObserver;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;

import java.util.HashSet;

/**
 * Project change observer that marks elements as dirty.
 */
public class EMFStoreDirtyObserver implements IECPProjectsChangedUIObserver {

	private HashSet<Object> excludedObjects;

	/**
	 * Default constructor.
	 */
	public EMFStoreDirtyObserver() {

	}

	// BEGIN SUPRESS CATCH EXCEPTION
	public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {

	}

	public void projectChanged(ECPProject project, boolean opened) throws Exception {

	}

	public void objectsChanged(ECPProject project, Object[] objects, boolean structural) throws Exception {

		loadExcludedObjects();

		if (EMFStoreProvider.INSTANCE.getProjectSpace((InternalProject) project).isShared()) {
			return;
		}

		for (Object object : objects) {

			if (!(object instanceof EObject)) {
				continue;
			}

			EObject eObject = (EObject) object;

			if (project.contains(eObject)) {
				EMFStoreDirtyDecoratorCachedTree.getInstance().update(eObject, Boolean.TRUE, excludedObjects);
			} else {
				if (eObject.eContainer() != null) {
					EMFStoreDirtyDecoratorCachedTree.getInstance().update(eObject.eContainer(), Boolean.TRUE,
						excludedObjects);
					EMFStoreDirtyDecoratorCachedTree.getInstance().remove(eObject, excludedObjects);
				}
			}
		}
	}

	// END SUPRESS CATCH EXCEPTION

	/**
	 * 
	 */
	private void loadExcludedObjects() {
		if (excludedObjects == null) {
			excludedObjects = new HashSet<Object>();
			for (ECPProject project : ECPProjectManager.INSTANCE.getProjects()) {
				excludedObjects.add(project);
				excludedObjects.add(project.getModelRoot());
			}
		}
	}
}