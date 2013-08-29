/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.TableColumn;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.validation.test.model.Computer;
import org.eclipse.emf.ecp.view.validation.test.model.Library;
import org.eclipse.emf.ecp.view.validation.test.model.Mainboard;
import org.eclipse.emf.ecp.view.validation.test.model.PowerBlock;
import org.eclipse.emf.ecp.view.validation.test.model.TestFactory;
import org.eclipse.emf.ecp.view.validation.test.model.TestPackage;
import org.eclipse.emf.ecp.view.validation.test.model.Writer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eugen Neufeld
 * 
 */
public class ViewValidationTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidationInitOk() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		computer.setName("bla");
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());

		final ViewModelContext vmc = new ViewModelContextImpl(control, computer);

		assertEquals("Severity must be ok", Diagnostic.OK, control.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationInitError() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());

		final ViewModelContext vmc = new ViewModelContextImpl(control, computer);

		assertEquals("Severity must be error", Diagnostic.ERROR, control.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationInitAggregation() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		computer.setName("bla");
		final PowerBlock power = TestFactory.eINSTANCE.createPowerBlock();
		power.setName("bla");
		final Mainboard board = TestFactory.eINSTANCE.createMainboard();
		computer.setMainboard(board);
		computer.setPowerBlock(power);

		final View view = ViewFactory.eINSTANCE.createView();
		final Control controlPower = ViewFactory.eINSTANCE.createControl();
		controlPower.getPathToFeature().add(TestPackage.eINSTANCE.getComputer_PowerBlock());
		controlPower.setTargetFeature(TestPackage.eINSTANCE.getPowerBlock_Name());

		final Control controlBoard = ViewFactory.eINSTANCE.createControl();
		controlBoard.getPathToFeature().add(TestPackage.eINSTANCE.getComputer_Mainboard());
		controlBoard.setTargetFeature(TestPackage.eINSTANCE.getMainboard_Name());

		view.getChildren().add(controlBoard);
		view.getChildren().add(controlPower);

		final ViewModelContext vmc = new ViewModelContextImpl(view, computer);

		assertEquals("Severity of mainboard must be error", Diagnostic.ERROR, controlBoard.getDiagnostic()
			.getHighestSeverity());
		assertEquals("Severity of power must be ok", Diagnostic.OK, controlPower.getDiagnostic().getHighestSeverity());
		assertEquals("Severity of view must be ok", Diagnostic.ERROR, view.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationInitPropagation1LevelOk() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		computer.setName("bla");
		final Column column = ViewFactory.eINSTANCE.createColumn();
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());

		column.getComposites().add(control);

		final ViewModelContext vmc = new ViewModelContextImpl(column, computer);

		assertEquals("Severity of control must be ok", Diagnostic.OK, control.getDiagnostic().getHighestSeverity());
		assertEquals("Severity of column must be ok", Diagnostic.OK, column.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationInitPropagation1LevelError() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();

		final Column column = ViewFactory.eINSTANCE.createColumn();
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());

		column.getComposites().add(control);

		final ViewModelContext vmc = new ViewModelContextImpl(column, computer);

		assertEquals("Severity of control must be error", Diagnostic.ERROR, control.getDiagnostic()
			.getHighestSeverity());
		assertEquals("Severity of column must be error", Diagnostic.ERROR, column.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationInitPropagation2LevelOk() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		computer.setName("bla");
		final View view = ViewFactory.eINSTANCE.createView();
		final Column column = ViewFactory.eINSTANCE.createColumn();
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());

		column.getComposites().add(control);
		view.getChildren().add(column);

		final ViewModelContext vmc = new ViewModelContextImpl(view, computer);

		assertEquals("Severity of control must be ok", Diagnostic.OK, control.getDiagnostic().getHighestSeverity());
		assertEquals("Severity of column must be ok", Diagnostic.OK, column.getDiagnostic().getHighestSeverity());
		assertEquals("Severity of view must be ok", Diagnostic.OK, view.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationInitPropagation2LevelError() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();

		final View view = ViewFactory.eINSTANCE.createView();

		final Column column = ViewFactory.eINSTANCE.createColumn();
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());

		column.getComposites().add(control);
		view.getChildren().add(column);

		final ViewModelContext vmc = new ViewModelContextImpl(column, computer);

		assertEquals("Severity of control must be error", Diagnostic.ERROR, control.getDiagnostic()
			.getHighestSeverity());
		assertEquals("Severity of column must be error", Diagnostic.ERROR, column.getDiagnostic().getHighestSeverity());
		assertEquals("Severity of view must be error", Diagnostic.ERROR, view.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationChangeOkToError() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		computer.setName("bla");
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());

		final ViewModelContext vmc = new ViewModelContextImpl(control, computer);

		computer.setName(null);

		assertEquals("Severity must be error", Diagnostic.ERROR, control.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationChangeErrorToOk() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());

		final ViewModelContext vmc = new ViewModelContextImpl(control, computer);

		computer.setName("bla");

		assertEquals("Severity must be ok", Diagnostic.OK, control.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationPropagationChangeOkToError() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		computer.setName("bla");
		final View view = ViewFactory.eINSTANCE.createView();
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());
		view.getChildren().add(control);

		final ViewModelContext vmc = new ViewModelContextImpl(view, computer);

		computer.setName(null);

		assertEquals("Severity of control must be error", Diagnostic.ERROR, control.getDiagnostic()
			.getHighestSeverity());
		assertEquals("Severity of view must be error", Diagnostic.ERROR, view.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationPropagationChangeErrorToOk() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		final View view = ViewFactory.eINSTANCE.createView();
		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getComputer_Name());

		view.getChildren().add(control);

		final ViewModelContext vmc = new ViewModelContextImpl(view, computer);

		computer.setName("bla");

		assertEquals("Severity of control must be ok", Diagnostic.OK, control.getDiagnostic().getHighestSeverity());
		assertEquals("Severity of view must be ok", Diagnostic.OK, control.getDiagnostic().getHighestSeverity());
	}

	// TODO fails as the control creates all intermediate domain objects it needs and registers with them, thus the
	// added object is not recognized during validation and the validation is not successful.
	// In order to allow the dynamic behavior on normal controls, each control has to know its domain object directly
	@Test
	public void testValidationDynamicExtension() {
		final Computer computer = TestFactory.eINSTANCE.createComputer();
		computer.setName("bla");

		final View view = ViewFactory.eINSTANCE.createView();

		final ViewModelContext vmc = new ViewModelContextImpl(view, computer);

		final Control control = ViewFactory.eINSTANCE.createControl();
		control.getPathToFeature().add(TestPackage.eINSTANCE.getComputer_Mainboard());
		control.setTargetFeature(TestPackage.eINSTANCE.getMainboard_Name());
		view.getChildren().add(control);

		final Mainboard mainboard = TestFactory.eINSTANCE.createMainboard();
		computer.setMainboard(mainboard);

		assertEquals("Severity of mainboard name must be error", Diagnostic.ERROR, control.getDiagnostic()
			.getHighestSeverity());
	}

	@Test
	public void testValidationTableControl() {
		final Library lib = TestFactory.eINSTANCE.createLibrary();
		lib.setName("bla");

		final TableControl control = ViewFactory.eINSTANCE.createTableControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getLibrary_Writers());

		final ViewModelContext vmc = new ViewModelContextImpl(control, lib);

		assertEquals("Severity of table must be error", Diagnostic.ERROR, control.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationTableControlChildren() {
		final Library lib = TestFactory.eINSTANCE.createLibrary();
		lib.setName("bla");
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		lib.getWriters().add(writer);

		final TableControl control = ViewFactory.eINSTANCE.createTableControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getLibrary_Writers());
		final TableColumn tc = ViewFactory.eINSTANCE.createTableColumn();
		tc.setAttribute(TestPackage.eINSTANCE.getWriter_FirstName());
		control.getColumns().add(tc);

		final ViewModelContext vmc = new ViewModelContextImpl(control, lib);

		assertEquals("Severity of table must be error", Diagnostic.ERROR, control.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationTableControlChildrenUpdateExisting() {
		final Library lib = TestFactory.eINSTANCE.createLibrary();
		lib.setName("bla");
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		lib.getWriters().add(writer);

		final TableControl control = ViewFactory.eINSTANCE.createTableControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getLibrary_Writers());
		final TableColumn tc = ViewFactory.eINSTANCE.createTableColumn();
		tc.setAttribute(TestPackage.eINSTANCE.getWriter_FirstName());
		control.getColumns().add(tc);

		final ViewModelContext vmc = new ViewModelContextImpl(control, lib);

		writer.setFirstName("bla");
		assertEquals("Severity of table must be ok", Diagnostic.OK, control.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationTableControlChildrenAddNew() {
		final Library lib = TestFactory.eINSTANCE.createLibrary();
		lib.setName("bla");
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		lib.getWriters().add(writer);
		writer.setFirstName("bla");

		final TableControl control = ViewFactory.eINSTANCE.createTableControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getLibrary_Writers());
		final TableColumn tc = ViewFactory.eINSTANCE.createTableColumn();
		tc.setAttribute(TestPackage.eINSTANCE.getWriter_FirstName());
		control.getColumns().add(tc);

		final ViewModelContext vmc = new ViewModelContextImpl(control, lib);

		final Writer writer2 = TestFactory.eINSTANCE.createWriter();
		lib.getWriters().add(writer2);
		assertEquals("Severity of table must be error", Diagnostic.ERROR, control.getDiagnostic().getHighestSeverity());
	}
}
