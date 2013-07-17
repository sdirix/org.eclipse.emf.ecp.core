package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class RenderableNodeBuilder<T extends org.eclipse.emf.ecp.view.model.Renderable> implements NodeBuilder<T> {

	@Override
	public Node<T> build(T model, ECPControlContext context, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		return new Node<T>(model, context);
	}
	
}
