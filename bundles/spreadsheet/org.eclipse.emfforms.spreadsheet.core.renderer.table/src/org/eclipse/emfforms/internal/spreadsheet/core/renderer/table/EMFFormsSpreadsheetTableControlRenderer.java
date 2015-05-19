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

import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.indexdmr.model.VIndexDomainModelReference;
import org.eclipse.emf.ecp.view.spi.indexdmr.model.VIndexdmrFactory;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emfforms.internal.spreadsheet.core.renderer.EMFFormsSpreadsheetControlRenderer;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.emfforms.spi.core.services.label.NoLabelFoundException;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsAbstractSpreadsheetRenderer;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetRenderTarget;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetReport;

/**
 * Spreadsheet renderer for {@link VTableControl}.
 *
 * @author Eugen Neufeld
 */
@SuppressWarnings("restriction")
public class EMFFormsSpreadsheetTableControlRenderer extends EMFFormsAbstractSpreadsheetRenderer<VTableControl> {

	private final EMFFormsDatabinding emfformsDatabinding;
	private final EMFFormsLabelProvider emfformsLabelProvider;
	private final ReportService reportService;

	/**
	 * Default constructor.
	 *
	 * @param emfformsDatabinding The EMFFormsDatabinding to use
	 * @param emfformsLabelProvider The EMFFormsLabelProvider to use
	 * @param reportService The {@link ReportService}
	 */
	public EMFFormsSpreadsheetTableControlRenderer(EMFFormsDatabinding emfformsDatabinding,
		EMFFormsLabelProvider emfformsLabelProvider, ReportService reportService) {
		this.emfformsDatabinding = emfformsDatabinding;
		this.emfformsLabelProvider = emfformsLabelProvider;
		this.reportService = reportService;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsAbstractSpreadsheetRenderer#render(org.apache.poi.ss.usermodel.Workbook,
	 *      org.eclipse.emf.ecp.view.spi.model.VElement, org.eclipse.emf.ecp.view.spi.context.ViewModelContext,
	 *      org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetRenderTarget)
	 */

	@Override
	public int render(Workbook workbook, VTableControl vElement, final ViewModelContext viewModelContext,
		EMFFormsSpreadsheetRenderTarget eMFFormsSpreadsheetRenderTarget) {
		final EMFFormsSpreadsheetControlRenderer controlRenderer = new EMFFormsSpreadsheetControlRenderer(
			emfformsDatabinding,
			new EMFFormsLabelProvider() {

				@Override
				public IObservableValue getDisplayName(VDomainModelReference domainModelReference, EObject rootObject)
					throws NoLabelFoundException {
					return emfformsLabelProvider.getDisplayName(domainModelReference, rootObject);
				}

				@Override
				public IObservableValue getDisplayName(VDomainModelReference domainModelReference)
					throws NoLabelFoundException {
					if (VIndexDomainModelReference.class.isInstance(domainModelReference)
						&& VIndexDomainModelReference.class.cast(domainModelReference).getPrefixDMR() != null) {
						final VIndexDomainModelReference indexDMR = VIndexDomainModelReference.class
							.cast(domainModelReference);
						String prefixName = (String) emfformsLabelProvider.getDisplayName(indexDMR.getPrefixDMR())
							.getValue();
						if (prefixName == null || prefixName.length() == 0) {
							try {
								prefixName = EStructuralFeature.class.cast(
									emfformsDatabinding.getValueProperty(indexDMR.getPrefixDMR(),
										viewModelContext.getDomainModel()).getValueType()).getName();
							} catch (final DatabindingFailedException ex) {
								reportService
									.report(new EMFFormsSpreadsheetReport(ex, EMFFormsSpreadsheetReport.ERROR));
							}
						}
						return Observables.constantObservableValue(prefixName + "_" + indexDMR.getIndex() //$NON-NLS-1$
							+ "_" + emfformsLabelProvider.getDisplayName(indexDMR.getTargetDMR()).getValue()); //$NON-NLS-1$
					}
					return emfformsLabelProvider.getDisplayName(domainModelReference);
				}

				@Override
				public IObservableValue getDescription(VDomainModelReference domainModelReference, EObject rootObject)
					throws NoLabelFoundException {
					return emfformsLabelProvider.getDescription(domainModelReference, rootObject);
				}

				@Override
				public IObservableValue getDescription(VDomainModelReference domainModelReference)
					throws NoLabelFoundException {
					return emfformsLabelProvider.getDescription(domainModelReference);
				}
			}, reportService);
		int numColumns = 0;
		try {
			final IObservableList observableList = emfformsDatabinding.getObservableList(
				vElement.getDomainModelReference(), viewModelContext.getDomainModel());

			final VTableDomainModelReference tableDomainModelReference = (VTableDomainModelReference) vElement
				.getDomainModelReference();
			for (int i = 0; i < Math.max(observableList.size(), 3); i++) {
				for (final VDomainModelReference domainModelReference : tableDomainModelReference
					.getColumnDomainModelReferences()) {
					final VIndexDomainModelReference indexDMR = VIndexdmrFactory.eINSTANCE
						.createIndexDomainModelReference();
					indexDMR.setPrefixDMR(EcoreUtil.copy(tableDomainModelReference.getDomainModelReference()));
					indexDMR.setIndex(i);
					indexDMR.setTargetDMR(EcoreUtil.copy(domainModelReference));

					final VControl vControl = VViewFactory.eINSTANCE.createControl();
					vControl.setDomainModelReference(indexDMR);

					numColumns += controlRenderer.render(workbook, vControl, viewModelContext,
						new EMFFormsSpreadsheetRenderTarget(
							eMFFormsSpreadsheetRenderTarget.getSheetName(), eMFFormsSpreadsheetRenderTarget.getRow(),
							eMFFormsSpreadsheetRenderTarget.getColumn()
								+ numColumns));
				}
			}
			if (vElement.getDetailEditing() != DetailEditing.NONE) {
				reportService.report(new EMFFormsSpreadsheetReport(
					"The fields defined in the detail were not rendered!", EMFFormsSpreadsheetReport.WARNING)); //$NON-NLS-1$
			}

		} catch (final DatabindingFailedException ex) {
			reportService.report(new EMFFormsSpreadsheetReport(ex, EMFFormsSpreadsheetReport.ERROR));
		}
		return numColumns;
	}
}
