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
package org.eclipse.emf.ecp.explorereditorbridge;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedUIObserver;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.EditModelElementContextListener;
import org.eclipse.emf.ecp.ui.composites.SelectModelClassComposite;
import org.eclipse.emf.ecp.ui.util.ActionHelper;
import org.eclipse.emf.ecp.wizards.NewModelElementWizard;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * An EditorContext depending on an {@link ECPProject}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class EditorContext implements EditModelElementContext {

	private final EObject modelElement;

	private final ECPProject ecpProject;

	private List<EditModelElementContextListener> contextListeners = new ArrayList<EditModelElementContextListener>();

	private IECPProjectsChangedUIObserver projectObserver;

	private Shell shell;

	public EditorContext(EObject modelElement, ECPProject ecpProject, Shell shell) {
		this.modelElement = modelElement;
		this.shell = shell;
		this.ecpProject = ecpProject;

		projectObserver = new IECPProjectsChangedUIObserver() {
			/** {@inheritDoc} */
			public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {
				// TODO Auto-generated method stub
				if (!ECPUtil.containsElement(newProjects, EditorContext.this.ecpProject)) {
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

	/** {@inheritDoc} */
	public void addModelElementContextListener(EditModelElementContextListener modelElementContextListener) {
		contextListeners.add(modelElementContextListener);
	}

	/** {@inheritDoc} */
	public void removeModelElementContextListener(EditModelElementContextListener modelElementContextListener) {
		contextListeners.remove(modelElementContextListener);
	}

	/** {@inheritDoc} */
	public Collection<EObject> getAllModelElementsbyClass(EClass clazz, boolean association) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	public EditingDomain getEditingDomain() {
		if (ecpProject != null) {
			return ecpProject.getEditingDomain();
		}
		return AdapterFactoryEditingDomain.getEditingDomainFor(modelElement);

	}

	/** {@inheritDoc} */
	public boolean contains(EObject eObject) {
		return ecpProject.contains(eObject);
	}

	/**
	 * Dispose the context.
	 */
	public void dispose() {
		ECPProjectManager.INSTANCE.removeObserver(projectObserver);
	}

	/** {@inheritDoc} */
	public Iterator<EObject> getLinkElements(EReference eReference) {
		return ecpProject.getReferenceCandidates(modelElement, eReference);
	}

	/** {@inheritDoc} */
	public void openEditor(EObject o, String source) {
		// TODO only elements of the same project?
		ActionHelper.openModelElement(o, source, ecpProject);
	}

	/** {@inheritDoc} */
	public void addModelElement(EObject newMEInstance) {
		ecpProject.getElements().add(newMEInstance);
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

	private EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();

	/** {@inheritDoc} */
	public DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}

	/** {@inheritDoc} */
	public void createAndReferenceNewModelElement(EReference eReference) {
		Collection<EClass> classes = ECPUtil.getSubClasses(eReference.getEReferenceType());
		List<EPackage> ePackages = new ArrayList<EPackage>();
		ePackages.add(modelElement.eClass().getEPackage());
		ePackages.addAll(modelElement.eClass().getEPackage().getESubpackages());

		NewModelElementWizard wizard = new NewModelElementWizard("New Reference Element");
		SelectModelClassComposite helper = new SelectModelClassComposite(ePackages, new HashSet<EPackage>(),
			new HashSet<EPackage>(), classes);
		wizard.setCompositeProvider(helper);
		// wizard.init(ePackages, new HashSet<EPackage>(), new HashSet<EPackage>(), classes);

		// ModelElementSelectionTreeDialog dialog = new ModelElementSelectionTreeDialog(PlatformUI.getWorkbench()
		// .getActiveWorkbenchWindow().getShell(), ePackages, new HashSet<EPackage>(), new HashSet<EPackage>(),
		// classes);
		//
		// dialog.setAllowMultiple(false);
		// int result = dialog.open();
		WizardDialog wd = new WizardDialog(shell, wizard);
		// wizard.setWindowTitle("New Reference Element");
		EObject newMEInstance = null;
		int result = wd.open();

		if (result == WizardDialog.OK) {
			// Object[] dialogSelection = dialog.getResult();
			// for (Object object : dialogSelection)
			// {
			// if (object instanceof EClass)
			// {
			// EClass eClasse = (EClass)object;
			Object[] selection = helper.getSelection();
			if (selection == null || selection.length == 0) {
				return;
			}
			EClass eClasse = (EClass) selection[0];
			// 1.create ME
			EPackage ePackage = eClasse.getEPackage();
			newMEInstance = ePackage.getEFactoryInstance().create(eClasse);

			// }
			// }
		}
		if (newMEInstance == null) {
			return;
			// EClass clazz = eReference.getEReferenceType();
			// EClass newClass = null;
			// Set<EClass> subclasses = modelElementContext..getMetaModelElementContext().getAllSubEClasses(clazz,
			// false);
			// if (subclasses.size() == 1)
			// {
			// newClass = subclasses.iterator().next();
			// }
			// else
			// {
			// ElementListSelectionDialog dlg = new ElementListSelectionDialog(PlatformUI.getWorkbench()
			// .getActiveWorkbenchWindow().getShell(), new MEClassLabelProvider());
			// dlg.setMessage(DIALOG_MESSAGE);
			// dlg.setElements(subclasses.toArray());
			//
			// dlg.setTitle("Select Element type");
			// dlg.setBlockOnOpen(true);
			// if (dlg.open() != Window.OK)
			// {
			// return;
			// }
			// Object result = dlg.getFirstResult();
			// if (result instanceof EClass)
			// {
			// newClass = (EClass)result;
			// }
			// }
		}

		// if (!eReference.isContainer()) {
		//
		// // Returns the value of the Container
		// EObject parent = modelElement.eContainer();
		// while (!(parent == null) && newMEInstance.eContainer() == null) {
		// EReference reference = getMetaModelElementContext().getPossibleContainingReference(newMEInstance,
		// parent);
		// if (reference != null && reference.isMany()) {
		// Object object = parent.eGet(reference);
		// EList<EObject> eList = (EList<EObject>) object;
		// eList.add(newMEInstance);
		// }
		// parent = parent.eContainer();
		// }
		//
		// if (newMEInstance.eContainer() == null) {
		// // throw new RuntimeException("No matching container for model element found");
		// addModelElement(newMEInstance);
		// }
		//
		// }

		// add the new object to the reference
		Object object = modelElement.eGet(eReference);
		if (eReference.getUpperBound() != 1 && eReference.getUpperBound() != 0) {
			EList<EObject> eList = (EList<EObject>) object;
			eList.add(newMEInstance);
		} else {
			modelElement.eSet(eReference, newMEInstance);
		}

		openEditor(newMEInstance, this.getClass().getName());
	}
}
