/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.spreadsheet.core.renderer.table;

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsAbstractSpreadsheetRenderer;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetRendererService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link EMFFormsSpreadsheetRendererService} for {@link VTableControl}.
 *
 * @author Eugen Neufeld
 */
@Component
public class EMFFormsSpreadsheetTableControlRendererService implements
	EMFFormsSpreadsheetRendererService<VTableControl> {

	private EMFFormsDatabinding emfformsDatabinding;
	private EMFFormsLabelProvider emfformsLabelProvider;
	private ReportService reportService;

	/**
	 * The ReportService to use.
	 *
	 * @param reportService the reportService to set
	 */
	@Reference
	protected void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * Set the EMFFormsDatabinding to use.
	 *
	 * @param emfformsDatabinding The EMFFormsDatabinding to use
	 */
	@Reference
	protected void setEmfformsDatabinding(EMFFormsDatabinding emfformsDatabinding) {
		this.emfformsDatabinding = emfformsDatabinding;
	}

	/**
	 * Set the EMFFormsLabelProvider to use.
	 *
	 * @param emfformsLabelProvider The EMFFormsLabelProvider to use
	 */
	@Reference
	protected void setEmfformsLabelProvider(EMFFormsLabelProvider emfformsLabelProvider) {
		this.emfformsLabelProvider = emfformsLabelProvider;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetRendererService#isApplicable(org.eclipse.emf.ecp.view.spi.model.VElement,
	 *      org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public double isApplicable(VElement vElement,
		ViewModelContext viewModelContext) {
		if (VTableControl.class.isInstance(vElement)) {
			return 2;
		}
		return NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetRendererService#getRendererInstance(org.eclipse.emf.ecp.view.spi.model.VElement,
	 *      org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public EMFFormsAbstractSpreadsheetRenderer<VTableControl> getRendererInstance(
		VTableControl vElement, ViewModelContext viewModelContext) {
		return new EMFFormsSpreadsheetTableControlRenderer(emfformsDatabinding, emfformsLabelProvider, reportService);
	}

}
