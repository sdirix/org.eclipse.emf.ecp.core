package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.View;

public class CategoryNodeBuilder implements NodeBuilder<Category> {

	@Override
	public Node build(Category category) {
		Node node = new Node(category);
		node.addChild(NodeBuilders.INSTANCE.build(category.getComposite()));
		return node;
	}

}
