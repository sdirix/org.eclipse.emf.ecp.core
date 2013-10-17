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
package org.eclipse.emf.ecp.view.custom.internal.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * This registers the {@link org.eclipse.emf.ecp.internal.ui.view.builders.RenderableNodeBuilder RenderableNodeBuilder}
 * as the NodeBuilder for {@link CustomControl}.
 * 
 * @author Eugen Neufeld
 */
// API or SPI definition missing
public class NodeBuilderCustom implements CustomNodeBuilder {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder#getCustomNodeBuilders()
	 */
	public Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> getCustomNodeBuilders() {

		final Map<Class<? extends Renderable>, NodeBuilder<? extends Renderable>> builders = new HashMap<Class<? extends Renderable>, NodeBuilder<? extends Renderable>>();
		builders.put(CustomControl.class,
			new CustomControlNodeBuilder());
		return builders;
	}

}
