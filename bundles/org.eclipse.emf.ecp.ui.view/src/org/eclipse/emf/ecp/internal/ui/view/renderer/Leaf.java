package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.view.model.Renderable;

public class Leaf<T extends Renderable> extends Node<T> {

	public Leaf(T model, ECPControlContext controlContext) {
		super(model, controlContext);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
	
}
