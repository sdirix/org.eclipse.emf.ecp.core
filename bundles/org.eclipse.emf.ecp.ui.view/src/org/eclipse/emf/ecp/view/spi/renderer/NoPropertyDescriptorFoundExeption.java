/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.renderer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;

/**
 * Exception indicating, that an {@link org.eclipse.emf.edit.provider.ItemPropertyDescriptor ItemPropertyDescriptor}
 * could not be found for an {@link EObject} and its {@link EStructuralFeature}.
 * 
 * @author Eugen Neufeld
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class NoPropertyDescriptorFoundExeption extends ECPRendererException {

	private static final String NO_PROPERTY_DESCRIPTOR_FOUND = "No Property descriptor found. Make sure, the corresponing edit bundle is started."; //$NON-NLS-1$

	private static final long serialVersionUID = -4450264762772550298L;

	private final EObject modelElement;
	private final EStructuralFeature targetFeature;

	public NoPropertyDescriptorFoundExeption(EObject modelElement,
		EStructuralFeature targetFeature) {
		super(NO_PROPERTY_DESCRIPTOR_FOUND);
		this.modelElement = modelElement;
		this.targetFeature = targetFeature;
	}

	public EObject getModelElement() {
		return modelElement;
	}

	public EStructuralFeature getTargetFeature() {
		return targetFeature;
	}
}
