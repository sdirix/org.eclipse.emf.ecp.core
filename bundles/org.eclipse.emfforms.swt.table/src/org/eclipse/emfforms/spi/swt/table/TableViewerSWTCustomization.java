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
package org.eclipse.emfforms.spi.swt.table;

import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.jface.viewers.AbstractTableViewer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.graphics.Image;

/**
 * The TableViewerSWTCustomization is used by the {@link TableViewerComposite} to create the UI with its behaviour.
 *
 * @author Alexandra Buzila
 * @author Johannes Faltermeier
 *
 * @param <T> the TableViewer implementation to use
 */
public interface TableViewerSWTCustomization<T extends AbstractTableViewer>
	extends TableViewerCompositeBuilder, TableViewerCreator<T>, ButtonBarBuilder,
	DNDProvider {

	/**
	 * Returns the comparator to use.
	 *
	 * @return the {@link ViewerComparator} which will be set on the viewer, if present
	 */
	Optional<ViewerComparator> getComparator();

	/**
	 * Returns the content provider to use.
	 *
	 * @return the {@link IContentProvider} which will be set on the viewer
	 */
	IContentProvider createContentProvider();

	/**
	 * Returns the column descriptions which will be used to create actual columns.
	 *
	 * @return the {@link ColumnConfiguration ColumnDescriptions}
	 */
	List<ColumnConfiguration> getColumns();

	/**
	 * A TableConfiguration is used to describe how a table viewer should be configured.
	 *
	 * Currently all configured TableConfiguration keys are passed down to each column as well.
	 * This is subject to change.
	 *
	 * @author Mat Hansen <mhansen@eclipsesource.com>
	 *
	 */
	interface TableConfiguration {

		/** Data key for a domain model reference. */
		String DMR = "domain_model_reference"; //$NON-NLS-1$

		/**
		 * Dummy method to satisfy CheckStyle.
		 * TODO: Delete after this interface defines some real methods.
		 */
		void dummy();

	}

	/**
	 * A ColumnConfiguration is used to describe how a viewer column shall be created.
	 *
	 * @author Johannes Faltermeier
	 *
	 */
	interface ColumnConfiguration {

		/** Data key for resizable columns. */
		String RESIZABLE = "resizable"; //$NON-NLS-1$

		/** Data key for column weight. */
		String WEIGHT = "weight"; //$NON-NLS-1$

		/** Constant indicating that {@link #getWeight()} has no value. */
		int NO_WEIGHT = -1;

		/** Data key for the minimum width of the column. */
		String MIN_WIDTH = "min_width"; //$NON-NLS-1$

		/** Data key for column id. */
		String COLUMN_ID = "column_id"; //$NON-NLS-1$

		/**
		 * <code>true</code> if resizeable, <code>false</code> otherwise.
		 *
		 * @return whether the column is resizeable
		 */
		boolean isResizeable();

		/**
		 * <code>true</code> if moveable, <code>false</code> otherwise.
		 *
		 * @return whether the column is moveable
		 */
		boolean isMoveable();

		/**
		 * The SWT style bits which will be used to create the column.
		 *
		 * @return the SWT style bits for the column
		 */
		int getStyleBits();

		/**
		 * The weight of the column.
		 *
		 * @return the weight of the column
		 */
		int getWeight();

		/**
		 * The minimal width of the column.
		 *
		 * @return the min width of the column in pixels
		 */
		int getMinWidth();

		/**
		 * The header text for the column.
		 *
		 * @return the column header text
		 */
		IObservableValue getColumnText();

		/**
		 * The column header tooltip text.
		 *
		 * @return the column header tooltip
		 */
		IObservableValue getColumnTooltip();

		/**
		 * The cell label provider which will be set on the column.
		 *
		 * @param columnViewer the column viewer
		 * @return the label provider
		 */
		CellLabelProvider createLabelProvider(AbstractTableViewer columnViewer);

		/**
		 * Called to setup the {@link EditingSupport} for the viewer.
		 *
		 * @param columnViewer the {@link AbstractTableViewer}
		 * @return the editing support for the column, if present
		 */
		Optional<EditingSupport> createEditingSupport(AbstractTableViewer columnViewer);

		/**
		 * The image of the column.
		 *
		 * @return the column image, if present
		 */
		Optional<Image> getColumnImage();

		/**
		 * Get an arbitrary element from the data map.
		 *
		 * @param key (see constants)
		 * @return object
		 */
		Object getData(String key);

		/**
		 * Add the contents of the given map to the data map.
		 *
		 * @param data object
		 */
		void setData(Map<String, Object> data);

		/**
		 * Get the underlying data map.
		 *
		 * @return data map object
		 */
		Map<String, Object> getData();

		/**
		 * Get the list of additional ConfigurationCallbacks.
		 *
		 * @return list of ConfigurationCallbacks.
		 */
		List<ConfigurationCallback<AbstractTableViewer, ViewerColumn>> getConfigurationCallbacks();

	}

	/**
	 * Interface for ConfigurationCallbacks.
	 *
	 * @author Mat Hansen <mhansen@eclipsesource.com>
	 *
	 * @param <V> the TableViewer implementation to use
	 * @param <C> the {@link ViewerColumn} implementation to use
	 */
	interface ConfigurationCallback<V extends AbstractTableViewer, C extends ViewerColumn> {

		/**
		 * Setup a custom configuration for the given viewer.
		 *
		 * @param config the {@link ColumnConfiguration}
		 * @param tableViewer the table viewer
		 * @param viewerColumn the viewer column
		 */
		void configure(ColumnConfiguration config, V tableViewer, C viewerColumn);

	}

}
