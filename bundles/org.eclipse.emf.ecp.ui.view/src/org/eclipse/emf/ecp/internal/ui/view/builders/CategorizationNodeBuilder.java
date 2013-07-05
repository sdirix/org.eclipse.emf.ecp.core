package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class CategorizationNodeBuilder implements NodeBuilder<Categorization> {

	@Override
	public Node build(Categorization categorization, ECPControlContext context, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		Node node = new Node(categorization,context);
		for (AbstractCategorization childCategorization : categorization.getCategorizations()) {
			node.addChild(NodeBuilders.INSTANCE.build(childCategorization, context, adapterFactoryItemDelegator));
		}
		return node;
	}

}
