package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.View;

public class ViewNodeBuilder implements NodeBuilder<View> {

	@Override
	public Node build(View view, Object... assets) {
		Node node = new Node(view);
		
		for (AbstractCategorization categorization : view.getCategorizations()) {
			node.addChild(NodeBuilders.INSTANCE.build(categorization, assets));
		}
		
		return node;
	}

}
