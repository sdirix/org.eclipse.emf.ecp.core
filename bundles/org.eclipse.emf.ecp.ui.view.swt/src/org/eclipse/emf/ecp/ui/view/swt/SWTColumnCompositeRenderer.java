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
package org.eclipse.emf.ecp.ui.view.swt;

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTColumnCompositeRenderer extends AbstractSWTRenderer<ColumnComposite> {

	public static final SWTColumnCompositeRenderer INSTANCE = new SWTColumnCompositeRenderer();
	private static final String CONTROL_COLUMN_COMPOSITE = "org_eclipse_emf_ecp_ui_control_column_composite";

	@Override
	public List<RenderingResultRow<Control>> renderSWT(Node<ColumnComposite> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		// TODO Add check whether label is shown
		// Label l=new Label(getParent(), SWT.NONE);
		// l.setText(modelColumnComposite.getName());
		final Composite parent = getParentFromInitData(initData);
		final ColumnComposite modelColumnComposite = node.getRenderable();

		final Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setBackground(parent.getBackground());
		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN_COMPOSITE);

		columnComposite.setLayout(getLayoutHelper().getColumnLayout(modelColumnComposite.getComposites().size(), true));

		// SWTLifted node = new SWTLifted(columnComposite, modelColumnComposite, controlContext);

		node.addRenderingResultDelegator(withSWT(columnComposite));

		for (final Node<? extends Renderable> child : node.getChildren()) {

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

			if (!node.isVisible()) {
				node.show(false);
			}
		}

		return createResult(columnComposite);
	}

}
