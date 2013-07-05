package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class ViewNodeBuilder implements NodeBuilder<View> {

	@Override
	public Node build(View view, ECPControlContext context, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		Node node = new Node(view, context);
		
		for (AbstractCategorization categorization : view.getCategorizations()) {
			node.addChild(NodeBuilders.INSTANCE.build(categorization, context, adapterFactoryItemDelegator));
		}
		
		return node;
	}

}
