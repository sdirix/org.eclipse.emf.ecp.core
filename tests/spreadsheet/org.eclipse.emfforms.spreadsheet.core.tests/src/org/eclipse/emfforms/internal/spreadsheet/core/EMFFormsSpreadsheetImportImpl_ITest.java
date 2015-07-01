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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xml.type.internal.XMLCalendar;
import org.eclipse.emf.ecp.makeithappen.model.task.Nationality;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskFactory;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskPackage;
import org.eclipse.emf.ecp.makeithappen.model.task.User;
import org.eclipse.emf.ecp.test.common.DefaultRealm;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emfforms.spi.spreadsheet.core.transfer.EMFFormsSpreadsheetExporter;
import org.eclipse.emfforms.spi.spreadsheet.core.transfer.EMFFormsSpreadsheetImporter;
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

	@Test
	public void testImportSpreadsheet() throws IOException, DatatypeConfigurationException {
		final EMFFormsSpreadsheetExporter viewRenderer = EMFFormsSpreadsheetExporter.INSTANCE;
		final User user = getDomainModel();
		final User user2 = getDomainModel();

		final Workbook workbook = viewRenderer.render(Arrays.asList(user, user2), getView(), null);

		final EMFFormsSpreadsheetImporter spreadsheetImport = EMFFormsSpreadsheetImporter.INSTANCE;
		final Collection<EObject> users = spreadsheetImport.importSpreadsheet(workbook,
			TaskPackage.eINSTANCE.getUser());
		final Iterator<EObject> iterator = users.iterator();
		assertTrue(EcoreUtil.equals(user, iterator.next()));
		assertTrue(EcoreUtil.equals(user2, iterator.next()));
	}

	private User getDomainModel() throws DatatypeConfigurationException {
		final User user = TaskFactory.eINSTANCE.createUser();
		user.setEmail("myEMail@test.de"); //$NON-NLS-1$
		user.setFirstName("Bob"); //$NON-NLS-1$
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
