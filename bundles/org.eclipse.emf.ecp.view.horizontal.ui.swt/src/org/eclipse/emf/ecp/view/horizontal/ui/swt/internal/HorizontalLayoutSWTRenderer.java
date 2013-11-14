/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.view.horizontal.ui.swt.internal;

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.ui.view.swt.internal.SWTRenderers;
import org.eclipse.emf.ecp.view.horizontal.model.VHorizontalLayout;
import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The SWT Renderer for the {@link VHorizontalLayout}. It renders all elements next to each other.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
// TODO no API
public class HorizontalLayoutSWTRenderer extends AbstractSWTRenderer<VHorizontalLayout> {

	private static final String CONTROL_COLUMN_COMPOSITE = "org_eclipse_emf_ecp_ui_layout_horizontal"; //$NON-NLS-1$

	@Override
	public List<RenderingResultRow<Control>> render(VHorizontalLayout vHorizontalLayout,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		// TODO Add check whether label is shown
		// Label l=new Label(getParent(), SWT.NONE);
		// l.setText(modelColumnComposite.getName());
		final Composite parent = getParentFromInitData(initData);

		final Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setBackground(parent.getBackground());
		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN_COMPOSITE);

		columnComposite.setLayout(getLayoutHelper().getColumnLayout(vHorizontalLayout.getChildren().size(), true));

		for (final VContainedElement child : vHorizontalLayout.getChildren()) {

			final Composite column = new Composite(columnComposite, SWT.NONE);
			column.setBackground(parent.getBackground());

			column.setLayoutData(getLayoutHelper().getSpanningLayoutData(1, 1));

			column.setLayout(getLayoutHelper().getColumnLayout(2, false));

			List<RenderingResultRow<Control>> resultRows;

			try {
				resultRows = SWTRenderers.INSTANCE.render(column, child, adapterFactoryItemDelegator);
			} catch (final NoPropertyDescriptorFoundExeption e) {
				continue;
			}

			setLayoutDataForResultRows(resultRows);

			// if (!node.isVisible()) {
			// node.show(false);
			// }
		}

		return createResult(columnComposite);
	}

}
