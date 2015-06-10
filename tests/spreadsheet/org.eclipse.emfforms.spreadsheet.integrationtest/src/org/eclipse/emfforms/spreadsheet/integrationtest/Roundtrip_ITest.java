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
package org.eclipse.emfforms.spreadsheet.integrationtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.makeithappen.model.task.Nationality;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskFactory;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskPackage;
import org.eclipse.emf.ecp.makeithappen.model.task.User;
import org.eclipse.emf.ecp.test.common.DefaultRealm;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.emfforms.spi.spreadsheet.core.transfer.EMFFormsSpreadsheetExporter;
import org.eclipse.emfforms.spi.spreadsheet.core.transfer.EMFFormsSpreadsheetImporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Roundtrip_ITest {
	private DefaultRealm realm;

	@Before
	public void setup() {
		realm = new DefaultRealm();
	}

	@After
	public void tearDown() {
		realm.dispose();
	}

	@Test
	public void test() throws DatatypeConfigurationException, IOException {
		// write data
		EMFFormsSpreadsheetExporter viewRenderer = EMFFormsSpreadsheetExporter.INSTANCE;
		File targetFile = new File("export.xls");
		EObject user = getDomainModel();
		Map<String, Object> context=new LinkedHashMap<String, Object>();
		context.put("root", true);
		context.put("detail", true);
		Workbook wb=viewRenderer.render(Collections.singleton(user),ViewProviderHelper.getView(user, context));

		saveWorkbook(wb,targetFile.getAbsolutePath());

		// read data
		final FileInputStream file = new FileInputStream(targetFile);
		final Workbook workbook = new HSSFWorkbook(file);
		
		final EMFFormsSpreadsheetImporter spreadsheetImport = EMFFormsSpreadsheetImporter.INSTANCE;
		final Collection<EObject> users = spreadsheetImport.importSpreadsheet(workbook, TaskPackage.eINSTANCE.getUser());
		for (final EObject eObject : users) {
			EcoreUtil.equals(eObject, user);
		}

	}

	private void saveWorkbook(Workbook wb, String absolutePath) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(absolutePath);
			wb.write(fileOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private EObject getDomainModel() {
		User user=TaskFactory.eINSTANCE.createUser();
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.doe@mail.com");
		user.setWeight(1.1);
		user.setHeigth(1);
		user.setNationality(Nationality.ITALIAN);
		return user;
	}

}
