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
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedUIObserver;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;

import java.util.Arrays;

/**
 * @author Eike Stepper
 */
public class ModelContentProvider extends ECPContentProvider<ECPProjectManager> implements IECPProjectsChangedUIObserver {
	public ModelContentProvider() {
	}

	public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {
		refreshViewer();
	}

	public void projectChanged(ECPProject project, boolean opened) throws Exception {
		refreshViewer(true, project);
	}

	public void objectsChanged(ECPProject project, Object[] objects, boolean structural) throws Exception {
		refreshViewer(structural, objects);
		if (!Arrays.asList(objects).contains(project)) {
			refreshViewer(false, project);
		}
	}

	@Override
	protected void connectInput(ECPProjectManager input) {
		ECPProjectManager.INSTANCE.addObserver(this);
	}

	@Override
	protected void disconnectInput(ECPProjectManager input) {
		ECPProjectManager.INSTANCE.removeObserver(this);
	}

	@Override
	protected void fillChildren(Object parent, InternalChildrenList childrenList) {
		if (parent == ECPProjectManager.INSTANCE) {
			childrenList.addChildren(ECPProjectManager.INSTANCE.getProjects());
		} else {
			super.fillChildren(parent, childrenList);
		}
	}
}
