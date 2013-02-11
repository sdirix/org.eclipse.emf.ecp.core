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
 ******************************************************************************/
package org.eclipse.emf.ecp.edit.swt.actions;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.internal.edit.swt.Activator;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.ISharedImages;

/**
 * @author Eugen Neufeld
 * 
 */
public class DeleteReferenceAction extends ECPSWTAction {

	/**
	 * @param modelElementContext
	 * @param itemPropertyDescriptor
	 * @param feature
	 */
	public DeleteReferenceAction(EditModelElementContext modelElementContext,
		IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature) {
		super(modelElementContext, itemPropertyDescriptor, feature);
		// TODO remove PlatformUI

		setImageDescriptor(Activator.getDefault().getWorkbench().getSharedImages()
			.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setToolTipText("Delete Reference");
	}

	@Override
	public void run() {
		super.run();
		// TODO: Reactivate
		EReference reference = (EReference) getFeature();
		if (reference.isContainment()) // &&
										// context.getMetaModelElementContext().isNonDomainElement(opposite.eClass())||
										// context.getMetaModelElementContext().isAssociationClassElement(opposite)
		{
			if (askConfirmation((EObject) getModelElementContext().getModelElement().eGet(reference))) {
				delete(reference);
			}
		} else {
			delete(reference);
		}
	}

	private void delete(EReference reference) {
		Command removeCommand = null;

		if (reference.isMany()) {
			removeCommand = RemoveCommand.create(getModelElementContext().getEditingDomain(), getModelElementContext()
				.getModelElement(), reference, getModelElementContext().getModelElement().eGet(getFeature()));
		} else {
			removeCommand = SetCommand.create(getModelElementContext().getEditingDomain(), getModelElementContext()
				.getModelElement(), reference, null);
		}
		getModelElementContext().getEditingDomain().getCommandStack().execute(removeCommand);
	}

	private boolean askConfirmation(EObject toBeDeleted) {
		String question = null;
		ComposedAdapterFactory adapterFactory = null;
		// if (toBeDeleted.size() == 1) {
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		AdapterFactoryLabelProvider adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(adapterFactory);
		String modelElementName = adapterFactoryLabelProvider.getText(toBeDeleted);
		question = "Do you really want to delete the model element " + modelElementName + "?";
		// } else {
		// question = "Do you really want to delete these " + toBeDeleted.size() + " model elements?";
		// }
		MessageDialog dialog = new MessageDialog(null, "Confirmation", null, question, MessageDialog.QUESTION,
			new String[] { "Yes", "No" }, 0);

		boolean confirm = false;
		if (dialog.open() == MessageDialog.OK) {
			confirm = true;
		}

		// if (adapterFactory != null)
		// {
		// adapterFactory.dispose();
		// }
		adapterFactory.dispose();
		adapterFactoryLabelProvider.dispose();
		return confirm;
	}
}
