/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.common.EMFUtils;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPDialogExecutor;
import org.eclipse.emf.ecp.edit.spi.ReferenceService;
import org.eclipse.emf.ecp.internal.edit.ECPControlHelper;
import org.eclipse.emf.ecp.spi.common.ui.CompositeFactory;
import org.eclipse.emf.ecp.spi.common.ui.SelectModelElementWizard;
import org.eclipse.emf.ecp.spi.common.ui.composites.SelectionComposite;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.view.internal.swt.Activator;
import org.eclipse.emf.ecp.view.internal.swt.Messages;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * @author Eugen
 * @since 1.4
 * 
 */
public class DefaultReferenceService implements ReferenceService {

	private EObject rootDomainModel;
	private EditingDomain editingDomain;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#instantiate(org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public void instantiate(ViewModelContext context) {
		rootDomainModel = context.getDomainModel();
		editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(rootDomainModel);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#dispose()
	 */
	@Override
	public void dispose() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#getPriority()
	 */
	@Override
	public int getPriority() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ReferenceService#addModelElement(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EReference)
	 */
	@Override
	public void addModelElement(EObject eObject, EReference eReference) {
		if (eReference.isContainer()) {
			// TODO language
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error",//$NON-NLS-1$
				"Operation not permitted for container references!");//$NON-NLS-1$
			return;
		}

		ECPControlHelper.addModelElementInReference(rootDomainModel, eObject, eReference,
			editingDomain);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ReferenceService#getNewElementFor(org.eclipse.emf.ecore.EReference)
	 */
	@Override
	public EObject getNewElementFor(EReference eReference) {
		final Collection<EClass> classes = EMFUtils.getSubClasses(eReference.getEReferenceType());

		final SelectModelElementWizard wizard = new SelectModelElementWizard("New Reference Element",
			Messages.NewModelElementWizard_WizardTitle_AddModelElement,
			Messages.NewModelElementWizard_PageTitle_AddModelElement,
			Messages.NewModelElementWizard_PageDescription_AddModelElement);

		final SelectionComposite<TreeViewer> helper = CompositeFactory.getSelectModelClassComposite(
			new HashSet<EPackage>(),
			new HashSet<EPackage>(), classes);
		wizard.setCompositeProvider(helper);

		final WizardDialog wd = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ReferenceService#getExistingElementFor(org.eclipse.emf.ecore.EReference)
	 */
	@Override
	public EObject getExistingElementFor(EReference eReference) {
		final Iterator<EObject> allElements =
			ItemPropertyDescriptor.getReachableObjectsOfType(rootDomainModel, eReference.getEType())
				.iterator();

		final Set<EObject> elements = new LinkedHashSet<EObject>();
		while (allElements.hasNext()) {
			elements.add(allElements.next());
		}

		final SelectModelElementWizard wizard = new SelectModelElementWizard("Select Elements",
			Messages.NewModelElementWizard_WizardTitle_AddModelElement,
			Messages.ModelelementSelectionDialog_DialogTitle,
			Messages.ModelelementSelectionDialog_DialogMessage_SearchPattern, EObject.class);

		final SelectionComposite<TableViewer> tableSelectionComposite = CompositeFactory
			.getTableSelectionComposite(elements.toArray());
		wizard.setCompositeProvider(tableSelectionComposite);

		final WizardDialog wd = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
		EObject eObject = null;
		final int result = wd.open();
		if (result == Window.OK) {
			final Object[] selection = tableSelectionComposite.getSelection();
			if (selection == null || selection.length == 0) {
				return null;
			}
			eObject = (EObject) selection[0];

		}
		return eObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ReferenceService#openInNewContext(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public void openInNewContext(final EObject eObject) {
		final Dialog dialog = new Dialog(Display.getDefault().getActiveShell()) {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
			 */
			@Override
			protected Control createDialogArea(Composite parent) {
				final Composite composite = (Composite) super.createDialogArea(parent);

				try {
					ECPSWTViewRenderer.INSTANCE.render(composite, eObject);
				} catch (final ECPRendererException ex) {
					Activator.log(ex);
				}

				return composite;
			}

		};
		new ECPDialogExecutor(dialog) {
			@Override
			public void handleResult(int codeResult) {

			}
		}.execute();
	}
}
