/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.groupedgrid.ui.internal;

import org.eclipse.emf.ecp.edit.groupedgrid.model.VGroup;
import org.eclipse.emf.ecp.edit.groupedgrid.model.VGroupedGrid;
import org.eclipse.emf.ecp.edit.groupedgrid.model.VRow;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * @author Eugen Neufeld
 * 
 */
public class NodeBuilderGroupedGrid implements
	NodeBuilder<VGroupedGrid> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder#build(org.eclipse.emf.ecp.view.model.Renderable,
	 *      org.eclipse.emf.ecp.edit.spi.ECPControlContext, org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator)
	 */
	public Node<VGroupedGrid> build(VGroupedGrid model, ECPControlContext context,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		final Node<VGroupedGrid> node = new Node<VGroupedGrid>(model, context);

		for (final VGroup group : model.getGroups()) {
			for (final VRow row : group.getRows()) {
				for (final org.eclipse.emf.ecp.view.model.Composite composite : row.getChildren()) {
					node.addChild(NodeBuilders.INSTANCE.build(composite, context, adapterFactoryItemDelegator));
				}
			}
		}
		return node;
	}
}
