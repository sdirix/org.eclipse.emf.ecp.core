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
package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTColumnRenderer extends AbstractSWTRenderer<Column> {
	public static final SWTColumnRenderer INSTANCE = new SWTColumnRenderer();
	private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_control_column";

	@Override
	public Control renderSWT(Node<Column> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Composite parent = getParentFromInitData(initData);
		final Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN);
		columnComposite.setBackground(parent.getBackground());

		node.addRenderingResultDelegator(withSWT(columnComposite));

		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(columnComposite);

		for (final Node<? extends Renderable> child : node.getChildren()) {

			Control childControl;
			try {
				childControl = SWTRenderers.INSTANCE.render(
					columnComposite, child, adapterFactoryItemDelegator);
			} catch (final NoPropertyDescriptorFoundExeption e) {
				continue;
			}

			// TOOD; when does this case apply?
			if (childControl == null) {
				continue;
			}

			// TODO Add check to handle differently if label is shown
			if (!child.isLeaf()) {
				GridDataFactory.fillDefaults()
					.align(SWT.FILL, SWT.BEGINNING)
					.grab(true, false)
					.span(2, 1).applyTo(childControl);
			}
		}

		return columnComposite;
	}
}
