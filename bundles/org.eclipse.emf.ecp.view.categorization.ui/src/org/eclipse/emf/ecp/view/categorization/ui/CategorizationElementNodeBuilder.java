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
package org.eclipse.emf.ecp.view.categorization.ui;

import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.categorization.model.VAbstractCategorization;
import org.eclipse.emf.ecp.view.categorization.model.VCategorizationElement;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * NodeBuilder for {@link VCategorizationElement}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class CategorizationElementNodeBuilder implements NodeBuilder<VCategorizationElement> {
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder#build(org.eclipse.emf.ecp.view.model.VElement,
	 *      org.eclipse.emf.ecp.edit.spi.ECPControlContext, org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator)
	 */
	public Node<VCategorizationElement> build(VCategorizationElement view, ECPControlContext context,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		final Node<VCategorizationElement> node = new Node<VCategorizationElement>(view, context);

		for (final VAbstractCategorization categorization : view.getCategorizations()) {
			node.addChild(NodeBuilders.INSTANCE.build(categorization, context, adapterFactoryItemDelegator));
		}

		return node;
	}

}
