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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.makeithappen.model.task.Task;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskFactory;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskPackage;
import org.eclipse.emf.ecp.test.common.DefaultRealm;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTableFactory;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.spreadsheet.core.transfer.EMFFormsSpreadsheetExporter;
import org.eclipse.emfforms.spi.spreadsheet.core.transfer.EMFFormsSpreadsheetImporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EMFFormsSpreadsheetTableControlRenderer_ITest {
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
	public void test() throws DatatypeConfigurationException, DatabindingFailedException, IOException {
		// write data
		EMFFormsSpreadsheetExporter viewRenderer = EMFFormsSpreadsheetExporter.INSTANCE;
		File targetFile = new File("export.xls");
		EObject domainModel = getDomainModel();
		EObject domainModel2 = getDomainModel();
		Workbook wb = viewRenderer.render(Arrays.asList(domainModel,domainModel2),getView(),null);
		
		FileOutputStream fileOut = new FileOutputStream(targetFile.getAbsolutePath());
		wb.write(fileOut);
		fileOut.close();

		// read data
		final FileInputStream file = new FileInputStream(targetFile);
		final Workbook workbook = new HSSFWorkbook(file);
		
		final EMFFormsSpreadsheetImporter spreadsheetImport = EMFFormsSpreadsheetImporter.INSTANCE;
		final Collection<EObject> domainModels = spreadsheetImport.importSpreadsheet(workbook, TaskPackage.eINSTANCE.getTask());
		
		for (final EObject model : domainModels) {
			assertTrue(EcoreUtil.equals(model, domainModel));
		}
		file.close();
	}

	private EObject getDomainModel() {
		Task task=TaskFactory.eINSTANCE.createTask();
		for(int i=0;i<3;i++){
			Task subTask=TaskFactory.eINSTANCE.createTask();
			subTask.setName("task_"+i);
			task.getSubTasks().add(subTask);
			
			for(int k=0;k<3;k++){
				Task subsubTask=TaskFactory.eINSTANCE.createTask();
				subsubTask.setDescription("bla_"+i+"_"+k);
				subsubTask.setName("task_"+i+"_"+k);
				subTask.getSubTasks().add(subsubTask);
			}
		}
		return task;
	}
	
	private VView getView(){
		VView view=VViewFactory.eINSTANCE.createView();
		view.setRootEClass(TaskPackage.eINSTANCE.getTask());
		VTableControl table=VTableFactory.eINSTANCE.createTableControl();
		view.getChildren().add(table);
		
		VTableDomainModelReference tDMR=VTableFactory.eINSTANCE.createTableDomainModelReference();
		table.setDomainModelReference(tDMR);
		VFeaturePathDomainModelReference innerDMR=VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		innerDMR.setDomainModelEFeature(TaskPackage.eINSTANCE.getTask_SubTasks());
		tDMR.setDomainModelReference(innerDMR);
		VFeaturePathDomainModelReference col1=VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		col1.setDomainModelEFeature(TaskPackage.eINSTANCE.getTask_Name());
		tDMR.getColumnDomainModelReferences().add(col1);
		
		VView subView=VViewFactory.eINSTANCE.createView();
		subView.setRootEClass(TaskPackage.eINSTANCE.getTask());
		
		VTableControl subtable=VTableFactory.eINSTANCE.createTableControl();
		subView.getChildren().add(subtable);
		VTableDomainModelReference subtDMR=VTableFactory.eINSTANCE.createTableDomainModelReference();
		subtable.setDomainModelReference(subtDMR);
		VFeaturePathDomainModelReference subinnerDMR=VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		subinnerDMR.setDomainModelEFeature(TaskPackage.eINSTANCE.getTask_SubTasks());
		subtDMR.setDomainModelReference(subinnerDMR);
		VFeaturePathDomainModelReference subcol1=VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		subcol1.setDomainModelEFeature(TaskPackage.eINSTANCE.getTask_Name());
		subtDMR.getColumnDomainModelReferences().add(subcol1);
		
		VView subsubView=VViewFactory.eINSTANCE.createView();
		subsubView.setRootEClass(TaskPackage.eINSTANCE.getTask());
		
		VControl vControl=VViewFactory.eINSTANCE.createControl();
		subsubView.getChildren().add(vControl);
		VFeaturePathDomainModelReference col2=VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		col2.setDomainModelEFeature(TaskPackage.eINSTANCE.getTask_Description());
		vControl.setDomainModelReference(col2);
		
		subtable.setDetailEditing(DetailEditing.WITH_PANEL);
		subtable.setDetailView(subsubView);
		
		table.setDetailEditing(DetailEditing.WITH_PANEL);
		table.setDetailView(subView);
		return view;
	}
}
