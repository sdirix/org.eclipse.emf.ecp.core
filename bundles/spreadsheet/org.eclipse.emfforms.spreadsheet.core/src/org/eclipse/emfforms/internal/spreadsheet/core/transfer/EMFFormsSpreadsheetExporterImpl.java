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
package org.eclipse.emfforms.internal.spreadsheet.core.transfer;

import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emfforms.internal.spreadsheet.core.EMFFormsSpreadsheetViewModelContext;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsAbstractSpreadsheetRenderer;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsNoRendererException;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetRenderTarget;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetRendererFactory;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetReport;
import org.eclipse.emfforms.spi.spreadsheet.core.transfer.EMFFormsSpreadsheetExporter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Implementation of the {@link EMFFormsSpreadsheetExporter}.
 *
 * @author Eugen Neufeld
 */
public class EMFFormsSpreadsheetExporterImpl implements EMFFormsSpreadsheetExporter {

	private final ReportService reportService;

	/**
	 * Default Constructor.
	 */
	public EMFFormsSpreadsheetExporterImpl() {
		final BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
		final ServiceReference<ReportService> reportServiceReference = bundleContext
			.getServiceReference(ReportService.class);
		reportService = bundleContext.getService(reportServiceReference);
	}

	@Override
	public Workbook render(final Collection<? extends EObject> domainObjects, VView viewModel) {

		final BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
		final ServiceReference<EMFFormsSpreadsheetRendererFactory> serviceReference = bundleContext
			.getServiceReference(EMFFormsSpreadsheetRendererFactory.class);
		final EMFFormsSpreadsheetRendererFactory emfFormsSpreadsheetRendererFactory = bundleContext
			.getService(serviceReference);
		final Workbook workbook = new HSSFWorkbook();

		if (domainObjects == null) {
			try {
				final ViewModelContext viewModelContext = new EMFFormsSpreadsheetViewModelContext(viewModel, null);
				final EMFFormsAbstractSpreadsheetRenderer<VElement> renderer = emfFormsSpreadsheetRendererFactory
					.getRendererInstance(
						viewModelContext.getViewModel(), viewModelContext);
				renderer.render(workbook, viewModelContext.getViewModel(), viewModelContext,
					new EMFFormsSpreadsheetRenderTarget("root", 0, 0)); //$NON-NLS-1$
			} catch (final EMFFormsNoRendererException ex) {
				reportService.report(new EMFFormsSpreadsheetReport(ex, EMFFormsSpreadsheetReport.ERROR));
			}
		} else {
			int i = 0;
			for (final EObject domainObject : domainObjects) {
				if (!viewModel.getRootEClass().isInstance(domainObject)) {
					reportService
						.report(new EMFFormsSpreadsheetReport(
							String
								.format(
									"The provided view %1$s doesn't fit for the passed EObject %2$s", viewModel, //$NON-NLS-1$
									domainObject),
							EMFFormsSpreadsheetReport.ERROR));
					continue;
				}
				try {
					final ViewModelContext viewModelContext = new EMFFormsSpreadsheetViewModelContext(viewModel,
						domainObject);
					final EMFFormsAbstractSpreadsheetRenderer<VElement> renderer = emfFormsSpreadsheetRendererFactory
						.getRendererInstance(
							viewModelContext.getViewModel(), viewModelContext);
					renderer.render(workbook, viewModelContext.getViewModel(), viewModelContext,
						new EMFFormsSpreadsheetRenderTarget("root", //$NON-NLS-1$
							i++, 0));
				} catch (final EMFFormsNoRendererException ex) {
					reportService.report(new EMFFormsSpreadsheetReport(ex, EMFFormsSpreadsheetReport.ERROR));
				}
			}
		}
		return workbook;
	}
}
