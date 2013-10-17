/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.separator.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.RenderableNodeBuilder;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.separator.model.Separator;

/**
 * NodeBuilder for separator.
 * 
 * @author Jonas
 * 
 */
public class SeparatorNodeBuilder implements CustomNodeBuilder {

	/**
	 * 
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder#getCustomNodeBuilders()
	 */
	public Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> getCustomNodeBuilders() {
		Map<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, NodeBuilder<? extends Renderable>> builders;
		builders = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.Renderable>, NodeBuilder<? extends Renderable>>() {
			private static final long serialVersionUID = 6030832571610301712L;

			{
				put(Separator.class, new RenderableNodeBuilder<Separator>());
			}
		};
		return builders;
	}

}
