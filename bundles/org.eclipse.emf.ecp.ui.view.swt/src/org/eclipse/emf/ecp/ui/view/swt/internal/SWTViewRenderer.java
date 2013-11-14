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
 * Johannes Falterimeier - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.internal;

import java.util.List;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

// TODO: Auto-generated Javadoc
/**
 * The Class SWTViewRenderer.
 */
public class SWTViewRenderer extends AbstractSWTRenderer<VView> {

	/** The Constant INSTANCE. */
	public static final SWTViewRenderer INSTANCE = new SWTViewRenderer();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer#render(org.eclipse.emf.ecp.view.model.VElement,
	 *      org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator, java.lang.Object[])
	 */
	@Override
	public List<RenderingResultRow<Control>> render(final VView vView,
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator,

		Object... initData) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Composite parent = getParentFromInitData(initData);

		final Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setBackground(parent.getBackground());
		columnComposite.setLayout(getLayoutHelper().getColumnLayout(2, false));

		for (final VContainedElement child : vView.getChildren()) {

			List<RenderingResultRow<Control>> resultRows;
			try {
				resultRows = SWTRenderers.INSTANCE.render(
					columnComposite, child, adapterFactoryItemDelegator);
			} catch (final NoPropertyDescriptorFoundExeption e) {
				continue;
			}

			// TOOD; when does this case apply?
			if (resultRows == null) {
				continue;
			}

			setLayoutDataForResultRows(resultRows);
		}

		return createResult(columnComposite);

	}

}
