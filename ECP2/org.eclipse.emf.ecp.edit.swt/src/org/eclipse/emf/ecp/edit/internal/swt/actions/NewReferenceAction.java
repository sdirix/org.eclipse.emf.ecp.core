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
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.actions;

import java.net.URL;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.util.OverlayImageDescriptor;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

/**
 * An Action for adding reference links to a model element. It is mainly used in the {@link MEMultiLinkControl}
 * 
 * @author shterev
 * @author Eugen Neufeld
 */
public class NewReferenceAction extends ECPSWTAction {

	/**
	 * The constructor for a new reference action.
	 * @param modelElementContext the {@link EditModelElementContext} to use
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 */
	public NewReferenceAction(EditModelElementContext modelElementContext,
		IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature) {
		super(modelElementContext, itemPropertyDescriptor, feature);
		Object obj = null;
		EReference eReference = (EReference) getFeature();
		// Only create a temporary object in order to get the correct icon from the label provider
		// the actual ME is created later on.
		if (!eReference.getEReferenceType().isAbstract()) {
			obj = eReference.getEReferenceType().getEPackage().getEFactoryInstance()
				.create(eReference.getEReferenceType());
		}
		IItemLabelProvider labelProvider = getItemPropertyDescriptor().getLabelProvider(
			modelElementContext.getModelElement());
		Image image = Activator.getImageDescriptor(((URL) labelProvider.getImage(obj)).toExternalForm()).createImage();

		ImageDescriptor addOverlay = Activator.getImageDescriptor("icons/add_overlay.png");
		OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, addOverlay,
			OverlayImageDescriptor.LOWER_RIGHT);
		setImageDescriptor(imageDescriptor);

		String attribute = getItemPropertyDescriptor().getDisplayName(eReference);

		// make singular attribute labels
		if (attribute.endsWith("ies")) {
			attribute = attribute.substring(0, attribute.length() - 3) + "y";
		} else if (attribute.endsWith("s")) {
			attribute = attribute.substring(0, attribute.length() - 1);
		}
		setToolTipText("Create and link new " + attribute);
	}

	/**
	 * Command to create a new reference.
	 * 
	 * @author helming
	 */
	private final class NewReferenceCommand extends ChangeCommand {

		public NewReferenceCommand(EObject eObject) {
			super(eObject);
		}

		@Override
		protected void doExecute() {
			getModelElementContext().createAndReferenceNewModelElement((EReference) getFeature());

		}
	}

	/**
	 * Default constructor.
	 * 
	 * @param modelElement
	 *            the source model element
	 * @param eReference
	 *            the target reference
	 * @param descriptor
	 *            the descriptor used to generate display content
	 * @param modelElementContext
	 *            the model element context
	 */
	// public NewReferenceAction(EObject modelElement, EReference eReference, IItemPropertyDescriptor descriptor,
	// EditModelElementContext modelElementContext, Shell shell, AdapterFactoryLabelProvider labelProvider) {
	// this.modelElement = modelElement;
	// this.eReference = eReference;
	// this.modelElementContext = modelElementContext;
	// this.shell = shell;
	//
	// Object obj = null;
	// // Only create a temporary object in order to get the correct icon from the label provider
	// // the actual ME is created later on.
	// if (!eReference.getEReferenceType().isAbstract()) {
	// obj = eReference.getEReferenceType().getEPackage().getEFactoryInstance()
	// .create(eReference.getEReferenceType());
	// }
	// Image image = labelProvider.getImage(obj);
	//
	// ImageDescriptor addOverlay = Activator.getImageDescriptor("icons/add_overlay.png");
	// OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, addOverlay,
	// OverlayImageDescriptor.LOWER_RIGHT);
	// setImageDescriptor(imageDescriptor);
	//
	// String attribute = descriptor.getDisplayName(eReference);
	//
	// // make singular attribute labels
	// if (attribute.endsWith("ies")) {
	// attribute = attribute.substring(0, attribute.length() - 3) + "y";
	// } else if (attribute.endsWith("s")) {
	// attribute = attribute.substring(0, attribute.length() - 1);
	// }
	// setToolTipText("Create and link new " + attribute);
	//
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		// checks if we try to create a container for ourself, this is not allowed
		if (((EReference) getFeature()).isContainer()) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "",
				"Operation not permitted for container references!");
			return;
		}
		getModelElementContext().getEditingDomain().getCommandStack()
			.execute(new NewReferenceCommand(getModelElementContext().getModelElement()));
	}

}
