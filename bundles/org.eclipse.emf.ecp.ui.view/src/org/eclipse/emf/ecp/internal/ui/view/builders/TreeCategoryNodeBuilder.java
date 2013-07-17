package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class TreeCategoryNodeBuilder implements NodeBuilder<TreeCategory> {

	@Override
	public Node<TreeCategory> build(TreeCategory treeCategory, ECPControlContext context, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		Node<TreeCategory> node = new Node<TreeCategory>(treeCategory, context, false);
		
		node.addChild(NodeBuilders.INSTANCE.build(treeCategory.getChildComposite(), context, adapterFactoryItemDelegator));
		
		return node;
	}

}
