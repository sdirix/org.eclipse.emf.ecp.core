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
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.emfstore.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.observers.CommitObserver;
import org.eclipse.emf.emfstore.server.model.versioning.ChangePackage;
import org.eclipse.emf.emfstore.server.model.versioning.PrimaryVersionSpec;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eugen Neufeld
 */
public class EMFStoreDirtyDecorator implements ILightweightLabelDecorator, CommitObserver {

	private String dirtyPath = "icons/dirty.png";
	private Set<InternalProject> observers = new HashSet<InternalProject>();

	/** {@inheritDoc} */
	public void decorate(Object element, IDecoration decoration) {

		if (element instanceof ECPProject) {
			InternalProject project = (InternalProject) element;
			ProjectSpace projectSpace = EMFStoreProvider.INSTANCE.getProjectSpace(project);

			if (!observers.contains(element)) {
				projectSpace.getOperationManager().addOperationListener(
					new EMFStoreDirtyObserver(projectSpace, project));
				observers.add(project);
			}
			if (project.isOpen() && EMFStoreProvider.INSTANCE.getProjectSpace(project).isShared()
				&& EMFStoreDirtyDecoratorCachedTree.getInstance(project).getRootValue().shouldDisplayDirtyIndicator()) {
				decoration.addOverlay(Activator.getImageDescriptor(dirtyPath), IDecoration.BOTTOM_LEFT);
			}
		}

		if (element instanceof EObject) {
			InternalProject project = null;
			try {
				project = ECPUtil.getECPProject(element, InternalProject.class);
			} catch (IllegalArgumentException iae) {
				// ignore
			}
			if (project != null
				&& project.isOpen()
				&& EMFStoreProvider.INSTANCE.getProjectSpace(project).isShared()
				&& EMFStoreDirtyDecoratorCachedTree.getInstance(project).getCachedValue(element)
					.shouldDisplayDirtyIndicator()) {
				decoration.addOverlay(Activator.getImageDescriptor(dirtyPath), IDecoration.BOTTOM_LEFT);
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
	public boolean inspectChanges(ProjectSpace projectSpace, ChangePackage changePackage) {
		return true;
	}

	/** {@inheritDoc} */
	public void commitCompleted(ProjectSpace projectSpace, PrimaryVersionSpec newRevision) {
		ECPProject project = EMFStoreProvider.INSTANCE.getProject(projectSpace);
		EMFStoreDirtyDecoratorCachedTree.getInstance(project).clear();
	}
}
