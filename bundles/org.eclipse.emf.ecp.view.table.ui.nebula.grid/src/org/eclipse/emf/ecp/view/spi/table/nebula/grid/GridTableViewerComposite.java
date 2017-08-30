/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.table.nebula.grid;

import java.util.List;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emfforms.spi.swt.table.AbstractTableViewerComposite;
import org.eclipse.emfforms.spi.swt.table.TableControl;
import org.eclipse.emfforms.spi.swt.table.TableViewerComparator;
import org.eclipse.emfforms.spi.swt.table.TableViewerSWTCustomization;
import org.eclipse.emfforms.spi.swt.table.TableViewerSWTCustomization.ColumnConfiguration;
import org.eclipse.jface.layout.AbstractColumnLayout;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridColumnLayout;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

/**
 * A {@link Composite} containing a {@link GridTableViewer}.
 *
 * @author Jonas Helming
 *
 */
public class GridTableViewerComposite extends AbstractTableViewerComposite<GridTableViewer> {

	private GridTableViewer gridTableViewer;

	/**
	 * Default constructor.
	 *
	 * @param parent the parent {@link Composite}
	 * @param style the style bits
	 * @param inputObject the input object
	 * @param customization the {@link TableViewerSWTCustomization}
	 * @param title the title
	 * @param tooltip the tooltip
	 */
	public GridTableViewerComposite(Composite parent, int style, Object inputObject,
		TableViewerSWTCustomization customization,
		IObservableValue title, IObservableValue tooltip) {
		super(parent, style, inputObject, customization, title, tooltip);
	}

	@Override
	public GridTableViewer getTableViewer() {
		return gridTableViewer;
	}

	@Override
	protected GridTableViewer createTableViewer(TableViewerSWTCustomization<GridTableViewer> customization,
		Composite viewerComposite) {
		gridTableViewer = customization.createTableViewer(viewerComposite);
		return gridTableViewer;
	}

	@Override
	protected AbstractColumnLayout createLayout(Composite viewerComposite) {
		final GridColumnLayout layout = new GridColumnLayout();
		viewerComposite.setLayout(layout);
		return layout;
	}

	@Override
	public Widget[] getColumns() {
		return gridTableViewer.getGrid().getColumns();
	}

	@Override
	public void addColumnListener(ControlListener columnlistener) {
		for (int i = 0; i < gridTableViewer.getGrid().getColumns().length; i++) {
			final GridColumn gridColumn = gridTableViewer.getGrid().getColumns()[i];
			gridColumn.addControlListener(columnlistener);
		}

	}

	@Override
	public TableControl getTableControl() {
		return new TableControl() {

			@Override
			public boolean isDisposed() {
				return getTableViewer().getGrid().isDisposed();
			}

			@Override
			public int getItemHeight() {
				return getTableViewer().getGrid().getItemHeight();
			}

			@Override
			public boolean getHeaderVisible() {
				return getTableViewer().getGrid().getHeaderVisible();
			}

			@Override
			public int getHeaderHeight() {
				return getTableViewer().getGrid().getHeaderHeight();
			}

			@Override
			public int getItemCount() {
				return getTableViewer().getGrid().getItemCount();
			}
		};
	}

	@Override
	protected ViewerColumn createColumn(ColumnConfiguration config,
		EMFDataBindingContext emfDataBindingContext, GridTableViewer tableViewer) {

		return new GridViewerColumnBuilder(config)
			.withDatabinding(emfDataBindingContext)
			.build(tableViewer);
	}

	@Override
	public void setComparator(final TableViewerComparator comparator, List<Integer> sortableColumns) {
		for (int i = 0; i < getTableViewer().getGrid().getColumns().length; i++) {
			if (!sortableColumns.contains(i)) {
				continue;
			}
			final int j = i;
			final GridColumn tableColumn = getTableViewer().getGrid().getColumns()[i];
			final SelectionAdapter selectionAdapter = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					comparator.setColumn(j);
					tableColumn.setSort(comparator.getDirection());
					gridTableViewer.refresh();
				}
			};
			tableColumn.addSelectionListener(selectionAdapter);
		}

	}

}
