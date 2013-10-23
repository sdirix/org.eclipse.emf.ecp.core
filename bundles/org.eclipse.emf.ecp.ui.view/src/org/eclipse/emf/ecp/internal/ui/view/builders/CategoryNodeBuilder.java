package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class CategoryNodeBuilder implements NodeBuilder<Category> {

	public Node<Category> build(Category category, ECPControlContext context,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		final Node<Category> node = new Node<Category>(category, context);
		if (category.getComposite() == null) {
			return node;
		}
		final Node<Composite> child = NodeBuilders.INSTANCE.build(category.getComposite(), context,
			adapterFactoryItemDelegator);
		node.addChild(child);
		return node;
	}

}
