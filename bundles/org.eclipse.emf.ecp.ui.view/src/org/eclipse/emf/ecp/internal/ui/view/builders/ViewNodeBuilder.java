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
import org.eclipse.emf.ecp.view.model.VAbstractCategorization;
import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class ViewNodeBuilder implements NodeBuilder<VView> {

	public Node<VView> build(VView view, ECPControlContext context,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		final Node<VView> node = new Node<VView>(view, context);

		if (view.getCategorizations().isEmpty()) {
			for (final VContainedElement composite : view.getChildren()) {
				node.addChild(NodeBuilders.INSTANCE.build(composite, context, adapterFactoryItemDelegator));
			}
		}
		else {
			for (final VAbstractCategorization categorization : view.getCategorizations()) {
				node.addChild(NodeBuilders.INSTANCE.build(categorization, context, adapterFactoryItemDelegator));
			}
		}

		return node;
	}

}
