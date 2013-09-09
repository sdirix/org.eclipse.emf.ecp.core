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

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControlInitException;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
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

		// TODO: handle exceptions
		try {
			final ECPAbstractCustomControlSWT categoryComposite = (ECPAbstractCustomControlSWT) customControl
				.getECPCustomControl();
			categoryComposite.init(node.getControlContext());

			final Composite parent = getParentFromInitData(initData);
			categoryComposite.setMessageShell(parent.getShell());
			final List<RenderingResultRow<Control>> renderingResultRows = categoryComposite
				.createControls(parent);
			node.addRenderingResultDelegator(new SWTRenderingResultCustomControl(categoryComposite, customControl,
				getParentFromInitData(initData)));

			return renderingResultRows;
		} catch (final ECPCustomControlInitException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		return null;
	}

}
