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
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.observers.OperationObserver;
import org.eclipse.emf.emfstore.common.model.ModelElementId;
import org.eclipse.emf.emfstore.common.model.Project;
import org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation;

/**
 * Project change observer that marks elements as dirty.
 */
public class EMFStoreDirtyObserver implements OperationObserver {

	private ProjectSpace projectSpace;
	private InternalProject internalProject;

	/**
	 * Default constructor.
	 * 
	 * @param project
	 */
	public EMFStoreDirtyObserver(ProjectSpace projectSpace, InternalProject project) {
		this.projectSpace = projectSpace;
		internalProject = project;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.emfstore.client.model.observers.OperationObserver#operationExecuted(org.eclipse.emf.emfstore.
	 * server.model.versioning.operations.AbstractOperation)
	 */
	public void operationExecuted(AbstractOperation operation) {
		for (ModelElementId modelElementId : operation.getAllInvolvedModelElements()) {
			Project project = projectSpace.getProject();
			EObject element = project.getModelElement(modelElementId);

			EMFStoreDirtyDecoratorCachedTree.getInstance(internalProject).addOperation(element);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.emfstore.client.model.observers.OperationObserver#operationUnDone(org.eclipse.emf.emfstore.server
	 * .model.versioning.operations.AbstractOperation)
	 */
	public void operationUnDone(AbstractOperation operation) {
		for (ModelElementId modelElementId : operation.getAllInvolvedModelElements()) {
			Project project = projectSpace.getProject();
			EObject element = project.getModelElement(modelElementId);

			EMFStoreDirtyDecoratorCachedTree.getInstance(internalProject).removeOperation(element);
		}

	}

	// END SUPRESS CATCH EXCEPTION
}