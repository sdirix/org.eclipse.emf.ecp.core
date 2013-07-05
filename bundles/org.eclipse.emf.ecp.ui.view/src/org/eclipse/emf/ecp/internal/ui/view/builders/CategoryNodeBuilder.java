package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class CategoryNodeBuilder implements NodeBuilder<Category> {

	@Override
	public Node build(Category category,ECPControlContext context, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		Node node = new Node(category, context);
		Node child = NodeBuilders.INSTANCE.build(category.getComposite(), context, adapterFactoryItemDelegator);
		child.setVisible(false);
		node.addChild(child);
		return node;
	}

}
