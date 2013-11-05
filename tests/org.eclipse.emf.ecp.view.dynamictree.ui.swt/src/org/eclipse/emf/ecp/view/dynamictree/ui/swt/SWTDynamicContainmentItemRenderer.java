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

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.SWTRenderers;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Control;

/**
 * SWT renderer for {@link DynamicContainmentItem}s.
 * 
 * @author emueller
 */
public class SWTDynamicContainmentItemRenderer extends AbstractSWTRenderer<DynamicContainmentItem> {

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer#renderSWT(org.eclipse.emf.ecp.internal.ui.view.renderer.Node,
	 *      org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator, java.lang.Object[])
	 */
	@Override
	protected List<RenderingResultRow<Control>> renderSWT(Node<DynamicContainmentItem> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... objects)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Node<?> child = node.getChildren().get(0);

		List<RenderingResultRow<Control>> childControl;
		try {
			childControl = SWTRenderers.INSTANCE.render(getParentFromInitData(objects), child,
				adapterFactoryItemDelegator);
		} catch (final NoPropertyDescriptorFoundExeption e) {
			return null;
		}
		return childControl;
	}

}
