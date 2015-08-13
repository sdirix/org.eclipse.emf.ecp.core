/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spreadsheet.integrationtest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskPackage;
import org.eclipse.emf.ecp.test.common.DefaultRealm;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.ErrorReport;
import org.eclipse.emfforms.spi.spreadsheet.core.error.model.SpreadsheetImportResult;
import org.eclipse.emfforms.spi.spreadsheet.core.transfer.EMFFormsSpreadsheetImporter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

// TODO tests are uncomplete. They should be extended after internationalisatio has been added. also not all error cases
// are handled yet
public class ImportErrors_ITest {

	private static Bundle bundle;
	private DefaultRealm realm;
	private InputStream stream;
	private static EClass eClass;

	@BeforeClass
	public static void beforeClass() {
		bundle = Platform.getBundle("org.eclipse.emfforms.spreadsheet.integrationtest"); //$NON-NLS-1$
		eClass = TaskPackage.eINSTANCE.getUser();
	}

	@Before
	public void setup() {
		realm = new DefaultRealm();
	}

	@After
	public void tearDown() throws IOException {
		realm.dispose();
		stream.close();
	}

	@Test
	public void testInvalidIDColumnName() throws IOException {
		/* setup */
		stream = bundle.getEntry("errorSheets/basexls").openStream(); //$NON-NLS-1$
		final Workbook workbook = new HSSFWorkbook(stream);
		final Sheet sheet = workbook.getSheetAt(0);
		sheet.getRow(0).getCell(0).setCellValue("FOO"); //$NON-NLS-1$

		/* act */
		final SpreadsheetImportResult result = EMFFormsSpreadsheetImporter.INSTANCE
			.importSpreadsheet(workbook, eClass);
		final EList<ErrorReport> errorReports = result.getErrorReports();

		/* assert */
		assertEquals(1, errorReports.size());
	}

	@Test
	public void testDeletedEObjectID() throws IOException {
		/* setup */
		stream = bundle.getEntry("errorSheets/basexls").openStream(); //$NON-NLS-1$
		final Workbook workbook = new HSSFWorkbook(stream);
		final Sheet sheet = workbook.getSheetAt(0);
		sheet.getRow(3).getCell(0).setCellValue(""); //$NON-NLS-1$

		/* act */
		final SpreadsheetImportResult result = EMFFormsSpreadsheetImporter.INSTANCE
			.importSpreadsheet(workbook, eClass);
		final EList<ErrorReport> errorReports = result.getErrorReports();

		/* assert */
		assertEquals(1, errorReports.size());
	}

	@Test
	public void testDuplicateEObjectID() throws IOException {
		/* setup */
		stream = bundle.getEntry("errorSheets/basexls").openStream(); //$NON-NLS-1$
		final Workbook workbook = new HSSFWorkbook(stream);
		final Sheet sheet = workbook.getSheetAt(0);
		sheet.getRow(3).getCell(0).setCellValue("_5XI6cEG2EeW04_MCsEmiSg"); //$NON-NLS-1$

		/* act */
		final SpreadsheetImportResult result = EMFFormsSpreadsheetImporter.INSTANCE
			.importSpreadsheet(workbook, eClass);
		final EList<ErrorReport> errorReports = result.getErrorReports();

		/* assert */
		assertEquals(1, errorReports.size());
	}

	@Test
	public void testDeleteDMRCellComment() throws IOException {
		/* setup */
		stream = bundle.getEntry("errorSheets/basexls").openStream(); //$NON-NLS-1$
		final Workbook workbook = new HSSFWorkbook(stream);
		final Sheet sheet = workbook.getSheetAt(0);
		sheet.getRow(0).getCell(1).setCellComment(null);

		/* act */
		final SpreadsheetImportResult result = EMFFormsSpreadsheetImporter.INSTANCE
			.importSpreadsheet(workbook, eClass);
		final EList<ErrorReport> errorReports = result.getErrorReports();

		/* assert */
		assertEquals(2, errorReports.size());
	}

	@Test
	public void testDeleteDMRCellCommentValue() throws IOException {
		/* setup */
		stream = bundle.getEntry("errorSheets/basexls").openStream(); //$NON-NLS-1$
		final Workbook workbook = new HSSFWorkbook(stream);
		final Sheet sheet = workbook.getSheetAt(0);
		sheet.getRow(0).getCell(1).getCellComment().setString(workbook.getCreationHelper().createRichTextString("")); //$NON-NLS-1$

		/* act */
		final SpreadsheetImportResult result = EMFFormsSpreadsheetImporter.INSTANCE
			.importSpreadsheet(workbook, eClass);
		final EList<ErrorReport> errorReports = result.getErrorReports();

		/* assert */
		assertEquals(2, errorReports.size());
	}

	@Test
	public void testBrokenDMR() throws IOException {
		/* setup */
		stream = bundle.getEntry("errorSheets/basexls").openStream(); //$NON-NLS-1$
		final Workbook workbook = new HSSFWorkbook(stream);
		final Sheet sheet = workbook.getSheetAt(0);
		sheet.getRow(0).getCell(1).getCellComment().setString(workbook.getCreationHelper().createRichTextString("foo")); //$NON-NLS-1$

		/* act */
		final SpreadsheetImportResult result = EMFFormsSpreadsheetImporter.INSTANCE
			.importSpreadsheet(workbook, eClass);
		final EList<ErrorReport> errorReports = result.getErrorReports();

		/* assert */
		assertEquals(2, errorReports.size());
	}

	@Test
	public void testUnresolveableDMR() throws IOException {
		/* setup */
		stream = bundle.getEntry("errorSheets/basexls").openStream(); //$NON-NLS-1$
		final Workbook workbook = new HSSFWorkbook(stream);
		final Sheet sheet = workbook.getSheetAt(0);
		final String dmrForTaskName = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //$NON-NLS-1$
			"<org.eclipse.emf.ecp.view.model:FeaturePathDomainModelReference xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" xmlns:org.eclipse.emf.ecp.view.model=\"http://org/eclipse/emf/ecp/view/model\">\n" //$NON-NLS-1$
			+ "<domainModelEFeature xsi:type=\"ecore:EAttribute\" href=\"http://eclipse/org/emf/ecp/makeithappen/model/task#//User/firstName\"/>\n" //$NON-NLS-1$
			+ "<domainModelEReferencePath href=\"http://eclipse/org/emf/ecp/makeithappen/model/task#//Task/assignee\"/>\n" //$NON-NLS-1$
			+ "</org.eclipse.emf.ecp.view.model:FeaturePathDomainModelReference>\n" + ""; //$NON-NLS-1$ //$NON-NLS-2$
		sheet.getRow(0).getCell(1).getCellComment()
			.setString(workbook.getCreationHelper().createRichTextString(dmrForTaskName));

		/* act */
		final SpreadsheetImportResult result = EMFFormsSpreadsheetImporter.INSTANCE
			.importSpreadsheet(workbook, eClass);
		final EList<ErrorReport> errorReports = result.getErrorReports();

		/* assert */
		assertEquals(2, errorReports.size());
	}

}
