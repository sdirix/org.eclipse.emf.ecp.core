/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 */
package org.eclipse.emf.ecp.view.dynamictree.model.test;

import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class DynamicContainmentItemNodeBuilder implements NodeBuilder<DynamicContainmentItem> {

	public Node<DynamicContainmentItem> build(DynamicContainmentItem renderable,
		ECPControlContext controlContext,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {

		final Node<DynamicContainmentItem> node = new Node<DynamicContainmentItem>(renderable, controlContext);
		final Node<VContainedElement> compositeNode = NodeBuilders.INSTANCE.build(renderable.getComposite(),
			controlContext);
		node.addChild(compositeNode);

		return node;
	}

}
