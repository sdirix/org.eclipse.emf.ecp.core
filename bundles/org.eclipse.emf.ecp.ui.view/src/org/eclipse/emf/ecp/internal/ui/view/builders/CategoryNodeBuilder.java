/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Muenchen - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.view.builders;

import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.VCategory;
import org.eclipse.emf.ecp.view.model.VContainableElement;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class CategoryNodeBuilder implements NodeBuilder<VCategory> {

	public Node<VCategory> build(VCategory category, ECPControlContext context,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		final Node<VCategory> node = new Node<VCategory>(category, context);
		if (category.getComposite() == null) {
			return node;
		}
		final Node<VContainableElement> child = NodeBuilders.INSTANCE.build(category.getComposite(), context,
			adapterFactoryItemDelegator);
		node.addChild(child);
		return node;
	}

}
