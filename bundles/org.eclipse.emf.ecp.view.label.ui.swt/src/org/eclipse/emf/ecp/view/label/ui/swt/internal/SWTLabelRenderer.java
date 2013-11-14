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
package org.eclipse.emf.ecp.view.label.ui.swt.internal;

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.label.model.VLabel;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Renders an {@link org.eclipse.emf.ecp.view.label.model.VLabel Label} to a SWT {@link Label}.
 * 
 */
public class SWTLabelRenderer extends AbstractSWTRenderer<org.eclipse.emf.ecp.view.label.model.VLabel> {
	/**
	 * Singleton instance for use by other renderers.
	 */
	public static final SWTLabelRenderer INSTANCE = new SWTLabelRenderer();

	@Override
	public List<RenderingResultRow<Control>> render(VLabel separator,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final Composite parent = getParentFromInitData(initData);
		final Label label = new Label(parent, SWT.NONE);
		label.setText(separator.getName());
		label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_ui_" + separator.getStyle()); //$NON-NLS-1$

		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER)
			.grab(true, true).span(2, 1).applyTo(label);

		return createResult(label);
	}

}
