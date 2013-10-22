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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegator;
import org.eclipse.emf.ecp.view.custom.model.impl.VCustomControlImpl;
import org.eclipse.emf.ecp.view.group.model.impl.VGroupImpl;
import org.eclipse.emf.ecp.view.label.model.impl.VLabelImpl;
import org.eclipse.emf.ecp.view.model.impl.ControlImpl;
import org.eclipse.emf.ecp.view.table.model.impl.VTableControlImpl;

public class AbstractCategorizationFilterHelper {

	@SuppressWarnings("serial")
	private static Set<Class<?>> filteredClasses = new LinkedHashSet<Class<?>>() {
		{
			add(ControlImpl.class);
			add(VTableControlImpl.class);
			add(VGroupImpl.class);
			add(VLabelImpl.class);
			add(VCustomControlImpl.class);
		}
	};

	public static List<Node<?>> filterNodes(final Node<?> node) {
		final List<Node<?>> result = new ArrayList<Node<?>>();
		final List<Node<?>> children = node.getChildren();
		for (final Node<?> child : children) {
			if (child.isVisible()) {
				if (filteredClasses.contains(child.getLabelObject().getClass())) {
					result.addAll(filterNodes(child));
				} else {
					result.add(child);
				}
			}
		}
		return result;
	}

	public interface RenderingResultDelegatorProvider {
		RenderingResultDelegator getRenderingResultDelegator(Node<?> node);
	}

}
