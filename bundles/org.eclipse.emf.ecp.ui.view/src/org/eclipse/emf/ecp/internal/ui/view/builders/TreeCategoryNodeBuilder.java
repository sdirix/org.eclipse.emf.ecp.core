package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.ecp.view.model.View;

public class TreeCategoryNodeBuilder implements NodeBuilder<TreeCategory> {

	@Override
	public Node build(TreeCategory treeCategory, Object... assets) {
		Node node = new Node(treeCategory, false);
		
		node.addChild(NodeBuilders.INSTANCE.build(treeCategory.getChildComposite(), assets));
		
		return node;
	}

}
