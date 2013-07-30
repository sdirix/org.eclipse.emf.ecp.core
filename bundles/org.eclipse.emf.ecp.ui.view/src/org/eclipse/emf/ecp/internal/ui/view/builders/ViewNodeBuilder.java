package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class ViewNodeBuilder implements NodeBuilder<View> {

	public Node<View> build(View view, ECPControlContext context,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		final Node<View> node = new Node<View>(view, context);

		if (view.getCategorizations().isEmpty()) {
			for (final Composite composite : view.getChildren()) {
				node.addChild(NodeBuilders.INSTANCE.build(composite, context, adapterFactoryItemDelegator));
			}
		}
		else {
			for (final AbstractCategorization categorization : view.getCategorizations()) {
				node.addChild(NodeBuilders.INSTANCE.build(categorization, context, adapterFactoryItemDelegator));
			}
		}

		return node;
	}

}
