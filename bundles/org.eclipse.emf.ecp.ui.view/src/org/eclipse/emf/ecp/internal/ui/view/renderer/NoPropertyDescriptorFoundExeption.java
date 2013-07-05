package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class NoPropertyDescriptorFoundExeption extends Exception {

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
