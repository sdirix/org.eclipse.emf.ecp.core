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
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

public class RenderableNodeBuilder<T extends org.eclipse.emf.ecp.view.model.Renderable> implements NodeBuilder<T> {

	public Node<T> build(T model, ECPControlContext context, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		return new Node<T>(model, context);
	}

}
