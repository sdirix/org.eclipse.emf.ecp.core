/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
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

/**
 * @author Eugen Neufeld
 */
public class EMFStoreDirtyDecorator implements ILightweightLabelDecorator, CommitObserver {

	private String dirtyPath = "icons/dirty.png";

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang .Object,
	 *      org.eclipse.jface.viewers.IDecoration)
	 * @param element
	 *            element
	 * @param decoration
	 *            decoration
	 */
	public void decorate(Object element, IDecoration decoration) {

		if (element instanceof EObject) {
			if (EMFStoreDirtyDecoratorCachedTree.getInstance().getCachedValue(element) == Boolean.TRUE) {
				decoration.addOverlay(Activator.getImageDescriptor(dirtyPath), IDecoration.BOTTOM_LEFT);
			}

			return;
		}

		if (element instanceof ECPProject) {
			if (EMFStoreProvider.INSTANCE.getProjectSpace((InternalProject) element).isShared()
				&& EMFStoreDirtyDecoratorCachedTree.getInstance().getRootValue() == Boolean.TRUE) {
				decoration.addOverlay(Activator.getImageDescriptor(dirtyPath), IDecoration.BOTTOM_LEFT);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse. jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
	}

	/**
	 * . {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang .Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * . {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 *      .jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean inspectChanges(ProjectSpace projectSpace, ChangePackage changePackage) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void commitCompleted(ProjectSpace projectSpace, PrimaryVersionSpec newRevision) {
		EMFStoreDirtyDecoratorCachedTree.getInstance().clear();
	}
}
