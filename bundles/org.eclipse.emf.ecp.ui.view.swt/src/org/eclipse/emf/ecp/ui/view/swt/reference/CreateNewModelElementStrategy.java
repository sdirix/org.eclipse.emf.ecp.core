/*******************************************************************************
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * lucas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.reference;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.common.spi.EMFUtils;
import org.eclipse.emf.ecp.spi.common.ui.CompositeFactory;
import org.eclipse.emf.ecp.spi.common.ui.SelectModelElementWizardFactory;
import org.eclipse.emf.ecp.spi.common.ui.composites.SelectionComposite;
import org.eclipse.emf.ecp.view.internal.swt.Activator;
import org.eclipse.emfforms.bazaar.Vendor;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

/**
 * A {@link org.eclipse.emf.ecp.ui.view.swt.DefaultReferenceService DefaultReferenceService}
 * <em>customization strategy</em> to add one or more new elements to a reference of an owner.
 *
 * @author Lucas Koehler
 * @see org.eclipse.emf.ecp.ui.view.swt.DefaultReferenceService DefaultReferenceService
 */
public interface CreateNewModelElementStrategy {

	/**
	 * Default strategy that allows creating a new model element based on the sub classes of the reference type. If
	 * there is more than one, a selection dialog is shown.
	 */
	CreateNewModelElementStrategy DEFAULT = new CreateNewModelElementStrategy() {

		@Override
		public Optional<EObject> createNewModelElement(EObject owner, EReference reference) {
			final Collection<EClass> classes = EMFUtils.getSubClasses(reference.getEReferenceType());
			if (classes.isEmpty()) {
				final String errorMessage = String.format("No concrete classes for the type %1$s were found!", //$NON-NLS-1$
					reference.getEReferenceType().getName());
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", //$NON-NLS-1$
					errorMessage);
				Activator.getDefault().getReportService().report(new AbstractReport(errorMessage));
				return Optional.empty();
			}
			if (classes.size() == 1) {
				return Optional.of(EcoreUtil.create(classes.iterator().next()));
			}
			return Optional.ofNullable(getModelElementInstanceFromList(classes));
		}

		private EObject getModelElementInstanceFromList(Collection<EClass> classes) {
			final SelectionComposite<TreeViewer> helper = CompositeFactory.getSelectModelClassComposite(
				new HashSet<EPackage>(),
				new HashSet<EPackage>(), classes);

			return SelectModelElementWizardFactory.openCreateNewModelElementDialog(helper);
		}

	};

	/**
	 * Create a new model element in the reference of the given owner.
	 *
	 * @param owner The {@link EObject} that contains the reference
	 * @param reference The reference for which a new model element should be created
	 * @return The created model element or <code>null</code> if none was created
	 */
	Optional<EObject> createNewModelElement(EObject owner, EReference reference);

	/**
	 * Specific Bazaar vendor interface for add new model elements strategies.
	 *
	 * @since 1.16
	 */
	public interface Provider extends Vendor<CreateNewModelElementStrategy> {
		// Nothing to add to the super interface
	}
}
