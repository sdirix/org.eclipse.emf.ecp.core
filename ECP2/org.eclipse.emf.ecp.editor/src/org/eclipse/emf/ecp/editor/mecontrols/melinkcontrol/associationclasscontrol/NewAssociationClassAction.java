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
package org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.associationclasscontrol;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.editor.Activator;
import org.eclipse.emf.ecp.editor.EditorModelelementContext;
import org.eclipse.emf.ecp.editor.OverlayImageDescriptor;
import org.eclipse.emf.ecp.editor.commands.ECPCommand;
import org.eclipse.emf.ecp.ui.model.MEClassLabelProvider;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import java.util.Set;

/**
 * This class provides the icon and command to create a new object and link it with a given object over an
 * AssociationClassElement.
 * 
 * @author Michael Haeger
 * @author Eugen Neufeld
 */
public class NewAssociationClassAction extends Action {

	private static final String DIALOG_MESSAGE = "Select a model element type to be created:";

	private EReference eReference;

	private EObject modelElement;

	private final EditorModelelementContext context;

	/**
	 * The create command.
	 * 
	 * @author Michael Haeger
	 */
	private final class NewAssociationClassCommand extends ECPCommand {

		public NewAssociationClassCommand(EObject eObject, EditingDomain domain) {
			super(eObject, domain);
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		protected void doRun() {
			EClass relatedModelElementClass = null;
			Set<EClass> subclasses = context.getMetaModelElementContext().getAllSubEClasses(modelElement.eClass(),
				false);
			// select object type to create
			if (subclasses.size() == 1) {
				relatedModelElementClass = subclasses.iterator().next();
			} else {
				ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
				ElementListSelectionDialog dlg = new ElementListSelectionDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), new MEClassLabelProvider(composedAdapterFactory));
				dlg.setMessage(DIALOG_MESSAGE);
				dlg.setElements(subclasses.toArray());
				dlg.setTitle("Select Element type");
				dlg.setBlockOnOpen(true);
				if (dlg.open() != Window.OK) {
					composedAdapterFactory.dispose();
					return;
				}
				Object result = dlg.getFirstResult();
				if (result instanceof EClass) {
					relatedModelElementClass = (EClass) result;
				}
				composedAdapterFactory.dispose();
			}
			// create the other side of the association
			EPackage ePackage = relatedModelElementClass.getEPackage();
			final EObject relatedModelElement = ePackage.getEFactoryInstance().create(relatedModelElementClass);
			if (!eReference.isContainer()) {
				EObject parent = modelElement.eContainer();
				while (!(parent == null) && relatedModelElement.eContainer() == null) {
					EReference reference = context.getMetaModelElementContext().getPossibleContainingReference(
						relatedModelElement, parent);
					if (reference != null && reference.isMany()) {
						Object object = parent.eGet(reference);
						EList<EObject> eList = (EList<EObject>) object;
						eList.add(relatedModelElement);
					}
					parent = parent.eContainer();
				}
				if (relatedModelElement.eContainer() == null) {
					throw new RuntimeException("No matching container for model element found");
				}
			}
			// create the association
			AssociationClassHelper.createAssociation(eReference, modelElement, relatedModelElement,
				context.getMetaModelElementContext());
			context.openEditor(relatedModelElement, this.getClass().getName());
		}
	}

	/**
	 * Default constructor.
	 * 
	 * @param modelElement
	 *            the object
	 * @param eReference
	 *            the reference to the AssociationClassElement
	 * @param descriptor
	 *            the descriptor used to generate display content
	 * @param context
	 *            model element context
	 */
	public NewAssociationClassAction(EObject modelElement, EReference eReference, IItemPropertyDescriptor descriptor,
		EditorModelelementContext context, AdapterFactoryLabelProvider labelProvider) {
		this.modelElement = modelElement;
		this.eReference = eReference;
		this.context = context;
		Object obj = null;
		if (!eReference.getEReferenceType().isAbstract()) {
			obj = eReference.getEReferenceType().getEPackage().getEFactoryInstance()
				.create(eReference.getEReferenceType());
		}
		Image image = labelProvider.getImage(obj);
		ImageDescriptor addOverlay = Activator.getImageDescriptor("icons/add_overlay.png");
		OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, addOverlay,
			OverlayImageDescriptor.LOWER_RIGHT);
		setImageDescriptor(imageDescriptor);
		String attribute = descriptor.getDisplayName(eReference);
		// make singular attribute labels
		if (attribute.endsWith("ies")) {
			attribute = attribute.substring(0, attribute.length() - 3) + "y";
		} else if (attribute.endsWith("s")) {
			attribute = attribute.substring(0, attribute.length() - 1);
		}
		setToolTipText("Create and link new " + attribute);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		new NewAssociationClassCommand(modelElement, context.getEditingDomain()).run(true);
	}
}
