/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VDiagnostic;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.validation.ValidationRegistry;
import org.eclipse.emf.ecp.view.validation.ValidationService;
import org.eclipse.emf.ecp.view.validation.test.model.Book;
import org.eclipse.emf.ecp.view.validation.test.model.Librarian;
import org.eclipse.emf.ecp.view.validation.test.model.Library;
import org.eclipse.emf.ecp.view.validation.test.model.TestFactory;
import org.eclipse.emf.ecp.view.validation.test.model.TestPackage;
import org.eclipse.emf.ecp.view.validation.test.model.Writer;
import org.junit.Test;

/**
 * @author jfaltermeier
 * 
 */
public class ValidationServiceTest {

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Init tests
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Tests if a control's {@link VDiagnostic} returns the expected severity.
	 */
	@Test
	public void testInitSingleControlAllCases() {
		testInitSingleControl(createOKWriter(), Diagnostic.OK);
		testInitSingleControl(createInfoWriter(), Diagnostic.INFO);
		testInitSingleControl(createWarningWriter(), Diagnostic.WARNING);
		testInitSingleControl(createErrorWriter(), Diagnostic.ERROR);
		testInitSingleControl(createCancelWriter(), Diagnostic.CANCEL);
	}

	private void testInitSingleControl(Writer writer, int severity) {
		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(writer.eClass());

		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		view.getChildren().add(control);

		instantiateValidationService(view, writer);

		final VDiagnostic diagnostic = control.getDiagnostic();
		assertEquals("Validation of severity " + severity + " was not propagated to control", severity,
			diagnostic.getHighestSeverity(), 0);
	}

