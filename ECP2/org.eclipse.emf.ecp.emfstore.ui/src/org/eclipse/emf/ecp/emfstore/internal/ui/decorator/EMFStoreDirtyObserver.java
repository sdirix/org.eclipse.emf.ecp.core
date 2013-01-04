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
 *******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.observers.OperationObserver;
import org.eclipse.emf.emfstore.common.model.ModelElementId;
import org.eclipse.emf.emfstore.common.model.Project;
import org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation;

import java.util.HashSet;
import java.util.Set;

/**
 * Project change observer that marks elements as dirty.
 */
public class EMFStoreDirtyObserver implements OperationObserver {

	private ProjectSpace projectSpace;
	private InternalProject internalProject;

	/**
	 * Default constructor.
	 * 
	 * @param projectSpace the ProjectSpace of the decorator
	 * @param project the ecpproject of the decorator
	 */
	public EMFStoreDirtyObserver(ProjectSpace projectSpace, InternalProject project) {
		this.projectSpace = projectSpace;
		internalProject = project;

		if (!projectSpace.isShared()) {
			return;
		}
		for (AbstractOperation operation : projectSpace.getOperations()) {
			for (ModelElementId modelElementId : operation.getAllInvolvedModelElements()) {
				EObject element = projectSpace.getProject().getModelElement(modelElementId);
				if (element != null) {
					EMFStoreDirtyDecoratorCachedTree.getInstance(internalProject).addOperation(element);
				}
			}
		}

	}

	private Set<EObject> lastAffected = null;

	/** {@inheritDoc} */
	public void operationExecuted(AbstractOperation operation) {

		if (!projectSpace.isShared()) {
			return;
		}
		lastAffected = new HashSet<EObject>();
		for (ModelElementId modelElementId : operation.getAllInvolvedModelElements()) {
			Project project = projectSpace.getProject();

			EObject element = project.getModelElement(modelElementId);

			if (element != null) {
				lastAffected
					.addAll(EMFStoreDirtyDecoratorCachedTree.getInstance(internalProject).addOperation(element));
			}
		}

	}

	/** {@inheritDoc} */
	public void operationUnDone(AbstractOperation operation) {

		if (!projectSpace.isShared()) {
			return;
		}

		for (ModelElementId modelElementId : operation.getAllInvolvedModelElements()) {
			Project project = projectSpace.getProject();
			EObject element = project.getIdToEObjectMapping().get(modelElementId.getId());
			if (element != null) {
				lastAffected = EMFStoreDirtyDecoratorCachedTree.getInstance(internalProject).removeOperation(element);
			}
		}

	}

	public Set<EObject> getLastAffected() {
		return lastAffected;
	}

}