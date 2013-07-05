package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;

public class CompositeCollectionNodeBuilder<T extends org.eclipse.emf.ecp.view.model.CompositeCollection> implements NodeBuilder<T> {

	@Override
	public Node build(T model, Object[] assets) {
		Node node = new Node(model);
		
		for (org.eclipse.emf.ecp.view.model.Composite composite : model.getComposites()) {
			node.addChild(NodeBuilders.INSTANCE.build(composite, assets));
		}
		
		return node;
	}
	
}
