/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.spreadsheet.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskPackage;
import org.eclipse.emf.ecp.test.common.DefaultRealm;
import org.eclipse.emfforms.internal.spreadsheet.core.transfer.EMFFormsSpreadsheetImporterImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EMFFormsSpreadsheetImportImpl_ITest {

	private DefaultRealm realm;

	@Before
	public void setup() {
		realm = new DefaultRealm();
	}

	@After
	public void tearDown() {
		realm.dispose();
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emfforms.internal.spreadsheet.core.transfer.EMFFormsSpreadsheetImporterImpl#importSpreadsheet(Workbook, org.eclipse.emf.ecore.EClass)}
	 * .
	 *
	 * @throws IOException
	 */
	@Test
	public void testImportSpreadsheet() throws IOException {
		final EMFFormsSpreadsheetImporterImpl spreadsheetImport = new EMFFormsSpreadsheetImporterImpl();
		final File targetFile = new File("export.xls"); //$NON-NLS-1$
		final FileInputStream file = new FileInputStream(targetFile);
		final Workbook workbook = new HSSFWorkbook(file);

		final Collection<EObject> users = spreadsheetImport.importSpreadsheet(workbook,
			TaskPackage.eINSTANCE.getUser());
		for (final EObject eObject : users) {
			System.out.println(eObject);
		}
	}

}
