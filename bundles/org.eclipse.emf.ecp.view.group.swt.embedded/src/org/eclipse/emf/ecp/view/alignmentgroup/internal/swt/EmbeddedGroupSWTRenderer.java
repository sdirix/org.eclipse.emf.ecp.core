/**
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 */
package org.eclipse.emf.ecp.view.alignmentgroup.internal.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecp.view.spi.group.model.VGroup;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.AbstractAdditionalSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridCell;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescription;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescriptionFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * A Group renderer, which doesn't enforce its own layout.
 * 
 * @author Eugen Neufeld
 * 
 */
public class EmbeddedGroupSWTRenderer extends AbstractSWTRenderer<VGroup> {

	private GridDescription currentGridDescription;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription(org.eclipse.emf.ecp.view.spi.swt.layout.GridDescription)
	 */
	@Override
	public GridDescription getGridDescription(GridDescription gridDescription) {
		currentGridDescription = new GridDescription();
		final List<GridCell> grid = new ArrayList<GridCell>();
		currentGridDescription.setRows(getVElement().getChildren().size() + 1);
		currentGridDescription.setGrid(grid);

		int row = 1;
		final Map<Integer, List<GridCell>> gridCellsPerRow = new LinkedHashMap<Integer, List<GridCell>>();
		for (final VContainedElement containedElement : getVElement().getChildren()) {
			gridCellsPerRow.put(row, new ArrayList<GridCell>());
			final AbstractSWTRenderer<VElement> renderer = getSWTRendererFactory().getRenderer(containedElement,
				getViewModelContext());
			final Collection<AbstractAdditionalSWTRenderer<VElement>> additionalRenderers = getSWTRendererFactory()
				.getAdditionalRenderer(containedElement, getViewModelContext());
			GridDescription rendererGridDescription = renderer.getGridDescription(GridDescriptionFactory.INSTANCE
				.createEmptyGridDescription());
			for (final AbstractAdditionalSWTRenderer<VElement> additionalRenderer : additionalRenderers) {
				rendererGridDescription = additionalRenderer.getGridDescription(rendererGridDescription);
			}
			if (currentGridDescription.getColumns() < rendererGridDescription.getColumns()) {
				currentGridDescription.setColumns(rendererGridDescription.getColumns());
			}
			for (final GridCell rendererGridCell : rendererGridDescription.getGrid()) {
				final GridCell gc = new GridCell(row, rendererGridCell.getColumn(), rendererGridCell.getRenderer());
				currentGridDescription.getGrid().add(gc);
				gridCellsPerRow.get(row).add(gc);
			}
			row++;
		}
		for (int i = 0; i < currentGridDescription.getColumns(); i++) {
			final GridCell gridCell = new GridCell(0, i, this);
			gridCell.setVerticalGrab(false);
			gridCell.setVerticalFill(false);
			gridCell.setHorizontalFill(false);
			gridCell.setHorizontalGrab(false);
			grid.add(i, gridCell); // label;
		}

		for (final Integer gdRow : gridCellsPerRow.keySet()) {
			final List<GridCell> rowGridCells = gridCellsPerRow.get(gdRow);
			if (currentGridDescription.getColumns() > rowGridCells.size()) {
				// span last cell -> x columns - #cells + 1 as the current cell already was subtracted
				rowGridCells.get(rowGridCells.size() - 1).setHorizontalSpan(
					currentGridDescription.getColumns() - rowGridCells.size() + 1);
			}
		}
		return currentGridDescription;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#renderControl(org.eclipse.emf.ecp.view.spi.swt.layout.GridCell,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control renderControl(GridCell cell, Composite parent) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		if (cell.getRenderer().equals(this)) {
			final Label l = new Label(parent, SWT.NONE);
			l.setBackground(parent.getBackground());
			String text = getVElement().getName();
			if (text == null)
			{
				text = ""; //$NON-NLS-1$
			}
			switch (getVElement().getLabelAlignment()) {
			case LABEL_ALIGNED:
				if (cell.getColumn() == 0) {
					l.setText(text);
					l.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_view_group_title"); //$NON-NLS-1$
				}
				break;
			case INPUT_ALIGNED:
				if (cell.getColumn() + 1 == currentGridDescription.getColumns()) {
					l.setText(text);
					l.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_view_group_title"); //$NON-NLS-1$
				}
				break;
			default:
				break;
			}
			return l;
		}
		return cell.getRenderer().render(cell, parent);
	}
}
