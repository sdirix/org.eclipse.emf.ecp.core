/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.ecp.explorereditorbridge.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.dialogs.MESuggestedSelectionDialog;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPControlHelper;
import org.eclipse.emf.ecp.internal.ui.Messages;
import org.eclipse.emf.ecp.internal.ui.util.ECPHandlerHelper;
import org.eclipse.emf.ecp.internal.ui.view.IViewProvider;
import org.eclipse.emf.ecp.internal.ui.view.ViewProviderHelper;
import org.eclipse.emf.ecp.internal.wizards.SelectModelElementWizard;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.ui.common.CompositeFactory;
import org.eclipse.emf.ecp.ui.common.SelectionComposite;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * @author Eugen Neufeld
 * 
 */
public class ECPControlContextImpl implements ECPControlContext {

	private final EObject modelElement;
	private final ViewModelContext viewContext;

	private final ECPProject ecpProject;
	private final Shell shell;

	private final EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();

	/**
	 * Constructor for the default implementation of the ECPControlContext.
	 * 
	 * @param modelElement the {@link EObject} which will be opened in the editor
	 * @param ecpProject the {@link ECPProject} to which the modelElement belongs
	 * @param shell the {@link Shell} to use for UI elements
	 */
	public ECPControlContextImpl(EObject modelElement, ECPProject ecpProject, Shell shell) {
		super();
		this.modelElement = modelElement;
		this.ecpProject = ecpProject;
		this.shell = shell;
		viewContext = new ViewModelContextImpl(getView(), getModelElement());
	}

	/**
	 * Constructor for the default implementation of the ECPControlContext.
	 * 
	 * @param modelElement the {@link EObject} which will be opened in the editor
	 * @param ecpProject the {@link ECPProject} to which the modelElement belongs
	 * @param shell the {@link Shell} to use for UI elements
	 * @param viewContext the {@link ViewModelContext}
	 */
	public ECPControlContextImpl(EObject modelElement, ECPProject ecpProject, Shell shell, ViewModelContext viewContext) {
		super();
		this.modelElement = modelElement;
		this.ecpProject = ecpProject;
		this.shell = shell;
		this.viewContext = viewContext;
	}

	/**
	 * Constructor for the default implementation of the ECPControlContext.
	 * 
	 * @param domainObject the {@link EObject} which will be opened in the editor
	 * @param ecpProject the {@link ECPProject} to which the modelElement belongs
	 * @param shell the {@link Shell} to use for UI elements
	 * @param view the view
	 */
	public ECPControlContextImpl(EObject domainObject, ECPProject ecpProject, Shell shell, Renderable view) {
		super();
		modelElement = domainObject;
		this.ecpProject = ecpProject;
		this.shell = shell;
		viewContext = new ViewModelContextImpl(view, getModelElement());
	}

	/** {@inheritDoc} */
	public DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}

	/** {@inheritDoc} */
	public EditingDomain getEditingDomain() {
		if (ecpProject != null) {
			return ecpProject.getEditingDomain();
		}
		return AdapterFactoryEditingDomain.getEditingDomainFor(modelElement);

	}

	/** {@inheritDoc} */
	public void openInNewContext(EObject o) {
		// TODO only elements of the same project?
		ECPHandlerHelper.openModelElement(o, ecpProject);
	}

	/** {@inheritDoc} */
	public void addModelElement(EObject newMEInstance, EReference eReference) {
		if (eReference == null) {
			// TODO needed?
			ecpProject.getContents().add(newMEInstance);
		}
		if (eReference.isContainer()) {
			// TODO language
			MessageDialog.openError(shell, "Error",//$NON-NLS-1$
				"Operation not permitted for container references!");//$NON-NLS-1$
			return;
		}
		ECPControlHelper.addModelElementInReference(modelElement, newMEInstance, eReference, getEditingDomain());

	}

	/** {@inheritDoc} */
	public EObject getModelElement() {
		return modelElement;
	}

	/** {@inheritDoc} */
	public EObject getNewElementFor(EReference eReference) {
		final Collection<EClass> classes = ECPUtil.getSubClasses(eReference.getEReferenceType());

		final SelectModelElementWizard wizard = new SelectModelElementWizard("New Reference Element",
			Messages.NewModelElementWizard_WizardTitle_AddModelElement,
			Messages.NewModelElementWizard_PageTitle_AddModelElement,
			Messages.NewModelElementWizard_PageDescription_AddModelElement);

		final SelectionComposite<TreeViewer> helper = CompositeFactory.getSelectModelClassComposite(
			new HashSet<EPackage>(),
			new HashSet<EPackage>(), classes);
		wizard.setCompositeProvider(helper);

		final WizardDialog wd = new WizardDialog(shell, wizard);
		// wizard.setWindowTitle("New Reference Element");
		EObject newMEInstance = null;
		final int result = wd.open();

		if (result == Window.OK) {
			final Object[] selection = helper.getSelection();
			if (selection == null || selection.length == 0) {
				return null;
			}
			final EClass eClasse = (EClass) selection[0];
			// 1.create ME
			final EPackage ePackage = eClasse.getEPackage();
			newMEInstance = ePackage.getEFactoryInstance().create(eClasse);
		}
		if (newMEInstance == null) {
			return null;

		}
		return newMEInstance;
	}

	// TODO externalize
	private static final String DIALOG_MESSAGE = "Enter model element name prefix or pattern (e.g. *Trun?)"; //$NON-NLS-1$

	/** {@inheritDoc} */
	public EObject getExistingElementFor(EReference eReference) {

		final Iterator<EObject> allElements = ((InternalProject) ecpProject).getReferenceCandidates(modelElement,
			eReference);

		final Set<EObject> elements = new HashSet<EObject>();
		while (allElements.hasNext()) {
			elements.add(allElements.next());
		}
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		final MESuggestedSelectionDialog dlg = new MESuggestedSelectionDialog("Select Elements", DIALOG_MESSAGE, true,//$NON-NLS-1$
			getModelElement(), eReference, elements, shell);

		final int dialogResult = dlg.open();
		if (dialogResult == Window.OK) {
			final Object result = dlg.getFirstResult();
			return (EObject) result;
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#isRunningAsWebApplication()
	 */
	public boolean isRunningAsWebApplication() {
		// TODO IMPLEMENT to be generic
		return false;
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	public ECPControlContext createSubContext(EObject eObject) {
		return new ECPControlContextImpl(eObject, ecpProject, shell, viewContext);
	}

	private View getView() {
		int highestPrio = IViewProvider.NOT_APPLICABLE;
		IViewProvider selectedProvider = null;
		for (final IViewProvider viewProvider : ViewProviderHelper.getViewProviders()) {
			final int prio = viewProvider.canRender(getModelElement());
			if (prio > highestPrio) {
				highestPrio = prio;
				selectedProvider = viewProvider;
			}
		}
		if (selectedProvider != null) {
			return selectedProvider.generate(getModelElement());
		}
		return null;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.ECPControlContext#getViewContext()
	 */
	public ViewModelContext getViewContext() {
		return viewContext;
	}

}
