/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.groupedgrid.ui.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.edit.groupedgrid.model.VGroupedGrid;
import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * The Class GroupedGridNodeBuilder.
 */
public class GroupedGridNodeBuilder implements CustomNodeBuilder {

	/**
	 * Instantiates a new grouped grid node builder.
	 */
	public GroupedGridNodeBuilder() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder#getCustomNodeBuilders()
	 */
	public Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> getCustomNodeBuilders() {
		final Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> result = new HashMap<Class<? extends Renderable>, NodeBuilder<? extends Renderable>>();
		result.put(VGroupedGrid.class, new NodeBuilderGroupedGrid());
		return result;
	}
}
