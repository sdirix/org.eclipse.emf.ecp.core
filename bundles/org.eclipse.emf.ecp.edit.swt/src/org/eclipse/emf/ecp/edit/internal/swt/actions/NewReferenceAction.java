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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.util.OverlayImageDescriptor;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * An Action for adding reference links to a model element. It is mainly used in the
 * {@link org.eclipse.emf.ecp.edit.internal.swt.controls.ReferenceMultiControl ReferenceMultiControl}
 * 
 * @author shterev
 * @author Eugen Neufeld
 */
public class NewReferenceAction extends ECPSWTAction {

	/**
	 * The constructor for a new reference action.
	 * 
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 */
	public NewReferenceAction(ECPControlContext modelElementContext, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature) {
		super(modelElementContext, itemPropertyDescriptor, feature);
		Object obj = null;
		final EReference eReference = (EReference) getFeature();
		// Only create a temporary object in order to get the correct icon from the label provider
		// the actual ME is created later on.
		if (!eReference.getEReferenceType().isAbstract()) {
			obj = eReference.getEReferenceType().getEPackage().getEFactoryInstance()
				.create(eReference.getEReferenceType());
		}
		final IItemLabelProvider labelProvider = getItemPropertyDescriptor().getLabelProvider(
			modelElementContext.getModelElement());
		Object labelProviderImageResult = labelProvider.getImage(obj);

		Image image = null;

		if (ComposedImage.class.isInstance(labelProviderImageResult)) {
			labelProviderImageResult = ((ComposedImage) labelProviderImageResult).getImages().get(0);
		}
		if (URI.class.isInstance(labelProviderImageResult)) {
			try {
				labelProviderImageResult = new URL(((URI) labelProviderImageResult).toString());
			} catch (final MalformedURLException ex) {
				Activator.logException(ex);
			}
		}
		if (URL.class.isInstance(labelProviderImageResult)) {
			image = Activator.getImage((URL) labelProviderImageResult);
		}
		else {
			image = Activator.getImage((URL) null);
		}

		final ImageDescriptor addOverlay = Activator.getImageDescriptor("icons/add_overlay.png");//$NON-NLS-1$
		final OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, addOverlay,
			OverlayImageDescriptor.LOWER_RIGHT);
		setImageDescriptor(imageDescriptor);

		String attribute = getItemPropertyDescriptor().getDisplayName(eReference);
		// TODO language, same text as in addreference
		// make singular attribute labels
		if (attribute.endsWith("ies")) {//$NON-NLS-1$
			attribute = attribute.substring(0, attribute.length() - 3) + "y";//$NON-NLS-1$
		} else if (attribute.endsWith("s")) {//$NON-NLS-1$
			attribute = attribute.substring(0, attribute.length() - 1);
		}
		setToolTipText(ActionMessages.NewReferenceAction_CreateAndLinkNew + attribute);
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
	// ImageDescriptor addOverlay = Activator.getImageDescriptor("icons/add_overlay.png");//$NON-NLS-1$
	// OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, addOverlay,
	// OverlayImageDescriptor.LOWER_RIGHT);
	// setImageDescriptor(imageDescriptor);
	//
	// String attribute = descriptor.getDisplayName(eReference);
	//
	// // make singular attribute labels
	// if (attribute.endsWith("ies")) {//$NON-NLS-1$
	// attribute = attribute.substring(0, attribute.length() - 3) + "y";//$NON-NLS-1$
	// } else if (attribute.endsWith("s")) {//$NON-NLS-1$
	// attribute = attribute.substring(0, attribute.length() - 1);
	// }
	// setToolTipText(ActionMessages.NewReferenceAction_CreateAndLinkNew + attribute);
	//
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		// checks if we try to create a container for ourself, this is not allowed

		// getModelElementContext().getEditingDomain().getCommandStack()
		// .execute(new NewReferenceCommand(getModelElementContext().getModelElement()));
		final EObject eObject = getModelElementContext().getNewElementFor((EReference) getFeature());
		if (eObject == null) {
			return;
		}
		getModelElementContext().addModelElement(eObject, (EReference) getFeature());
		getModelElementContext().openInNewContext(eObject);
	}

}
