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
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.SWTRenderers;
import org.eclipse.emf.ecp.view.group.model.VGroup;
import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Renders a SWT group.
 * 
 */
public class SWTGroupRenderer extends AbstractSWTRenderer<VGroup> {
	/**
	 * Singleton instance of the SWT Group renderer.
	 */
	public static final SWTGroupRenderer INSTANCE = new SWTGroupRenderer();
	private static final Object CONTROL_GROUP = "org_eclipse_emf_ecp_ui_control_group"; //$NON-NLS-1$

	@Override
	public List<RenderingResultRow<Control>> render(VGroup vGroup,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Composite parent = getParentFromInitData(initData);

		final org.eclipse.swt.widgets.Group group = new org.eclipse.swt.widgets.Group(parent, SWT.TITLE);
		group.setData(CUSTOM_VARIANT, CONTROL_GROUP);
		if (vGroup.getName() != null) {
			group.setText(vGroup.getName());
		}
		group.setBackground(parent.getBackground());
		group.setLayout(getLayoutHelper().getColumnLayout(2, false));

		for (final VContainedElement child : vGroup.getChildren()) {

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
