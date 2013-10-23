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
package org.eclipse.emf.ecp.view.vertical.ui.internal;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecp.internal.ui.view.builders.CompositeCollectionNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.vertical.model.VVerticalLayout;

/**
 * Vertical Node Builder Class for providing an own Node builder for the Vertical element.
 * 
 * @author Eugen Neufeld
 * 
 */
public class VerticalNodeBuilder implements CustomNodeBuilder {

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.CustomNodeBuilder#getCustomNodeBuilders()
	 */
	public Map<Class<? extends VElement>, NodeBuilder<? extends VElement>> getCustomNodeBuilders() {
		Map<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, NodeBuilder<? extends VElement>> builders;
		builders = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, NodeBuilder<? extends VElement>>();
		builders.put(VVerticalLayout.class, new CompositeCollectionNodeBuilder<VVerticalLayout>());
		return builders;
	}
}
