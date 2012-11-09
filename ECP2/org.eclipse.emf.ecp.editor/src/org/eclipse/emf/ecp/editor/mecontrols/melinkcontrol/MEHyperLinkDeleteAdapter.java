/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.editor.EditorModelelementContext;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/**
 * A mouse adapter regarding deletion of model elements.
 * 
 * @author helming
 * @author shterev
 * @author Eugen Neufeld
 */
public class MEHyperLinkDeleteAdapter extends MouseAdapter {

	private EObject modelElement;

	private EReference reference;

	private EObject opposite;

	private final EditorModelelementContext context;

	/**
	 * Default constructor.
	 * 
	 * @param modelElement
	 *            the model element
	 * @param reference
	 *            the reference link
	 * @param opposite
	 *            the model element on the other side of the link
	 * @param context
	 *            the model element context
	 */
	public MEHyperLinkDeleteAdapter(EObject modelElement, EReference reference, EObject opposite,
		EditorModelelementContext context) {
		this.modelElement = modelElement;
		this.reference = reference;
		this.opposite = opposite;
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mouseUp(MouseEvent e) {
		// TODO: Reactivate
		if (reference.isContainment()) // &&
										// context.getMetaModelElementContext().isNonDomainElement(opposite.eClass())||
										// context.getMetaModelElementContext().isAssociationClassElement(opposite)
		{
			if (askConfirmation(opposite)) {
				delete();
			}
		} else {
			delete();
		}
	}

	private void delete() {
		Command removeCommand = null;
		if (reference.isMany()) {
			removeCommand = RemoveCommand.create(context.getEditingDomain(), modelElement, reference, opposite);
		} else {
			removeCommand = SetCommand.create(context.getEditingDomain(), modelElement, reference, null);
		}
		context.getEditingDomain().getCommandStack().execute(removeCommand);
	}

	public static boolean askConfirmation(EObject toBeDeleted) {
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

		return confirm;
	}
}
