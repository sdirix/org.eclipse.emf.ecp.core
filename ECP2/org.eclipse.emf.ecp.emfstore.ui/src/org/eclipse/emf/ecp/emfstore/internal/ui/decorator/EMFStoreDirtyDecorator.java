/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 ******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.observer.ECPObserverBus;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectPreDeleteObserver;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.emfstore.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.ILocalProject;
import org.eclipse.emf.emfstore.client.model.observer.ESCommitObserver;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.server.model.IChangePackage;
import org.eclipse.emf.emfstore.server.model.versionspec.IPrimaryVersionSpec;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugen Neufeld
 */
public class EMFStoreDirtyDecorator implements ILightweightLabelDecorator, ESCommitObserver, IECPProjectPreDeleteObserver {

	private String dirtyPath = "icons/dirty.png";
	private Map<ECPProject, EMFStoreDirtyObserver> observers = new HashMap<ECPProject, EMFStoreDirtyObserver>();

	/**
	 * Instantiates a new EMFStoreDirtyDecorator.
	 */
	public EMFStoreDirtyDecorator() {
		super();
		ECPObserverBus.getInstance().register(this);
	}

	/** {@inheritDoc} */
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof ECPProject) {
			InternalProject project = (InternalProject) element;
			if (ECPProjectManager.INSTANCE.getProject(project.getName()) == null) {
				return;
			}
			ProjectSpace projectSpace = EMFStoreProvider.INSTANCE.getProjectSpace(project);

			if (!observers.containsKey(element)) {
				EMFStoreDirtyObserver emfStoreDirtyObserver = new EMFStoreDirtyObserver(projectSpace, project);
				projectSpace.getOperationManager().addOperationListener(emfStoreDirtyObserver);
				observers.put((ECPProject) element, emfStoreDirtyObserver);
			}
			if (project.isOpen() && EMFStoreProvider.INSTANCE.getProjectSpace(project).isShared()
				&& EMFStoreDirtyDecoratorCachedTree.getInstance(project).getRootValue() > 0) {
				decoration.addOverlay(Activator.getImageDescriptor(dirtyPath), IDecoration.BOTTOM_LEFT);
			}
		}

		else if (element instanceof EObject) {
			InternalProject project = null;
			try {
				project = (InternalProject) ECPProjectManager.INSTANCE.getProject(element);
			} catch (IllegalArgumentException iae) {
				// ignore
			}
			if (project != null && project.isOpen() && EMFStoreProvider.INSTANCE.getProjectSpace(project).isShared()
				&& EMFStoreDirtyDecoratorCachedTree.getInstance(project).getCachedValue(element) > 0) {
				decoration.addOverlay(Activator.getImageDescriptor(dirtyPath), IDecoration.BOTTOM_LEFT);
			} else {
				decoration.addOverlay(null);
			}
			return;
		}

	}

	/** {@inheritDoc} */
	public void addListener(ILabelProviderListener listener) {
	}

	/** {@inheritDoc} */
	public void dispose() {
	}

	/** {@inheritDoc} */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/** {@inheritDoc} */
	public void removeListener(ILabelProviderListener listener) {
	}

	/** {@inheritDoc} */
	public boolean inspectChanges(ILocalProject project, IChangePackage changePackage, IProgressMonitor monitor) {
		return true;
	}

	/** {@inheritDoc} */
	public void commitCompleted(ILocalProject localProject, IPrimaryVersionSpec newRevision, IProgressMonitor monitor) {
		// TODO: cast
		ECPProject project = EMFStoreProvider.INSTANCE.getProject((ProjectSpace) localProject);
		EMFStoreDirtyDecoratorCachedTree.getInstance(project).clear();
	}

	/** {@inheritDoc} */
	public void projectDelete(ECPProject project) {
		EMFStoreDirtyDecoratorCachedTree.removeProject(project);

		observers.remove(project);
	}

}
