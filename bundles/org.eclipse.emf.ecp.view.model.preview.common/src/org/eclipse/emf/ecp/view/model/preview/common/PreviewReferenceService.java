/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.preview.common;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.spi.ReferenceService;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;

/**
 * @author Alexandra Buzila
 * 
 */
public class PreviewReferenceService implements ReferenceService {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#instantiate(org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	public void instantiate(ViewModelContext context) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#dispose()
	 */
	public void dispose() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#getPriority()
	 */
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ReferenceService#addModelElement(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EReference)
	 */
	public void addModelElement(EObject eObject, EReference eReference) {
		// preview should not change the model
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ReferenceService#getNewElementFor(org.eclipse.emf.ecore.EReference)
	 */
	public EObject getNewElementFor(EReference eReference) {
		// preview should not change the model
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ReferenceService#getExistingElementFor(org.eclipse.emf.ecore.EReference)
	 */
	public EObject getExistingElementFor(EReference eReference) {
		// // final Iterator<EObject> allElements = ((InternalProject) ecpProject).getReferenceCandidates(
		// // context.getDomainModel(),
		// // eReference);
		// final EClass eReferenceType = eReference.getEReferenceType();
		// // check if interface or abstract
		//
		// final List<EObject> elements = new ArrayList<EObject>();
		// // for loop create arbitrary dummy objects based on the eclass
		// for (int i = 0; i < 5; i++) {
		// final EObject create = EcoreUtil.create(eReferenceType);
		// // search for name/label or just set all
		// for (final EAttribute attr : eReferenceType.getEAllAttributes()) {
		// // generate dummy value
		//
		// // create.eSet(feature, attr);
		// }
		//
		// elements.add(create);
		// }
		// // final Set<EObject> elements = new HashSet<EObject>();
		// // while (allElements.hasNext()) {
		// // elements.add(allElements.next());
		// // }
		//
		//		final SelectModelElementWizard wizard = new SelectModelElementWizard("Select Elements", //$NON-NLS-1$
		// Messages.NewModelElementWizard_WizardTitle_AddModelElement,
		// Messages.ModelelementSelectionDialog_DialogTitle,
		// Messages.ModelelementSelectionDialog_DialogMessage_SearchPattern, EObject.class);
		//
		// final SelectionComposite<TableViewer> tableSelectionComposite = CompositeFactory
		// .getTableSelectionComposite(elements.toArray());
		// wizard.setCompositeProvider(tableSelectionComposite);
		//
		// final WizardDialog wd = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
		// EObject eObject = null;
		// final int result = wd.open();
		// if (result == Window.OK) {
		// final Object[] selection = tableSelectionComposite.getSelection();
		// if (selection == null || selection.length == 0) {
		// return null;
		// }
		// eObject = (EObject) selection[0];
		//
		// }
		// return eObject;
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ReferenceService#openInNewContext(org.eclipse.emf.ecore.EObject)
	 */
	public void openInNewContext(EObject eObject) {
	}

}
