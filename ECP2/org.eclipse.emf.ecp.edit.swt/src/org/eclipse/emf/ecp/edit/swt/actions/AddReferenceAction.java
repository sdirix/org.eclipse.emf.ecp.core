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

package org.eclipse.emf.ecp.edit.swt.actions;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.swt.util.OverlayImageDescriptor;
import org.eclipse.emf.ecp.internal.edit.swt.Activator;
import org.eclipse.emf.ecp.internal.edit.swt.dialogs.MESuggestedSelectionDialog;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * An Action for adding reference links to a model element. It is mainly used in the {@link MEMultiLinkControl}
 * 
 * @author shterev
 * @author Eugen Neufeld
 */
public class AddReferenceAction extends ECPSWTAction {

	private static final String DIALOG_MESSAGE = "Enter model element name prefix or pattern (e.g. *Trun?)";

	/**
	 * @param modelElementContext
	 * @param itemPropertyDescriptor
	 * @param feature
	 */
	public AddReferenceAction(EditModelElementContext modelElementContext,
		IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature) {
		super(modelElementContext, itemPropertyDescriptor, feature);
		EReference eReference = (EReference) feature;
		Object obj = null;
		if (!eReference.getEReferenceType().isAbstract()) {
			obj = eReference.getEReferenceType().getEPackage().getEFactoryInstance()
				.create(eReference.getEReferenceType());
		}
		IItemLabelProvider labelProvider = getItemPropertyDescriptor().getLabelProvider(
			modelElementContext.getModelElement());

		Image image = Activator.getImageDescriptor(((URL) labelProvider.getImage(obj)).toExternalForm()).createImage();

		String overlayString = "icons/link_overlay.png";
		if (eReference.isContainment()) {
			overlayString = "icons/containment_overlay.png";
		}
		ImageDescriptor addOverlay = Activator.getImageDescriptor(overlayString);
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

		setToolTipText("Link " + attribute);
	}

	/**
	 * Command to add a new reference.
	 * 
	 * @author helming
	 */
	private final class AddReferenceCommand extends ChangeCommand {

		public AddReferenceCommand(EObject eObject) {
			super(eObject);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void doExecute() {

			EObject object = (EObject) notifier;
			// Collection<EObject> allElements=new HashSet<EObject>();
			//

			Iterator<EObject> allElements = getModelElementContext().getLinkElements((EReference) getFeature());

			// EClass clazz = eReference.getEReferenceType();
			// Collection<EObject> allElements = context.getAllModelElementsbyClass(clazz, true);

			// checks that elements are "real"
			// allElements.remove(modelElement);
			// Object object = modelElement.eGet(eReference);
			//
			// EList<EObject> eList = null;
			// EObject eObject = null;
			//
			// // don't the instances that are already linked
			// if (eReference.isMany() && object instanceof EList) {
			// eList = (EList<EObject>) object;
			// for (EObject ref : eList) {
			// allElements.remove(ref);
			// }
			// } else if (!eReference.isMany() && object instanceof EObject) {
			// eObject = (EObject) object;
			// allElements.remove(eObject);
			// }
			//
			// // don't show contained elements for inverse containment references
			// if (eReference.isContainer()) {
			// allElements.removeAll(modelElement.eContents());
			// }
			//
			// // take care of circular references
			// if (eReference.isContainment()) {
			// Iterator<EObject> iter = allElements.iterator();
			// while (iter.hasNext()) {
			// EObject me = iter.next();
			// if (EcoreUtil.isAncestor(me, modelElement)) {
			// iter.remove();
			// }
			// }
			// }
			Set<EObject> elements = new HashSet<EObject>();
			while (allElements.hasNext()) {
				elements.add(allElements.next());
			}

			// HandlerHelper.createNewReferenceElement(modelElement, eReference,
			// new WizardUICallback<SelectModelElementComposite>(shell, null));
			// TODO remove PlatformUI
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MESuggestedSelectionDialog dlg = new MESuggestedSelectionDialog("Select Elements", DIALOG_MESSAGE, true,
				getModelElementContext().getModelElement(), (EReference) getFeature(), elements, shell);

			int dialogResult = dlg.open();
			if (dialogResult == Dialog.OK) {
				if (getFeature().isMany()) {
					Object[] results = dlg.getResult();
					ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
					progressDialog.open();
					progressDialog.getProgressMonitor().beginTask("Adding references...", results.length * 10);
					List<EObject> list = new ArrayList<EObject>();
					for (Object result : results) {
						if (result instanceof EObject) {
							list.add((EObject) result);
							progressDialog.getProgressMonitor().worked(10);
						}
					}
					((EList<EObject>) getModelElementContext().getModelElement().eGet(getFeature())).addAll(list);

					progressDialog.getProgressMonitor().done();
					progressDialog.close();
				} else {
					Object result = dlg.getFirstResult();
					if (result instanceof EObject) {
						getModelElementContext().getModelElement().eSet(getFeature(), result);
					}
				}

			}
		}
	}

	// /**
	// * Default constructor.
	// *
	// * @param modelElement
	// * the source model element
	// * @param eReference
	// * the target reference
	// * @param descriptor
	// * the descriptor used to generate display content
	// * @param context
	// * the context of the model element
	// * @param shell the {@link Shell} to create the dialog in
	// */
	// public AddReferenceAction(EObject modelElement, EReference eReference, IItemPropertyDescriptor descriptor,
	// EditModelElementContext context, Shell shell, AdapterFactoryLabelProvider labelProvider) {
	// this.modelElement = modelElement;
	// this.eReference = eReference;
	// this.context = context;
	// this.shell = shell;
	//
	// Object obj = null;
	// if (!eReference.getEReferenceType().isAbstract()) {
	// obj = eReference.getEReferenceType().getEPackage().getEFactoryInstance()
	// .create(eReference.getEReferenceType());
	// }
	// Image image = labelProvider.getImage(obj);
	// String overlayString = "icons/link_overlay.png";
	// if (eReference.isContainment()) {
	// overlayString = "icons/containment_overlay.png";
	// }
	// ImageDescriptor addOverlay = Activator.getImageDescriptor(overlayString);
	// OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, addOverlay,
	// OverlayImageDescriptor.LOWER_RIGHT);
	// setImageDescriptor(imageDescriptor);
	//
	// String attribute = descriptor.getDisplayName(eReference);
	// // make singular attribute labels
	// if (attribute.endsWith("ies")) {
	// attribute = attribute.substring(0, attribute.length() - 3) + "y";
	// } else if (attribute.endsWith("s")) {
	// attribute = attribute.substring(0, attribute.length() - 1);
	// }
	//
	// setToolTipText("Link " + attribute);
	//
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		// TODO check if works
		// Iterator<EObject> allElements = context.getLinkElements(eReference);
		// Set<EObject> elements = new HashSet<EObject>();
		// while (allElements.hasNext()) {
		// elements.add(allElements.next());
		// }
		// HandlerHelper.createNewReferenceElement(context.getEditingDomain(), context.getModelElement(), eReference,
		// elements, new WizardUICallback<SelectModelElementComposite>(shell, new AddReferenceModelElementWizard()));

		getModelElementContext().getEditingDomain().getCommandStack()
			.execute(new AddReferenceCommand(getModelElementContext().getModelElement()));

	}

}
