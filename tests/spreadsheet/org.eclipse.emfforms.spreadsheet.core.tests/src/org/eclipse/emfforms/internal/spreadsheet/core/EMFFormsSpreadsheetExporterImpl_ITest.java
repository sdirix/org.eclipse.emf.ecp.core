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
package org.eclipse.emfforms.internal.spreadsheet.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xml.type.internal.XMLCalendar;
import org.eclipse.emf.ecp.makeithappen.model.task.Gender;
import org.eclipse.emf.ecp.makeithappen.model.task.Nationality;
import org.eclipse.emf.ecp.makeithappen.model.task.Task;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskFactory;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskPackage;
import org.eclipse.emf.ecp.makeithappen.model.task.User;
import org.eclipse.emf.ecp.test.common.DefaultRealm;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emfforms.internal.spreadsheet.core.transfer.EMFFormsSpreadsheetExporterImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EMFFormsSpreadsheetExporterImpl_ITest {

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
	public void testRenderTemplate() throws DatatypeConfigurationException, IOException {
		final EMFFormsSpreadsheetExporterImpl viewRenderer = new EMFFormsSpreadsheetExporterImpl();
		final Workbook wb = viewRenderer.render(null, getView(), null);
		assertEquals(2, wb.getSheetAt(0).getLastRowNum());

		final FileOutputStream fos = new FileOutputStream("export.xls");
		wb.write(fos);
		fos.close();
	}

	@Test
	public void testRenderMultiple() throws DatatypeConfigurationException {
		final EMFFormsSpreadsheetExporterImpl viewRenderer = new EMFFormsSpreadsheetExporterImpl();
		final User user = getDomainModel();
		final User user2 = getDomainModel();
		final Workbook wb = viewRenderer.render(Arrays.asList(user, user2), getView(), null);
		assertEquals(4, wb.getSheetAt(0).getLastRowNum());
	}

	@Test
	public void testRenderMultipleInvalid() throws DatatypeConfigurationException {
		final EMFFormsSpreadsheetExporterImpl viewRenderer = new EMFFormsSpreadsheetExporterImpl();
		final User user = getDomainModel();
		final Task task = TaskFactory.eINSTANCE.createTask();
		final Workbook wb = viewRenderer.render(Arrays.asList(user, task), getView(), null);
		assertEquals(3, wb.getSheetAt(0).getLastRowNum());
	}

	@Test
	public void testRender() throws DatatypeConfigurationException {
		final EMFFormsSpreadsheetExporterImpl viewRenderer = new EMFFormsSpreadsheetExporterImpl();
		final User user = getDomainModel();
		final Workbook wb = viewRenderer.render(Collections.singleton(user), getView(), null);
		final Sheet sheet = wb.getSheetAt(0);
		final Row row = sheet.getRow(3);
		assertEquals(10, row.getLastCellNum());
		for (int i = 0; i < 10; i++) {
			final Cell cell = row.getCell(i);
			switch (i) {
			case 0:
				assertEquals(user.getFirstName(), cell.getStringCellValue());
				break;
			case 1:
				assertEquals(user.getLastName(), cell.getStringCellValue());
				break;
			case 2:
				assertEquals(user.getGender().toString(), cell.getStringCellValue());
				break;
			case 3:
				assertEquals(Boolean.toString(user.isActive()), cell.getStringCellValue());
				break;
			case 4:
				final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); //$NON-NLS-1$
				assertEquals(df.format(user.getTimeOfRegistration()), cell.getStringCellValue());
				break;
			case 5:
				assertEquals(Double.toString(user.getWeight()), cell.getStringCellValue());
				break;
			case 6:
				assertEquals(Integer.toString(user.getHeigth()), cell.getStringCellValue());
				break;
			case 7:
				assertEquals(user.getNationality().toString(), cell.getStringCellValue());
				break;
			case 8:
				assertEquals(user.getDateOfBirth().toString(), cell.getStringCellValue());
				break;
			case 9:
				assertEquals(user.getEmail(), cell.getStringCellValue());
				break;
			default:
				fail();
			}
		}
	}

	private User getDomainModel() throws DatatypeConfigurationException {
		final User user = TaskFactory.eINSTANCE.createUser();
		user.setEmail("myEMail@test.de"); //$NON-NLS-1$
		user.setFirstName("Bob"); //$NON-NLS-1$
		user.setGender(Gender.MALE);
		user.setHeigth(2);
		user.setLastName("Smith"); //$NON-NLS-1$
		user.setNationality(Nationality.US);
		user.setTimeOfRegistration(new Date());
		user.setWeight(1.45);
		user.setDateOfBirth(new XMLCalendar(new Date(), XMLCalendar.DATE));
		return user;
	}

	private VView getView() {
		final VView view = VViewFactory.eINSTANCE.createView();
		view.setRootEClass(TaskPackage.eINSTANCE.getUser());
		final EList<EStructuralFeature> structuralFeatures = TaskPackage.eINSTANCE.getUser()
			.getEAllStructuralFeatures();
		for (final EStructuralFeature feature : structuralFeatures) {
			if (EReference.class.isInstance(feature)) {
				continue;
			}
			final VControl control = VViewFactory.eINSTANCE.createControl();
			final VFeaturePathDomainModelReference modelReference = VViewFactory.eINSTANCE
				.createFeaturePathDomainModelReference();
			modelReference.setDomainModelEFeature(feature);
			control.setDomainModelReference(modelReference);
			view.getChildren().add(control);
		}
		return view;
	}

}
