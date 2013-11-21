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

import org.eclipse.emf.ecp.edit.internal.swt.util.DoubleColumnRow;
import org.eclipse.emf.ecp.edit.internal.swt.util.SingleColumnRow;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
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
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer#renderSWT(org.eclipse.emf.ecp.internal.ui.view.renderer.Node,
	 *      org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator, java.lang.Object[])
	 */
	@Override
	public List<RenderingResultRow<Control>> renderSWT(final Node<VView> viewNode,
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator,

		Object... initData) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Composite parent = getParentFromInitData(initData);

		return renderChildren(parent, viewNode, adapterFactoryItemDelegator);

	}

	/**
	 * Render children.
	 * 
	 * @param parent the parent
	 * @param node the node
	 * @param adapterFactoryItemDelegator the adapter factory item delegator
	 * @return the composite
	 * @throws NoRendererFoundException the no renderer found exception
	 */
	private List<RenderingResultRow<Control>> renderChildren(Composite parent, Node<VView> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator)
		throws NoRendererFoundException {
		final Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setBackground(parent.getBackground());
		// Gridlayout does not have an overflow as other Layouts might have.
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(columnComposite);

		node.addRenderingResultDelegator(withSWT(columnComposite));

		for (final Node<? extends VElement> child : node.getChildren()) {

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

	@Override
	protected void setLayoutDataForResultRows(final List<RenderingResultRow<Control>> resultRows) {
		for (final RenderingResultRow<Control> row : resultRows) {
			if (SingleColumnRow.class.isInstance(row)) {
				GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true)
					.applyTo(((SingleColumnRow) row).getControl());
			}
			else if (DoubleColumnRow.class.isInstance(row)) {
				GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).grab(false, false)
					.applyTo(((DoubleColumnRow) row).getLeftControl());
				GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
					.applyTo(((DoubleColumnRow) row).getRightControl());
			}
		}
	}

}
