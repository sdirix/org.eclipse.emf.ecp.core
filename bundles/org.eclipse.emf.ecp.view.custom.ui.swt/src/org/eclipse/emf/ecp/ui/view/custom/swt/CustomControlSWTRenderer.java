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
package org.eclipse.emf.ecp.ui.view.custom.swt;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControlInitException;
import org.eclipse.emf.ecp.view.custom.ui.internal.swt.Activator;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A Renderer for Custom Controls.
 * 
 * @author eneufeld
 * 
 */
public class CustomControlSWTRenderer extends
	AbstractSWTRenderer<CustomControl> {

	@Override
	protected List<RenderingResultRow<Control>> renderSWT(Node<CustomControl> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final CustomControl customControl = node.getRenderable();

		try {
			final ECPAbstractCustomControlSWT customControlSWT = (ECPAbstractCustomControlSWT) customControl
				.getECPCustomControl();
			customControlSWT.init(node.getControlContext());

			final Composite parent = getParentFromInitData(initData);
			customControlSWT.setMessageShell(parent.getShell());
			final List<RenderingResultRow<Control>> renderingResultRows = customControlSWT
				.createControl(parent);
			if (customControl.isReadonly()) {
				customControlSWT.setEditable(customControl.isReadonly());

			}

			final Set<Control> controls = new LinkedHashSet<Control>();
			for (final RenderingResultRow<Control> row : renderingResultRows) {
				controls.addAll(row.getControls());
			}

			node.addRenderingResultDelegator(new SWTRenderingResultCustomControl(customControlSWT, customControl,
				controls.toArray(new Control[0])));

			return renderingResultRows;
		} catch (final ECPCustomControlInitException ex) {
			Activator.log(ex);
		}

		return null;
	}

}
