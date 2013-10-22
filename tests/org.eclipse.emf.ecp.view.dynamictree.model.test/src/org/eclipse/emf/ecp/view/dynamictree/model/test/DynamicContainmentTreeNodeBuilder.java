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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree;
import org.eclipse.emf.ecp.view.model.Renderable;

public class DynamicContainmentTreeNodeBuilder implements CustomNodeBuilder {

	public DynamicContainmentTreeNodeBuilder() {
	}

	public Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> getCustomNodeBuilders() {
		final Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> builders =
			new HashMap<Class<? extends Renderable>, NodeBuilder<? extends Renderable>>();
		builders.put(DynamicContainmentTree.class, new DynamicNodeBuilder());
		builders.put(DynamicContainmentItem.class, new DynamicContainmentItemNodeBuilder());
		return builders;
	}

}
