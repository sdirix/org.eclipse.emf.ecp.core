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
package org.eclipse.emf.ecp.view.group.ui.swt.internal;

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderers;
import org.eclipse.emf.ecp.view.group.model.Group;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Renders a SWT group.
 * 
 */
public class SWTGroupRenderer extends AbstractSWTRenderer<Group> {
	/**
	 * Singleton instance of the SWT Group renderer.
	 */
	public static final SWTGroupRenderer INSTANCE = new SWTGroupRenderer();
	private static final Object CONTROL_GROUP = "org_eclipse_emf_ecp_ui_control_group";

	@Override
	public List<RenderingResultRow<Control>> renderSWT(Node<Group> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Composite parent = getParentFromInitData(initData);
		final Group modelGroup = node.getRenderable();
		final org.eclipse.swt.widgets.Group group = new org.eclipse.swt.widgets.Group(parent, SWT.TITLE);
		group.setData(CUSTOM_VARIANT, CONTROL_GROUP);
		if (modelGroup.getName() != null) {
			group.setText(modelGroup.getName());
		}

		group.setLayout(getLayoutHelper().getColumnLayout(2, false));

		node.addRenderingResultDelegator(withSWT(group));

		for (final Node<? extends Renderable> child : node.getChildren()) {

			List<RenderingResultRow<Control>> resultRows;
			try {
				resultRows = SWTRenderers.INSTANCE.render(group, child, adapterFactoryItemDelegator);
			} catch (final NoPropertyDescriptorFoundExeption e) {
				continue;
			}
			// TODO when does this case apply?
			if (resultRows == null) {
				continue;
			}

			setLayoutDataForResultRows(resultRows);
		}

		return createResult(group);
	}
}
