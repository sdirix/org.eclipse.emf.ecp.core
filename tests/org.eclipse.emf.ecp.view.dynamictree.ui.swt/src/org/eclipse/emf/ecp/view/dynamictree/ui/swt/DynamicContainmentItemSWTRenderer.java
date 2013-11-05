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
package org.eclipse.emf.ecp.view.dynamictree.ui.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.SWTRenderer;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree;
import org.eclipse.emf.ecp.view.model.VElement;

/**
 * Registers custom SWT renderers for {@link DynamicContainmentItem}s and {@link DynamicContainmentTree}s.
 * 
 * @author emuller
 * 
 */
public class DynamicContainmentItemSWTRenderer implements CustomSWTRenderer {

	/**
	 * Default constructor.
	 */
	public DynamicContainmentItemSWTRenderer() {
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.CustomSWTRenderer#getCustomRenderers()
	 */
	public Map<Class<? extends VElement>, SWTRenderer<?>> getCustomRenderers() {
		final Map<Class<? extends VElement>, SWTRenderer<?>> map = new HashMap<Class<? extends VElement>, SWTRenderer<?>>();
		map.put(DynamicContainmentTree.class, new SWTDynamicContainmentTreeRenderer());
		map.put(DynamicContainmentItem.class, new SWTDynamicContainmentItemRenderer());
		return map;
	}

}