	/**
	 * Tests if a controls severity is propagted to its parent.
	 */
	@Test
	public void testInitParentPropagationOneChildAllCases() {
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.OK);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 0, 0), Diagnostic.INFO);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 0, 0), Diagnostic.WARNING);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 1, 0), Diagnostic.ERROR);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 0, 1), Diagnostic.CANCEL);
	}

	/**
	 * @param library
	 * @param severity expected severity
	 */
	private void testInitParentPropagationOneChild(Library library, int severity) {
		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(library.eClass());

		final Column parentColumn = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(parentColumn);

		final Column column = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(column);

		final Control controlWriter = ViewFactory.eINSTANCE.createControl();
		controlWriter.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		controlWriter.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Writers());
		column.getComposites().add(controlWriter);

		instantiateValidationService(view, library);

		final VDiagnostic diagnosticLibary = view.getDiagnostic();
		assertEquals("Validation result was not propagated to parent.", severity,
			diagnosticLibary.getHighestSeverity(),
			0);
	}

	/**
	 * Tests if a (multi-)control choses the highest severity.
	 */
	@Test
	public void testInitHighestSeverityAllCases() {
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 1, 0, 0, 0), Diagnostic.INFO);

		testInitParentPropagationOneChild(createLibaryWithWriters(1, 1, 1, 0, 0), Diagnostic.WARNING);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 0, 1, 0, 0), Diagnostic.WARNING);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 1, 1, 0, 0), Diagnostic.WARNING);

		testInitParentPropagationOneChild(createLibaryWithWriters(1, 1, 1, 1, 0), Diagnostic.ERROR);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 1, 0, 1, 0), Diagnostic.ERROR);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 0, 1, 1, 0), Diagnostic.ERROR);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 1, 0), Diagnostic.ERROR);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 1, 1, 1, 0), Diagnostic.ERROR);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 1, 0), Diagnostic.ERROR);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 1, 0), Diagnostic.ERROR);

		testInitParentPropagationOneChild(createLibaryWithWriters(1, 1, 1, 1, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 1, 1, 0, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 1, 0, 1, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 1, 0, 0, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 0, 1, 1, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 0, 1, 0, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 1, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 0, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 1, 1, 1, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 1, 1, 0, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 1, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 0, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 1, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 0, 1), Diagnostic.CANCEL);
		testInitParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 1, 1), Diagnostic.CANCEL);
	}

	/**
	 * Tests if control with two child controls has to highest severity of its children as its severity.
	 */
	@Test
	public void testInitParentPropagationTwoChildrenAllCases() {
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 0)), Diagnostic.OK,
			Diagnostic.OK);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(0, 1, 0, 0, 0)), Diagnostic.OK,
			Diagnostic.INFO);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(0, 0, 1, 0, 0)), Diagnostic.OK,
			Diagnostic.WARNING);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(0, 0, 0, 1, 0)), Diagnostic.OK,
			Diagnostic.ERROR);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(0, 0, 0, 0, 1)), Diagnostic.OK,
			Diagnostic.CANCEL);

		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 1, 0, 0, 0), createBooks(0, 1, 0, 0, 0)), Diagnostic.INFO,
			Diagnostic.INFO);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 1, 0, 0, 0), createBooks(0, 0, 1, 0, 0)), Diagnostic.INFO,
			Diagnostic.WARNING);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 1, 0, 0, 0), createBooks(0, 0, 0, 1, 0)), Diagnostic.INFO,
			Diagnostic.ERROR);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 1, 0, 0, 0), createBooks(0, 0, 0, 0, 1)), Diagnostic.INFO,
			Diagnostic.CANCEL);

		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 1, 0, 0), createBooks(0, 0, 1, 0, 0)), Diagnostic.WARNING,
			Diagnostic.WARNING);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 1, 0, 0), createBooks(0, 0, 0, 1, 0)), Diagnostic.WARNING,
			Diagnostic.ERROR);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 1, 0, 0), createBooks(0, 0, 0, 0, 1)), Diagnostic.WARNING,
			Diagnostic.CANCEL);

		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 0, 1, 0), createBooks(0, 0, 0, 1, 0)), Diagnostic.ERROR,
			Diagnostic.ERROR);
		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 0, 1, 0), createBooks(0, 0, 0, 0, 1)), Diagnostic.ERROR,
			Diagnostic.CANCEL);

		testInitParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 0, 0, 1), createBooks(0, 0, 0, 0, 1)), Diagnostic.CANCEL,
			Diagnostic.CANCEL);
	}

	/**
	 * @param severityWriter expected severity for writer
	 * @param severityBooks expected severity for books
	 */
	private void testInitParentPropagationTwoChildren(Library library, int severityWriter, int severityBooks) {
		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(library.eClass());

		final Column parentColumn = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(parentColumn);

		// Writers //////////////////////////////////////
		final Column columnWriter = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(columnWriter);

		final Control controlWriter = ViewFactory.eINSTANCE.createControl();
		controlWriter.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		controlWriter.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Writers());
		columnWriter.getComposites().add(controlWriter);

		// Books //////////////////////////////////////////
		final Column columnBooks = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(columnBooks);

		final Control controlBooks = ViewFactory.eINSTANCE.createControl();
		controlBooks.setTargetFeature(TestPackage.eINSTANCE.getBook_Title());
		controlBooks.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Books());
		columnBooks.getComposites().add(controlBooks);

		// Validation ///////////////////////////////////////
		instantiateValidationService(view, library);

		final VDiagnostic diagnosticWriter = columnWriter.getDiagnostic();
		assertEquals("Validation result of writers was not propagated to parent.", severityWriter,
			diagnosticWriter.getHighestSeverity(), 0);

		final VDiagnostic diagnosticBooks = columnBooks.getDiagnostic();
		assertEquals("Validation result of books was not propagated to parent.", severityBooks,
			diagnosticBooks.getHighestSeverity(), 0);

		final int severity = severityWriter > severityBooks ? severityWriter : severityBooks;
		final VDiagnostic diagnosticLibary = view.getDiagnostic();
		assertEquals("Validation result was not propagated to parent.", severity,
			diagnosticLibary.getHighestSeverity(), 0);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Validation upon change tests
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Tests if a control's {@link VDiagnostic} returns the expected severity when context changes.
	 */
	@Test
	public void testChangeSingleControlAllCases() {
		testChangeSingleControl(createOKWriter(), Diagnostic.OK);
		testChangeSingleControl(createOKWriter(), Diagnostic.INFO);
		testChangeSingleControl(createOKWriter(), Diagnostic.WARNING);
		testChangeSingleControl(createOKWriter(), Diagnostic.ERROR);
		testChangeSingleControl(createOKWriter(), Diagnostic.CANCEL);

		testChangeSingleControl(createInfoWriter(), Diagnostic.OK);
		testChangeSingleControl(createInfoWriter(), Diagnostic.INFO);
		testChangeSingleControl(createInfoWriter(), Diagnostic.WARNING);
		testChangeSingleControl(createInfoWriter(), Diagnostic.ERROR);
		testChangeSingleControl(createInfoWriter(), Diagnostic.CANCEL);

		testChangeSingleControl(createWarningWriter(), Diagnostic.OK);
		testChangeSingleControl(createWarningWriter(), Diagnostic.INFO);
		testChangeSingleControl(createWarningWriter(), Diagnostic.WARNING);
		testChangeSingleControl(createWarningWriter(), Diagnostic.ERROR);
		testChangeSingleControl(createWarningWriter(), Diagnostic.CANCEL);

		testChangeSingleControl(createErrorWriter(), Diagnostic.OK);
		testChangeSingleControl(createErrorWriter(), Diagnostic.INFO);
		testChangeSingleControl(createErrorWriter(), Diagnostic.WARNING);
		testChangeSingleControl(createErrorWriter(), Diagnostic.ERROR);
		testChangeSingleControl(createErrorWriter(), Diagnostic.CANCEL);

		testChangeSingleControl(createCancelWriter(), Diagnostic.OK);
		testChangeSingleControl(createCancelWriter(), Diagnostic.INFO);
		testChangeSingleControl(createCancelWriter(), Diagnostic.WARNING);
		testChangeSingleControl(createCancelWriter(), Diagnostic.ERROR);
		testChangeSingleControl(createCancelWriter(), Diagnostic.CANCEL);
	}

	private void testChangeSingleControl(Writer writer, int changeToSeverity) {
		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(writer.eClass());

		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		view.getChildren().add(control);

		instantiateValidationService(view, writer);

		changeWriter(writer, changeToSeverity);

		final VDiagnostic diagnostic = control.getDiagnostic();
		assertEquals("Validation upon change is wrong", changeToSeverity,
			diagnostic.getHighestSeverity(), 0);
	}

	/**
	 * Tests if a controls severity is propagted to its parent upon change.
	 */
	@Test
	public void testChangeParentPropagationOneChildAllCases() {
		testChangeParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.OK);
		testChangeParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.INFO);
		testChangeParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.WARNING);
		testChangeParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.ERROR);
		testChangeParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.CANCEL);

		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 0, 0), Diagnostic.OK);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 0, 0), Diagnostic.INFO);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 0, 0), Diagnostic.WARNING);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 0, 0), Diagnostic.ERROR);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 0, 0), Diagnostic.CANCEL);

		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 0, 0), Diagnostic.OK);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 0, 0), Diagnostic.INFO);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 0, 0), Diagnostic.WARNING);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 0, 0), Diagnostic.ERROR);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 0, 0), Diagnostic.CANCEL);

		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 1, 0), Diagnostic.OK);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 1, 0), Diagnostic.INFO);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 1, 0), Diagnostic.WARNING);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 1, 0), Diagnostic.ERROR);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 1, 0), Diagnostic.CANCEL);

		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 0, 1), Diagnostic.OK);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 0, 1), Diagnostic.INFO);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 0, 1), Diagnostic.WARNING);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 0, 1), Diagnostic.ERROR);
		testChangeParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 0, 1), Diagnostic.CANCEL);
	}

	/**
	 * It is expected that the library only contains one writer!
	 * 
	 * @param library
	 * @param severity expected severity
	 */
	private void testChangeParentPropagationOneChild(Library library, int changeToSeverity) {
		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(library.eClass());

		final Column parentColumn = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(parentColumn);

		final Column column = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(column);

		final Control controlWriter = ViewFactory.eINSTANCE.createControl();
		controlWriter.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		controlWriter.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Writers());
		column.getComposites().add(controlWriter);

		instantiateValidationService(view, library);

		changeWriter(library.getWriters().get(0), changeToSeverity);

		final VDiagnostic diagnosticLibary = view.getDiagnostic();
		assertEquals("Validation result was not propagated to parent.", changeToSeverity,
			diagnosticLibary.getHighestSeverity(), 0);
	}

	@Test
	public void testAddAndRemoveChildHighestSeverityAllCases() {
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.OK);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 0, 0, 0), Diagnostic.OK);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 0, 0), Diagnostic.OK);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 0), Diagnostic.OK);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 1), Diagnostic.OK);

		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.INFO);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 0, 0, 0), Diagnostic.INFO);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 0, 0), Diagnostic.INFO);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 0), Diagnostic.INFO);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 1), Diagnostic.INFO);

		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.WARNING);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 0, 0, 0), Diagnostic.WARNING);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 0, 0), Diagnostic.WARNING);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 0), Diagnostic.WARNING);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 1), Diagnostic.WARNING);

		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.ERROR);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 0, 0, 0), Diagnostic.ERROR);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 0, 0), Diagnostic.ERROR);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 0), Diagnostic.ERROR);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 1), Diagnostic.ERROR);

		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.CANCEL);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 0, 0, 0), Diagnostic.CANCEL);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 0, 0), Diagnostic.CANCEL);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 0), Diagnostic.CANCEL);
		testAddAndRemoveChildHighestSeverity(createLibaryWithWriters(1, 1, 1, 1, 1), Diagnostic.CANCEL);
	}

	private void testAddAndRemoveChildHighestSeverity(Library library, int severityOfNewWriter) {
		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(library.eClass());

		final Column parentColumn = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(parentColumn);

		final Column column = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(column);

		final Control controlWriter = ViewFactory.eINSTANCE.createControl();
		controlWriter.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		controlWriter.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Writers());
		column.getComposites().add(controlWriter);

		instantiateValidationService(view, library);

		final int severityBefore = view.getDiagnostic().getHighestSeverity();

		final Writer writer = changeWriter(TestFactory.eINSTANCE.createWriter(), severityOfNewWriter);

		library.getWriters().add(writer);

		final int severityAfterAdd = view.getDiagnostic().getHighestSeverity();

		if (severityBefore >= severityOfNewWriter) {
			assertEquals(severityBefore, severityAfterAdd, 0);
		} else {
			assertEquals(severityOfNewWriter, severityAfterAdd, 0);
		}

		library.getWriters().remove(writer);

		final int severityAfterRemove = view.getDiagnostic().getHighestSeverity();

		assertEquals(severityBefore, severityAfterRemove);
	}

	@Test
	public void testChangeParentPropagationTwoChildrenAllCases() {
		// testChangeParentPropagationTwoChildren(
		// addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 0)), Diagnostic.OK);
		// testChangeParentPropagationTwoChildren(
		// addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 0)), Diagnostic.INFO);
		// testChangeParentPropagationTwoChildren(
		// addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 0)), Diagnostic.WARNING);
		// testChangeParentPropagationTwoChildren(
		// addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 0)), Diagnostic.ERROR);
		// testChangeParentPropagationTwoChildren(
		// addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 0)), Diagnostic.CANCEL);

		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 1, 0, 0, 0)), Diagnostic.OK);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 1, 0, 0, 0)), Diagnostic.INFO);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 1, 0, 0, 0)), Diagnostic.WARNING);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 1, 0, 0, 0)), Diagnostic.ERROR);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 1, 0, 0, 0)), Diagnostic.CANCEL);

		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 0, 0)), Diagnostic.OK);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 0, 0)), Diagnostic.INFO);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 0, 0)), Diagnostic.WARNING);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 0, 0)), Diagnostic.ERROR);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 0, 0)), Diagnostic.CANCEL);

		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 1, 0)), Diagnostic.OK);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 1, 0)), Diagnostic.INFO);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 1, 0)), Diagnostic.WARNING);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 1, 0)), Diagnostic.ERROR);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 1, 1, 0)), Diagnostic.CANCEL);

		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 1)), Diagnostic.OK);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 1)), Diagnostic.INFO);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 1)), Diagnostic.WARNING);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 1)), Diagnostic.ERROR);
		testChangeParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 1)), Diagnostic.CANCEL);
	}

	/**
	 * It is expected that library only has one writer.
	 * 
	 * @param newSeverityWriter expected severity for writer
	 * @param severityBooks expected severity for books
	 */
	private void testChangeParentPropagationTwoChildren(Library library, int newSeverityWriter) {
		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(library.eClass());

		final Column parentColumn = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(parentColumn);

		// Writers //////////////////////////////////////
		final Column columnWriter = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(columnWriter);

		final Control controlWriter = ViewFactory.eINSTANCE.createControl();
		controlWriter.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		controlWriter.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Writers());
		columnWriter.getComposites().add(controlWriter);

		// Books //////////////////////////////////////////
		final Column columnBooks = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(columnBooks);

		final Control controlBooks = ViewFactory.eINSTANCE.createControl();
		controlBooks.setTargetFeature(TestPackage.eINSTANCE.getBook_Title());
		controlBooks.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Books());
		columnBooks.getComposites().add(controlBooks);

		// Validation ///////////////////////////////////////
		instantiateValidationService(view, library);

		final int diagnosticWriterBefore = columnWriter.getDiagnostic().getHighestSeverity();
		final int diagnosticBooks = columnBooks.getDiagnostic().getHighestSeverity();

		final Writer writer = library.getWriters().get(0);
		changeWriter(writer, newSeverityWriter);

		final int expectedSeverityWriter = diagnosticWriterBefore > newSeverityWriter ? diagnosticWriterBefore
			: newSeverityWriter;
		assertEquals(expectedSeverityWriter, columnWriter.getDiagnostic().getHighestSeverity());

		final int expectedSeverityAll = expectedSeverityWriter > diagnosticBooks ? expectedSeverityWriter
			: diagnosticBooks;
		assertEquals(expectedSeverityAll, view.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testMappingDiagnosticResultToControls() {
		// Book with warning (no writer) and error (no title)
		final Book book = TestFactory.eINSTANCE.createBook();

		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(book.eClass());

		final Control controlTitle = ViewFactory.eINSTANCE.createControl();
		controlTitle.setTargetFeature(TestPackage.eINSTANCE.getBook_Title());
		view.getChildren().add(controlTitle);

		final Control controlWriter = ViewFactory.eINSTANCE.createControl();
		controlWriter.setTargetFeature(TestPackage.eINSTANCE.getBook_Writers());
		view.getChildren().add(controlWriter);

		instantiateValidationService(view, book);

		VDiagnostic diagnosticTitle = controlTitle.getDiagnostic();
		assertEquals(Diagnostic.ERROR, diagnosticTitle.getHighestSeverity());

		VDiagnostic diagnosticWriter = controlWriter.getDiagnostic();
		assertEquals(Diagnostic.WARNING, diagnosticWriter.getHighestSeverity());

		VDiagnostic diagnosticBook = view.getDiagnostic();
		assertEquals(Diagnostic.ERROR, diagnosticBook.getHighestSeverity());

		// change book title info
		book.setTitle("a");

		diagnosticTitle = controlTitle.getDiagnostic();
		assertEquals(Diagnostic.INFO, diagnosticTitle.getHighestSeverity());

		diagnosticWriter = controlWriter.getDiagnostic();
		assertEquals(Diagnostic.WARNING, diagnosticWriter.getHighestSeverity());

		diagnosticBook = view.getDiagnostic();
		assertEquals(Diagnostic.WARNING, diagnosticBook.getHighestSeverity());
	}

	@Test
	public void testRegistry() {
		final Library library = addBooksToLibrary(createLibaryWithWriters(1, 1, 1, 1, 1), createBooks(1, 1, 1, 1, 1));

		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(library.eClass());

		final Column parentColumn = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(parentColumn);

		// Writers //////////////////////////////////////
		final Column columnWriter = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(columnWriter);

		final Control controlWriter = ViewFactory.eINSTANCE.createControl();
		controlWriter.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		controlWriter.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Writers());
		columnWriter.getComposites().add(controlWriter);

		// Books //////////////////////////////////////////
		final Column columnBooks = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(columnBooks);

		final Control controlBooks = ViewFactory.eINSTANCE.createControl();
		controlBooks.setTargetFeature(TestPackage.eINSTANCE.getBook_Title());
		controlBooks.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Books());
		columnBooks.getComposites().add(controlBooks);

		// Test ///////////////////////////////////////////
		final ValidationRegistry registry = new ValidationRegistry();
		registry.register(library, view);

		final List<Renderable> librayRenderables = registry.getRenderablesForEObject(library);
		assertEquals("Library has false renderable count", 2, librayRenderables.size());
		assertEquals("Wrong renderable is associated to library", parentColumn, librayRenderables.get(0));
		assertEquals("Wrong renderable is associated to library", view, librayRenderables.get(1));

		for (final Writer writer : library.getWriters()) {
			final List<Renderable> writerRenderables = registry.getRenderablesForEObject(writer);
			assertEquals("Writer has false renderable count", 2, writerRenderables.size());
			assertEquals("Wrong renderable is associated to writer", controlWriter, writerRenderables.get(0));
			assertEquals("Wrong renderable is associated to writer", columnWriter, writerRenderables.get(1));
		}

		for (final Book book : library.getBooks()) {
			final List<Renderable> bookRenderables = registry.getRenderablesForEObject(book);
			assertEquals("Book has false renderable count", 2, bookRenderables.size());
			assertEquals("Wrong renderable is associated to book", controlBooks, bookRenderables.get(0));
			assertEquals("Wrong renderable is associated to book", columnBooks, bookRenderables.get(1));
		}

		final List<EObject> writerControlObjects = registry.getEObjectsForControl(controlWriter);
		assertEquals("Number of books associated to book control is wrong.", 5, writerControlObjects.size(), 0);
		for (final EObject o : writerControlObjects) {
			assertTrue(library.getWriters().contains(o));
		}

		final List<EObject> bookControlObjects = registry.getEObjectsForControl(controlBooks);
		assertEquals("Number of books associated to book control is wrong.", 5, bookControlObjects.size(), 0);
		for (final EObject o : bookControlObjects) {
			assertTrue(library.getBooks().contains(o));
		}

		// TODO remove after coverage
		testRegistrySingleContainmentRef();
	}

	@Test
	public void testRegistrySingleContainmentRef() {
		final Library library = TestFactory.eINSTANCE.createLibrary();
		final Writer writer = createOKWriter();
		final Librarian librarian = TestFactory.eINSTANCE.createLibrarian();
		library.getWriters().add(writer);
		library.setLibrarian(librarian);

		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(library.eClass());

		final Column parentColumn = ViewFactory.eINSTANCE.createColumn();
		view.getChildren().add(parentColumn);

		final Control controlWriterFirstName = ViewFactory.eINSTANCE.createControl();
		controlWriterFirstName.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());
		controlWriterFirstName.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Writers());
		parentColumn.getComposites().add(controlWriterFirstName);

		final Control controlLibrarianName = ViewFactory.eINSTANCE.createControl();
		controlLibrarianName.setTargetFeature(TestPackage.eINSTANCE.getLibrarian_Name());
		controlLibrarianName.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Librarian());
		parentColumn.getComposites().add(controlLibrarianName);

		// Test ///////////////////////////////////////////
		final ValidationRegistry registry = new ValidationRegistry();
		registry.register(library, view);

		final List<Renderable> librayRenderables = registry.getRenderablesForEObject(library);
		assertEquals("Library has false renderable count", 2, librayRenderables.size());
		assertEquals("Wrong renderable is associated to library", parentColumn, librayRenderables.get(0));
		assertEquals("Wrong renderable is associated to library", view, librayRenderables.get(1));

		final List<Renderable> writerRenderables = registry.getRenderablesForEObject(writer);
		assertEquals(1, writerRenderables.size(), 0);
		assertTrue(writerRenderables.contains(controlWriterFirstName));

		final List<Renderable> librarianRenderables = registry.getRenderablesForEObject(librarian);
		assertEquals(1, librarianRenderables.size(), 0);
		assertTrue(librarianRenderables.contains(controlLibrarianName));

		final List<EObject> librarianObjects = registry.getEObjectsForControl(controlLibrarianName);
		assertEquals(1, librarianObjects.size(), 0);
		assertTrue(librarianObjects.contains(librarian));

		final List<EObject> writerNameObjects = registry.getEObjectsForControl(controlWriterFirstName);
		assertEquals(1, writerNameObjects.size(), 0);
		assertTrue(writerNameObjects.contains(writer));
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Util from here
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Instantiate validation service
	 * 
	 * @return the validation service
	 */
	private ValidationService instantiateValidationService(View view, final EObject domainModel) {
		final ValidationService validationService = new ValidationService();
		validationService.instantiate(new ViewModelContextImpl(view, domainModel));
		return validationService;
	}

	/**
	 * Creates a library with the given count of writers and severities.
	 */
	private Library createLibaryWithWriters(int ok, int info, int warning, int error, int cancel) {
		final Library library = TestFactory.eINSTANCE.createLibrary();
		final Set<Writer> writers = new HashSet<Writer>();
		for (int i = 0; i < ok; i++) {
			writers.add(createOKWriter());
		}
		for (int i = 0; i < info; i++) {
			writers.add(createInfoWriter());
		}
		for (int i = 0; i < warning; i++) {
			writers.add(createWarningWriter());
		}
		for (int i = 0; i < error; i++) {
			writers.add(createErrorWriter());
		}
		for (int i = 0; i < cancel; i++) {
			writers.add(createCancelWriter());
		}
		library.getWriters().addAll(writers);
		return library;
	}

	/**
	 * Creates a set of books with the given count of severities.
	 */
	private Set<Book> createBooks(int ok, int info, int warning, int error, int cancel) {
		final Set<Book> books = new HashSet<Book>();
		for (int i = 0; i < ok; i++) {
			books.add(createOKBook());
		}
		for (int i = 0; i < info; i++) {
			books.add(createInfoBook());
		}
		for (int i = 0; i < warning; i++) {
			books.add(createWarningBook());
		}
		for (int i = 0; i < error; i++) {
			books.add(createErrorBook());
		}
		for (int i = 0; i < cancel; i++) {
			books.add(createCancelBook());
		}
		return books;
	}

	private Library addBooksToLibrary(Library library, Set<Book> books) {
		library.getBooks().addAll(books);
		return library;
	}

	private Writer createOKWriter() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		return changeWriter(writer, Diagnostic.OK);
	}

	private Writer createInfoWriter() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		return changeWriter(writer, Diagnostic.INFO);
	}

	private Writer createWarningWriter() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		return changeWriter(writer, Diagnostic.WARNING);
	}

	private Writer createErrorWriter() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		return changeWriter(writer, Diagnostic.ERROR);
	}

	private Writer createCancelWriter() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		return changeWriter(writer, Diagnostic.CANCEL);
	}

	private Writer changeWriter(Writer writer, int changeToSeverity) {
		switch (changeToSeverity) {
		case Diagnostic.OK:
			writer.setFirstName("Hans");
			writer.setLastName("Meyer");
			break;
		case Diagnostic.INFO:
			writer.setFirstName("H");
			writer.setLastName("M");
			break;
		case Diagnostic.WARNING:
			writer.setFirstName("Hans");
			writer.setLastName("Hans");
			break;
		case Diagnostic.ERROR:
			writer.setFirstName("");
			writer.setLastName("");
			break;
		case Diagnostic.CANCEL:
			writer.setFirstName("Offensive");
			writer.setLastName("Offensive");
			break;
		default:
			org.junit.Assert.fail("No Diagnostic value like " + changeToSeverity + "found.");
		}
		return writer;
	}

	private Book createOKBook() {
		final Writer writer = createOKWriter();
		final Book book = TestFactory.eINSTANCE.createBook();
		book.setTitle("Eine Chronik");
		book.setWriters(writer);
		return book;
	}

	private Book createInfoBook() {
		final Writer writer = createOKWriter();
		final Book book = TestFactory.eINSTANCE.createBook();
		book.setTitle("C");
		book.setWriters(writer);
		return book;
	}

	private Book createWarningBook() {
		final Book book = TestFactory.eINSTANCE.createBook();
		book.setTitle("Warning");
		return book;
	}

	private Book createErrorBook() {
		final Writer writer = createOKWriter();
		final Book book = TestFactory.eINSTANCE.createBook();
		book.setWriters(writer);
		return book;
	}

	private Book createCancelBook() {
		final Writer writer = createOKWriter();
		final Book book = TestFactory.eINSTANCE.createBook();
		book.setTitle("Offensive");
		book.setWriters(writer);
		return book;
	}
}
