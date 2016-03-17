/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.table.swt;

import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VReadOnlyColumnConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VTableColumnConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableFactory;
import org.eclipse.emf.ecp.view.spi.table.model.VWidthConfiguration;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Helper class for retrieving configuration information.
 *
 * @author Eugen Neufeld
 *
 */
public final class TableConfigurationHelper {

	private TableConfigurationHelper() {

	}

	/**
	 * Retrieve the readonly state for the table control of a {@link VDomainModelReference}.
	 *
	 * @param tableControl the {@link VTableControl} to get the readonly state of a column for
	 * @param domainModelReference the {@link VDomainModelReference} to get the state for
	 * @return true if the domainModelReference is readonly, false otherwise
	 */
	public static boolean isReadOnly(VTableControl tableControl, VDomainModelReference domainModelReference) {
		final boolean readOnly = false;
		for (final VTableColumnConfiguration columnConfiguration : tableControl.getColumnConfigurations()) {
			if (VReadOnlyColumnConfiguration.class.isInstance(columnConfiguration)) {
				final VReadOnlyColumnConfiguration configuration = VReadOnlyColumnConfiguration.class
					.cast(columnConfiguration);
				return configuration.getColumnDomainReferences().contains(domainModelReference);
			}
		}
		return readOnly;
	}

	/**
	 * Retrieve any column width information available for the given column.
	 *
	 * @param tableControl the {@link VTableControl} containing the column configuration
	 * @param domainModelReference the column {@link VDomainModelReference}
	 * @return the width information, if present
	 */
	public static Optional<Integer> getColumnWidth(VTableControl tableControl,
		VDomainModelReference domainModelReference) {
		for (final VTableColumnConfiguration configuration : tableControl.getColumnConfigurations()) {
			if (!VWidthConfiguration.class.isInstance(configuration)) {
				continue;
			}
			final VWidthConfiguration widthConfiguration = VWidthConfiguration.class.cast(configuration);
			if (widthConfiguration.getColumnDomainReference() != domainModelReference) {
				continue;
			}
			return Optional.ofNullable(widthConfiguration.getWeight());
		}
		return Optional.empty();
	}

	/**
	 * Updates the {@link VWidthConfiguration} for a column. If there is no configuration one will be created.
	 *
	 * @param tableControl the {@link VTableControl} containing the column configuration
	 * @param domainModelReference the column {@link VDomainModelReference}
	 * @param swtTable the swt table
	 * @param tableColumn the table column
	 */
	public static void updateWidthConfiguration(VTableControl tableControl, VDomainModelReference domainModelReference,
		Table swtTable, TableColumn tableColumn) {
		VWidthConfiguration widthConfiguration = null;
		for (final VTableColumnConfiguration configuration : tableControl.getColumnConfigurations()) {
			if (!VWidthConfiguration.class.isInstance(configuration)) {
				continue;
			}
			final VWidthConfiguration candidate = VWidthConfiguration.class.cast(configuration);
			if (candidate.getColumnDomainReference() == domainModelReference) {
				widthConfiguration = candidate;
				break;
			}
		}
		if (widthConfiguration == null) {
			widthConfiguration = VTableFactory.eINSTANCE.createWidthConfiguration();
			widthConfiguration.setColumnDomainReference(domainModelReference);
			tableControl.getColumnConfigurations().add(widthConfiguration);
		}

		// TODO JF the column widths should normalized to a commin scale which is alliged with the default weight of the
		// cell editors. however [0,100] in int values brings too much rounding errors

		// final double columnCount = swtTable.getColumnCount();
		// final double tableWidth = swtTable.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		final double columnWidth = tableColumn.getWidth();

		// final double widthPerColumn = tableWidth / columnCount;
		// final double normalizedWidthPerColumn = 100d / widthPerColumn;

		// widthConfiguration.setWeight(new Double(normalizedWidthPerColumn * columnWidth).intValue());
		widthConfiguration.setWeight(new Double(columnWidth).intValue());
	}
}
