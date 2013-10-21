/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.generator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.internal.ui.view.IViewProvider;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;

/**
 * View Provider.
 */
public class ViewProvider implements IViewProvider {

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.IViewProvider#generate(org.eclipse.emf.ecore.EObject)
	 */
	public View generate(EObject eObject) {
		final View view = ViewFactory.eINSTANCE.createView();
		for (final EStructuralFeature feature : eObject.eClass().getEAllStructuralFeatures()) {

			if (isInvalidFeature(feature)) {
				continue;
			}

			final Control control = ViewFactory.eINSTANCE.createControl();
			final VFeaturePathDomainModelReference modelReference = ViewFactory.eINSTANCE
				.createVFeaturePathDomainModelReference();
			modelReference.setDomainModelEFeature(feature);
			control.setDomainModelReference(modelReference);
			view.getChildren().add(control);
		}

		return view;
	}

	private boolean isInvalidFeature(EStructuralFeature feature) {
		return isContainerReference(feature) || isTransient(feature) || isVolatile(feature);
	}

	private boolean isContainerReference(EStructuralFeature feature) {
		if (feature instanceof EReference) {
			final EReference reference = (EReference) feature;
			if (reference.isContainer()) {
				return true;
			}
		}

		return false;
	}

	private boolean isTransient(EStructuralFeature feature) {
		return feature.isTransient();
	}

	private boolean isVolatile(EStructuralFeature feature) {
		return feature.isVolatile();
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.IViewProvider#canRender(org.eclipse.emf.ecore.EObject)
	 */
	public int canRender(EObject eObject) {
		return 1;
	}
}
