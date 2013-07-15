package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * 
 * Leafs may override rule evaluation behavior of their parents.
 * 
 * @author emueller
 *
 * @param <T>
 */
public final class Leaf<T extends Renderable> extends Node<T> {

	public Leaf(T model, ECPControlContext controlContext) {
		super(model, controlContext);
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
	
	@Override
	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
	    super.validationChanged(affectedObjects);
	}
}
