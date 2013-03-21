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
package org.eclipse.emf.ecp.explorereditorbridge.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedUIObserver;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.ECPEditorContext;
import org.eclipse.emf.ecp.edit.EditModelElementContextListener;

import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.List;

/**
 * An EditorContext depending on an {@link ECPProject}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class EditorContext implements ECPEditorContext {

	/**
	 * @author Jonas
	 * 
	 */
	private final class IECPProjectsChangedUIObserverImplementation implements IECPProjectsChangedUIObserver {
		/** {@inheritDoc} */
		public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {
			// TODO Auto-generated method stub
			if (!ECPUtil.containsElement(newProjects, ecpProject)) {
				for (EditModelElementContextListener contextListener : contextListeners) {
					contextListener.onContextDeleted();
				}
				dispose();
			}
		}

		/** {@inheritDoc} */
		public void projectChanged(ECPProject project, boolean opened) throws Exception {
			if (!opened) {
				for (EditModelElementContextListener contextListener : contextListeners) {
					contextListener.onContextDeleted();
				}
				dispose();
			}
		}

		/** {@inheritDoc} */
		public void objectsChanged(ECPProject project, Object[] objects, boolean structural) throws Exception {
			// if we have a structural change (otherwise nothing should be closed), and the change is in our project
			// and our model element is no longer contained
			// then we notify about deletion and dispose ourself
			if (structural && ecpProject.equals(project) && !project.contains(modelElement)) {
				for (EditModelElementContextListener contextListener : contextListeners) {
					contextListener.onModelElementDeleted(modelElement);
				}
				dispose();
			}
		}
	}

	private List<EditModelElementContextListener> contextListeners = new ArrayList<EditModelElementContextListener>();

	private IECPProjectsChangedUIObserver projectObserver;

	private ECPControlContextImpl ecpControlContextImpl;

	private EObject modelElement;

	private ECPProject ecpProject;

	public EditorContext(EObject modelElement, ECPProject ecpProject, Shell shell) {
		this.modelElement = modelElement;
		this.ecpProject = ecpProject;
		ecpControlContextImpl = new ECPControlContextImpl(modelElement, ecpProject, shell);
		projectObserver = new IECPProjectsChangedUIObserverImplementation();
		ECPProjectManager.INSTANCE.addObserver(projectObserver);
	}

	/** {@inheritDoc} */
	public void addModelElementContextListener(EditModelElementContextListener modelElementContextListener) {
		contextListeners.add(modelElementContextListener);
	}

	/** {@inheritDoc} */
	public void removeModelElementContextListener(EditModelElementContextListener modelElementContextListener) {
		contextListeners.remove(modelElementContextListener);
	}

	/**
	 * Dispose the context.
	 */
	public void dispose() {
		ECPProjectManager.INSTANCE.removeObserver(projectObserver);
	}

	/** {@inheritDoc} */
	public boolean isDirty() {
		// auto save
		return false;
	}

	/** {@inheritDoc} */
	public void save() {
		// do nothing
	}

	/** {@inheritDoc} */
	public EObject getModelElement() {
		return modelElement;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.ECPEditorContext#getECPControlContext()
	 */
	public ECPControlContext getECPControlContext() {
		return ecpControlContextImpl;
	}
}
