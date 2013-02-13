/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.ui.model;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedUIObserver;

import org.eclipse.jface.viewers.LabelProviderChangedEvent;

/**
 * @author Eike Stepper
 */
public class ModelLabelProvider extends ECPLabelProvider implements IECPProjectsChangedUIObserver {

	public ModelLabelProvider(ECPModelContextProvider modelContextProvider) {
		super(modelContextProvider);
		ECPProjectManager.INSTANCE.addObserver(this);
	}

	@Override
	public void dispose() {
		ECPProjectManager.INSTANCE.removeObserver(this);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ECPProject) {
			ECPProject project = (ECPProject) element;
			return project.getName();
		}

		return super.getText(element);
	}

	/** {@inheritDoc} */
	public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {
		// Do nothing
	}

	/** {@inheritDoc} */
	public void projectChanged(final ECPProject project, boolean opened) throws Exception {
		fireEvent(new LabelProviderChangedEvent(this, project));
	}

	/** {@inheritDoc} */
	public void objectsChanged(ECPProject project, Object[] objects, boolean structural) throws Exception {
		// Do nothing
	}
}
