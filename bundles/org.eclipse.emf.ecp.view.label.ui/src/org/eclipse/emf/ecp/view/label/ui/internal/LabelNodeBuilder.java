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
package org.eclipse.emf.ecp.view.label.ui.internal;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.RenderableNodeBuilder;
import org.eclipse.emf.ecp.view.label.model.VLabel;
import org.eclipse.emf.ecp.view.model.VElement;

/**
 * Label Node Builder Class for providing an own Node builder for the Label element.
 * 
 * @author Eugen Neufeld
 * 
 */
public class LabelNodeBuilder implements CustomNodeBuilder {

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder#getCustomNodeBuilders()
	 */
	public Map<Class<? extends VElement>, NodeBuilder<? extends VElement>> getCustomNodeBuilders() {
		Map<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, NodeBuilder<? extends VElement>> builders;
		builders = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, NodeBuilder<? extends VElement>>();
		builders.put(VLabel.class, new RenderableNodeBuilder<VLabel>());
		return builders;
	}

}
