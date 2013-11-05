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
package org.eclipse.emf.ecp.view.dynamictree.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree;
import org.eclipse.emf.ecp.view.model.VElement;

/**
 * Creates a custom node builder for {@link DynamicContainmentTree}s and {@link DynamicContainmentItem}s.
 * 
 * @author emueller
 * 
 */
public class DynamicContainmentTreeNodeBuilder implements CustomNodeBuilder {

	/**
	 * Default constructor.
	 */
	public DynamicContainmentTreeNodeBuilder() {
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder#getCustomNodeBuilders()
	 */
	public Map<Class<? extends VElement>, NodeBuilder<? extends VElement>> getCustomNodeBuilders() {
		final Map<Class<? extends VElement>, NodeBuilder<? extends VElement>> builders =
			new HashMap<Class<? extends VElement>, NodeBuilder<? extends VElement>>();
		builders.put(DynamicContainmentTree.class, new DynamicNodeBuilder());
		builders.put(DynamicContainmentItem.class, new DynamicContainmentItemNodeBuilder());
		return builders;
	}

}
