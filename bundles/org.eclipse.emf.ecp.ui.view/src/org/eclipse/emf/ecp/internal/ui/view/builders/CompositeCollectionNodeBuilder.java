package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class CompositeCollectionNodeBuilder<T extends org.eclipse.emf.ecp.view.model.CompositeCollection> implements
	NodeBuilder<T> {

	public Node<T> build(T model, ECPControlContext context, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		Node<T> node = new Node<T>(model, context);

		for (org.eclipse.emf.ecp.view.model.Composite composite : model.getComposites()) {
			node.addChild(NodeBuilders.INSTANCE.build(composite, context, adapterFactoryItemDelegator));
		}

		return node;
	}

}
