package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.View;

public class CategorizationNodeBuilder implements NodeBuilder<Categorization> {

	@Override
	public Node build(Categorization categorization, Object... assets) {
		Node node = new Node(categorization);
		for (AbstractCategorization childCategorization : categorization.getCategorizations()) {
			node.addChild(NodeBuilders.INSTANCE.build(childCategorization, assets));
		}
		return node;
	}

}
