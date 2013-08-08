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
package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class NoPropertyDescriptorFoundExeption extends Exception {

	private static final long serialVersionUID = -4450264762772550298L;

	private final EObject modelElement;
	private final EStructuralFeature targetFeature;

	public NoPropertyDescriptorFoundExeption(EObject modelElement,
		EStructuralFeature targetFeature) {
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
