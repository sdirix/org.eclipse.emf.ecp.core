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
package org.eclipse.emf.ecp.explorereditorbridge;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedUIObserver;
import org.eclipse.emf.ecp.edit.EditMetaModelContext;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.EditModelElementContextListener;
import org.eclipse.emf.ecp.ui.util.ActionHelper;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.databinding.DataBindingContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Eugen Neufeld
 * 
 */
public class EditorContext implements EditModelElementContext {

	private final EObject modelElement;

	private final ECPProject ecpProject;

	// TODO what is it god for
	private MetaModeElementContext metaModeElementContext;

	private List<EditModelElementContextListener> contextListeners = new ArrayList<EditModelElementContextListener>();

	private IECPProjectsChangedUIObserver projectObserver;

	public EditorContext(EObject modelElement, ECPProject ecpProject) {
		this.modelElement = modelElement;
		this.ecpProject = ecpProject;
		metaModeElementContext = new MetaModeElementContext();

		projectObserver = new IECPProjectsChangedUIObserver() {

			public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {
				// TODO Auto-generated method stub

			}

			public void projectChanged(ECPProject project, boolean opened) throws Exception {
				if (!opened) {
					for (EditModelElementContextListener contextListener : contextListeners) {
						contextListener.onContextDeleted();
					}
					dispose();
				}
			}

			public void objectsChanged(ECPProject project, Object[] objects, boolean structural) throws Exception {
				// if we have a structural change (otherwise nothing should be closed), and the change is in our project
				// and our model element is no longer contained
				// then we notify about deletion and dispose ourself
				if (structural && EditorContext.this.ecpProject.equals(project)
					&& !project.contains(EditorContext.this.modelElement)) {
					for (EditModelElementContextListener contextListener : contextListeners) {
						contextListener.onModelElementDeleted(EditorContext.this.modelElement);
					}
					dispose();
				}
			}

		};
		ECPProjectManager.INSTANCE.addObserver(projectObserver);
	}

	public void addModelElementContextListener(EditModelElementContextListener modelElementContextListener) {
		contextListeners.add(modelElementContextListener);
	}

	public void removeModelElementContextListener(EditModelElementContextListener modelElementContextListener) {
		contextListeners.remove(modelElementContextListener);
	}

	public Collection<EObject> getAllModelElementsbyClass(EClass clazz, boolean association) {
		// TODO Auto-generated method stub
		return null;
	}

	public EditingDomain getEditingDomain() {
		if (ecpProject != null) {
			return ecpProject.getEditingDomain();
		}
		return AdapterFactoryEditingDomain.getEditingDomainFor(modelElement);

	}

	public EditMetaModelContext getMetaModelElementContext() {
		// TODO Auto-generated method stub
		return metaModeElementContext;
	}

	public boolean contains(EObject eObject) {
		return ecpProject.contains(eObject);
	}

	/**
	 * Dispose the context.
	 */
	public void dispose() {
		ECPProjectManager.INSTANCE.removeObserver(projectObserver);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#getLinkElements(org.eclipse.emf.ecore.EReference)
	 */
	public Iterator<EObject> getLinkElements(EReference eReference) {
		return ecpProject.getReferenceCandidates(modelElement, eReference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#openEditor(org.eclipse.emf.ecore.EObject)
	 */
	public void openEditor(EObject o, String source) {
		// TODO only elements of the same project?
		ActionHelper.openModelElement(o, source, ecpProject);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#addModelElement(org.eclipse.emf.ecore.EObject)
	 */
	public void addModelElement(EObject newMEInstance) {
		ecpProject.getElements().add(newMEInstance);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#isDirty()
	 */
	public boolean isDirty() {
		// auto save
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#save()
	 */
	public void save() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#getModelElement()
	 */
	public EObject getModelElement() {
		return modelElement;
	}

	private EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#getDataBindingContext()
	 */
	public DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}
}
