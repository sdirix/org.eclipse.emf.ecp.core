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
import org.eclipse.emfforms.common.Property;
import org.eclipse.emfforms.common.Property.ChangeListener;
import org.eclipse.emfforms.spi.swt.table.AbstractTableViewerColumnBuilder;
import org.eclipse.emfforms.spi.swt.table.ColumnConfiguration;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.widgets.Item;

/**
 * Nebula Grid viewer configuration helper class.
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
	protected void configure(GridTableViewer tableViewer, GridViewerColumn viewerColumn) {
		super.configure(tableViewer, viewerColumn);

		// Nebula Grid supports a few more things
		configureHideShow(tableViewer, viewerColumn);

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
		column.setVisible(getConfig().visible().getValue());
		// column.getColumn().setWidth(width);
	}

	@Override
	protected void configureEditingSupport(GridViewerColumn viewerColumn, GridTableViewer tableViewer) {
		final Optional<EditingSupport> editingSupport = getConfig().createEditingSupport(tableViewer);
		if (editingSupport.isPresent()) {
			viewerColumn.setEditingSupport(editingSupport.get());
		}
	}

	/**
	 * Configure hide/show columns toggle.
	 *
	 * @param tableViewer the table viewer
	 * @param viewerColumn the viewer column to configure
	 */
	protected void configureHideShow(final GridTableViewer tableViewer, final GridViewerColumn viewerColumn) {

		getConfig().visible().addChangeListener(new ChangeListener<Boolean>() {
			@Override
			public void valueChanged(Property<Boolean> property, Boolean oldValue, Boolean newValue) {
				viewerColumn.getColumn().setVisible(newValue);
			}
		});

	}

}
