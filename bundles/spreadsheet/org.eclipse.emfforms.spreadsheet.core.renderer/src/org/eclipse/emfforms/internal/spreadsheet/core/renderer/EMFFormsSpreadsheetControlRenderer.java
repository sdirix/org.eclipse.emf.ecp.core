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
package org.eclipse.emfforms.internal.spreadsheet.core.renderer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter.WriteableOutputStream;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.emfforms.spi.core.services.label.NoLabelFoundException;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsAbstractSpreadsheetRenderer;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetRenderTarget;
import org.eclipse.emfforms.spi.spreadsheet.core.EMFFormsSpreadsheetReport;

/**
 * Spreadsheet renderer for {@link VControl}.
 *
 * @author Eugen Neufeld
 */
public class EMFFormsSpreadsheetControlRenderer extends EMFFormsAbstractSpreadsheetRenderer<VControl> {

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
	public EMFFormsSpreadsheetControlRenderer(EMFFormsDatabinding emfformsDatabinding,
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
	public int render(Workbook workbook, VControl vElement,
		ViewModelContext viewModelContext, EMFFormsSpreadsheetRenderTarget renderTarget) {
		Sheet sheet = workbook.getSheet(renderTarget.getSheetName());
		if (sheet == null) {
			sheet = workbook.createSheet(renderTarget.getSheetName());
		}
		Row labelRow = sheet.getRow(0);
		if (labelRow == null) {
			labelRow = sheet.createRow(0);
		}
		Row valueRow = sheet.getRow(renderTarget.getRow());
		if (valueRow == null) {
			valueRow = sheet.createRow(renderTarget.getRow());
		}
		final Cell labelCell = labelRow.getCell(renderTarget.getColumn(),
			Row.CREATE_NULL_AS_BLANK);

		final Cell valueCell = valueRow.getCell(renderTarget.getColumn(),
			Row.CREATE_NULL_AS_BLANK);

		try {
			final IObservableValue displayName = emfformsLabelProvider
				.getDisplayName(vElement.getDomainModelReference());
			labelCell.setCellValue(displayName.getValue().toString());
			displayName.dispose();
			final IObservableValue observableValue = emfformsDatabinding
				.getObservableValue(vElement.getDomainModelReference(),
					viewModelContext.getDomainModel());
			valueCell.setCellValue(getValueString(observableValue.getValue(), observableValue.getValueType()));
			observableValue.dispose();

			final Comment comment = createComment(workbook, sheet, vElement.getDomainModelReference(),
				renderTarget.getRow(), renderTarget.getColumn());
			labelCell.setCellComment(comment);

			return 1;
		} catch (final DatabindingFailedException ex) {
			reportService.report(new EMFFormsSpreadsheetReport(ex, EMFFormsSpreadsheetReport.ERROR));
		} catch (final NoLabelFoundException ex) {
			reportService.report(new EMFFormsSpreadsheetReport(ex, EMFFormsSpreadsheetReport.ERROR));
		} catch (final IOException ex) {
			reportService.report(new EMFFormsSpreadsheetReport(ex, EMFFormsSpreadsheetReport.ERROR));
		}

		return 0;
	}

	private String getValueString(Object fromObject, Object valueType) {
		if (EAttribute.class.isInstance(valueType)) {
			final EAttribute eAttribute = (EAttribute) valueType;
			final EDataType eDataType = eAttribute.getEAttributeType();
			final EFactory eFactory = eDataType.getEPackage().getEFactoryInstance();

			if (eAttribute.isMany())
			{
				final StringBuilder result = new StringBuilder();
				for (final Object value : (List<?>) fromObject)
				{
					if (result.length() == 0)
					{
						result.append(' ');
					}
					result.append(eFactory.convertToString(eDataType, value));
				}
				return result.toString();
			}
			return eFactory.convertToString(eDataType, fromObject);
		}
		if (fromObject == null) {
			return ""; //$NON-NLS-1$
		}
		return fromObject.toString();
	}

	private Comment createComment(Workbook workbook, Sheet sheet, VDomainModelReference domainModelReference, int row,
		int column) throws IOException {
		final CreationHelper factory = workbook.getCreationHelper();

		// When the comment box is visible, have it show in a 1x3 space
		final ClientAnchor anchor = factory.createClientAnchor();
		anchor.setCol1(column);
		anchor.setCol2(column + 1);
		anchor.setRow1(row);
		anchor.setRow2(row + 1);

		final Drawing drawing = sheet.createDrawingPatriarch();
		final Comment comment = drawing.createCellComment(anchor);

		comment.setAuthor("EMFForms Spreadsheet Renderer"); //$NON-NLS-1$
		comment.setVisible(false);
		comment.setString(factory.createRichTextString(getSerializedDMR(domainModelReference)));
		return comment;
	}

	private String getSerializedDMR(VDomainModelReference domainModelReference) throws IOException {
		final ResourceSet rs = new ResourceSetImpl();
		final Resource resource = rs.createResource(URI.createURI("VIRTAUAL_URI")); //$NON-NLS-1$
		resource.getContents().add(EcoreUtil.copy(domainModelReference));

		final StringWriter sw = new StringWriter();
		final WriteableOutputStream os = new WriteableOutputStream(sw, "UTF-8"); //$NON-NLS-1$

		resource.save(os, null);
		final String value = sw.getBuffer().toString();
		return value;
	}

}
