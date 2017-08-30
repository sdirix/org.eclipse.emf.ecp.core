/*******************************************************************************
 * Copyright (c) 2011-2016 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.table.nebula.grid;

import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.swt.table.AbstractTableViewerColumnBuilder;
import org.eclipse.emfforms.spi.swt.table.TableViewerSWTCustomization.ColumnConfiguration;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.widgets.Item;

/**
 * The nebula grid viewer configuration helper class.
 *
 * @author Mat Hansen <mhansen@eclipsesource.com>
 *
 */
public class GridViewerColumnBuilder extends AbstractTableViewerColumnBuilder<GridTableViewer, GridViewerColumn> {

	/**
	 * The constructor.
	 *
	 * @param config the {@link ColumnConfiguration}
	 */
	public GridViewerColumnBuilder(ColumnConfiguration config) {
		super(config);
	}

	@Override
	public GridViewerColumn createViewerColumn(GridTableViewer tableViewer) {
		return new GridViewerColumn(tableViewer, getConfig().getStyleBits());
	}

	@Override
	protected Item getTableColumn(GridViewerColumn viewerColumn) {
		return viewerColumn.getColumn();
	}

	@Override
	protected void configureViewerColumn(GridViewerColumn viewerColumn) {
		final GridColumn column = viewerColumn.getColumn();

		column.setResizeable(getConfig().isResizeable());
		column.setMoveable(getConfig().isMoveable());
		// column.getColumn().setWidth(width);
	}

	@Override
	protected void configureEditingSupport(GridViewerColumn viewerColumn, GridTableViewer tableViewer) {
		final Optional<EditingSupport> editingSupport = getConfig().createEditingSupport(tableViewer);
		if (editingSupport.isPresent()) {
			viewerColumn.setEditingSupport(editingSupport.get());
		}
	}

}
