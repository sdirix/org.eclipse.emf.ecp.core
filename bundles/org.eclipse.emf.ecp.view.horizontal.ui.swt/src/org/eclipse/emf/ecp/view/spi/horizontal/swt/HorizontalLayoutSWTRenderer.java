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
package org.eclipse.emf.ecp.view.spi.horizontal.swt;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecp.view.internal.horizontal.swt.Activator;
import org.eclipse.emf.ecp.view.spi.horizontal.model.VHorizontalLayout;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCellDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescriptionFactory;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The SWT Renderer for the {@link VHorizontalLayout}. It renders all elements next to each other.
 * 
 * @author Eugen Neufeld
 * @since 1.2
 * 
 */
public class HorizontalLayoutSWTRenderer extends AbstractSWTRenderer<VHorizontalLayout> {

	private static final String CONTROL_COLUMN_COMPOSITE = "org_eclipse_emf_ecp_ui_layout_horizontal"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription()
	 */
	@Override
	public GridDescription getGridDescription() {
		return GridDescriptionFactory.INSTANCE.createSimpleGrid(1, 1);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#renderControl(int, org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.emf.ecp.view.spi.model.VElement, org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	protected Control renderControl(GridCell gridCell, Composite parent) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		if (gridCell.getColumn() != 0) {
			return null;
		}
		final Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setBackground(parent.getBackground());
		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN_COMPOSITE);

		final Map<VContainedElement, AbstractSWTRenderer<VElement>> elementRendererMap = new LinkedHashMap<VContainedElement, AbstractSWTRenderer<VElement>>();
		for (final VContainedElement child : getVElement().getChildren()) {

			final AbstractSWTRenderer<VElement> renderer = getSWTRendererFactory().getRenderer(child,
				getViewModelContext());
			if (renderer == null) {
				Activator
					.getDefault()
					.getLog()
					.log(
						new Status(IStatus.INFO, Activator.PLUGIN_ID, String.format(
							"No Renderer for %s found.", child.eClass().getName()))); //$NON-NLS-1$
				continue;
			}
			elementRendererMap.put(child, renderer);
		}
		// Gridlayout does not have an overflow as other Layouts might have.

		columnComposite.setLayout(getLayoutHelper().getColumnLayout(elementRendererMap.size(), true));
		for (final VContainedElement child : elementRendererMap.keySet()) {
			final AbstractSWTRenderer<VElement> renderer = elementRendererMap.get(child);
			final Composite column = new Composite(columnComposite, SWT.NONE);
			column.setBackground(parent.getBackground());

			column.setLayoutData(getLayoutHelper().getSpanningLayoutData(1, 1));

			column.setLayout(getLayoutHelper().getColumnLayout(renderer.getGridDescription().getColumns(), false));

			try {
				for (final GridCell childGridCell : renderer.getGridDescription().getGrid()) {
					final Control control = renderer.render(childGridCell, column);
					// TODO who should apply the layout
					setLayoutDataForControl(childGridCell, renderer.getGridDescription(),
						renderer.getGridDescription().getColumns(), new LinkedHashSet<GridCellDescription>(),
						new LinkedHashSet<GridCellDescription>(),
						control);
				}
				renderer.postRender(column);
			} catch (final NoPropertyDescriptorFoundExeption e) {
				continue;
			}
		}

		return columnComposite;
	}

}
