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
 * {@link org.eclipse.emf.ecp.edit.internal.swt.controls.MultiControl MultiControl}
 * 
 * @author shterev
 * @author Eugen Neufeld
 */
public class AddReferenceAction extends ECPSWTAction {

	/**
	 * The constructor of an add reference action.
	 * 
	 * @param modelElementContext the {@link ECPControlContext}
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor}
	 * @param feature the {@link EStructuralFeature}
	 */
	public AddReferenceAction(ECPControlContext modelElementContext, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature) {
		super(modelElementContext, itemPropertyDescriptor, feature);
		final EReference eReference = (EReference) feature;
		Object obj = null;
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
		String overlayString = "icons/link_overlay.png";//$NON-NLS-1$
		if (eReference.isContainment()) {
			overlayString = "icons/containment_overlay.png";//$NON-NLS-1$
		}
		final ImageDescriptor addOverlay = Activator.getImageDescriptor(overlayString);
		final OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, addOverlay,
			OverlayImageDescriptor.LOWER_RIGHT);
		setImageDescriptor(imageDescriptor);

		String attribute = getItemPropertyDescriptor().getDisplayName(eReference);
		// make singular attribute labels
		// TODO language dependent
		if (attribute.endsWith("ies")) {//$NON-NLS-1$ 
			attribute = attribute.substring(0, attribute.length() - 3) + "y"; //$NON-NLS-1$
		} else if (attribute.endsWith("s")) {//$NON-NLS-1$
			attribute = attribute.substring(0, attribute.length() - 1);
		}
		setToolTipText(ActionMessages.AddReferenceAction_Link + attribute);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		final EObject selectedEObject = getModelElementContext().getExistingElementFor((EReference) getFeature());
		if (selectedEObject == null) {
			return;
		}
		getModelElementContext().addModelElement(selectedEObject, (EReference) getFeature());
		getModelElementContext().openInNewContext(selectedEObject);

	}

}
