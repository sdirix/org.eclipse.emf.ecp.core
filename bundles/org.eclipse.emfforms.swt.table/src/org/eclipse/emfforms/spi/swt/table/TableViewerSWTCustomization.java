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

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.graphics.Image;

/**
 * The TableViewerSWTCustomization is used by the {@link TableViewerComposite} to create the UI with its behaviour.
 *
 * @author Alexandra Buzila
 * @author Johannes Faltermeier
 *
 */
public interface TableViewerSWTCustomization
	extends TableViewerCompositeBuilder, TableViewerCreator, ButtonBarBuilder, DNDProvider {

	/**
	 * @return the {@link ViewerComparator} which will be set on the viewr, if present
	 */
	Optional<ViewerComparator> getComparator();

	/**
	 *
	 * @return the {@link IContentProvider} which will be set on the viewer
	 */
	IContentProvider createContentProvider();

	/**
	 *
	 * @return the {@link ColumnDescription ColumnDescriptions}
	 */
	List<ColumnDescription> getColumns();

	/**
	 * A ColumnDescription is used to describe how a viewer column shall be created.
	 *
	 * @author Johannes Faltermeier
	 *
	 */
	interface ColumnDescription {

		/**
		 *
		 * @return whether the column is resizeable
		 */
		boolean isResizeable();

		/**
		 *
		 * @return whether the column is moveable
		 */
		boolean isMoveable();

		/**
		 *
		 * @return the SWT style bits for the column
		 */
		int getStyleBits();

		/**
		 *
		 * @return the weight of the column
		 */
		int getWeight();

		/**
		 *
		 * @return the mind width of the column in pixels
		 */
		int getMinWidth();

		/**
		 *
		 * @return the column header text
		 */
		IObservableValue getColumnText();

		/**
		 *
		 * @return the column header tooltip
		 */
		IObservableValue getColumnTooltip();

		/**
		 *
		 * @return the label provider
		 */
		CellLabelProvider createLabelProvider();

		/**
		 * @param columnViewer the {@link TableViewer}
		 * @return the editing support for the column, if present
		 */
		Optional<EditingSupport> createEditingSupport(TableViewer columnViewer);

		/**
		 *
		 * @return the column image, if present
		 */
		Optional<Image> getColumnImage();

	}

}
