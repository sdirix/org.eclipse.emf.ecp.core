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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.VDiagnostic;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.validation.test.model.Book;
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

	/**
	 * Tests if a control's {@link VDiagnostic} returns the expected severity.
	 */
	@Test
	public void testSingleControl() {
		testSingleControl(createOKWriter(), Diagnostic.OK);
		testSingleControl(createInfoWriter(), Diagnostic.INFO);
		testSingleControl(createWarningWriter(), Diagnostic.WARNING);
		testSingleControl(createErrorWriter(), Diagnostic.ERROR);
		testSingleControl(createCancelWriter(), Diagnostic.CANCEL);
	}

	private void testSingleControl(Writer writer, int severity) {
		final View view = ViewFactory.eINSTANCE.createView();
		view.setRootEClass(writer.eClass());

		final Control control = ViewFactory.eINSTANCE.createControl();
		control.setTargetFeature(TestPackage.eINSTANCE.getWriter_FirstName());

		final VDiagnostic diagnostic = control.getDiagnostic();
		assertEquals("Validation of severity " + severity + " was not propagated to control", severity,
			diagnostic.getHighestSeverity(), 0);
	}

	/**
	 * Tests if a controls severity is propagted to its parent.
	 */
	@Test
	public void testParentPropagationOneChild() {
		testParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 0, 0), Diagnostic.OK);
		testParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 0, 0), Diagnostic.INFO);
		testParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 0, 0), Diagnostic.WARNING);
		testParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 1, 0), Diagnostic.ERROR);
		testParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 0, 1), Diagnostic.CANCEL);
	}

	/**
	 * @param library
	 * @param severity expected severity
	 */
	private void testParentPropagationOneChild(Library library, int severity) {
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

		final VDiagnostic diagnosticLibary = view.getDiagnostic();
		assertEquals("Validation result was not propagated to parent.", severity,
			diagnosticLibary.getHighestSeverity(),
			0);
	}

	/**
	 * Tests if a (multi-)control choses the highest severity.
	 */
	@Test
	public void testHighestSeverity() {
		testParentPropagationOneChild(createLibaryWithWriters(1, 1, 0, 0, 0), Diagnostic.INFO);

		testParentPropagationOneChild(createLibaryWithWriters(1, 1, 1, 0, 0), Diagnostic.WARNING);
		testParentPropagationOneChild(createLibaryWithWriters(1, 0, 1, 0, 0), Diagnostic.WARNING);
		testParentPropagationOneChild(createLibaryWithWriters(0, 1, 1, 0, 0), Diagnostic.WARNING);

		testParentPropagationOneChild(createLibaryWithWriters(1, 1, 1, 1, 0), Diagnostic.ERROR);
		testParentPropagationOneChild(createLibaryWithWriters(1, 1, 0, 1, 0), Diagnostic.ERROR);
		testParentPropagationOneChild(createLibaryWithWriters(1, 0, 1, 1, 0), Diagnostic.ERROR);
		testParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 1, 0), Diagnostic.ERROR);
		testParentPropagationOneChild(createLibaryWithWriters(0, 1, 1, 1, 0), Diagnostic.ERROR);
		testParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 1, 0), Diagnostic.ERROR);
		testParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 1, 0), Diagnostic.ERROR);

		testParentPropagationOneChild(createLibaryWithWriters(1, 1, 1, 1, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(1, 1, 1, 0, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(1, 1, 0, 1, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(1, 1, 0, 0, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(1, 0, 1, 1, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(1, 0, 1, 0, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 1, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(1, 0, 0, 0, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(0, 1, 1, 1, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(0, 1, 1, 0, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 1, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(0, 1, 0, 0, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 1, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(0, 0, 1, 0, 1), Diagnostic.CANCEL);
		testParentPropagationOneChild(createLibaryWithWriters(0, 0, 0, 1, 1), Diagnostic.CANCEL);
	}

	/**
	 * Tests if control with two child controls has to highest severity of its children as its severity.
	 */
	@Test
	public void testParentPropagationTwoChildren() {
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(1, 0, 0, 0, 0)), Diagnostic.OK,
			Diagnostic.OK);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(0, 1, 0, 0, 0)), Diagnostic.OK,
			Diagnostic.INFO);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(0, 0, 1, 0, 0)), Diagnostic.OK,
			Diagnostic.WARNING);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(0, 0, 0, 1, 0)), Diagnostic.OK,
			Diagnostic.ERROR);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(1, 0, 0, 0, 0), createBooks(0, 0, 0, 0, 1)), Diagnostic.OK,
			Diagnostic.CANCEL);

		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 1, 0, 0, 0), createBooks(0, 1, 0, 0, 0)), Diagnostic.INFO,
			Diagnostic.INFO);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 1, 0, 0, 0), createBooks(0, 0, 1, 0, 0)), Diagnostic.INFO,
			Diagnostic.WARNING);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 1, 0, 0, 0), createBooks(0, 0, 0, 1, 0)), Diagnostic.INFO,
			Diagnostic.ERROR);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 1, 0, 0, 0), createBooks(0, 0, 0, 0, 1)), Diagnostic.INFO,
			Diagnostic.CANCEL);

		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 1, 0, 0), createBooks(0, 0, 1, 0, 0)), Diagnostic.WARNING,
			Diagnostic.WARNING);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 1, 0, 0), createBooks(0, 0, 0, 1, 0)), Diagnostic.WARNING,
			Diagnostic.ERROR);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 1, 0, 0), createBooks(0, 0, 0, 0, 1)), Diagnostic.WARNING,
			Diagnostic.CANCEL);

		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 0, 1, 0), createBooks(0, 0, 0, 1, 0)), Diagnostic.ERROR,
			Diagnostic.ERROR);
		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 0, 1, 0), createBooks(0, 0, 0, 0, 1)), Diagnostic.ERROR,
			Diagnostic.CANCEL);

		testParentPropagationTwoChildren(
			addBooksToLibrary(createLibaryWithWriters(0, 0, 0, 0, 1), createBooks(0, 0, 0, 0, 1)), Diagnostic.CANCEL,
			Diagnostic.CANCEL);
	}

	/**
	 * @param severityWriter expected severity for writer
	 * @param severityBooks expected severity for books
	 */
	private void testParentPropagationTwoChildren(Library library, int severityWriter, int severityBooks) {
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

		final VDiagnostic diagnosticWriter = columnWriter.getDiagnostic();
		assertEquals("Validation result of writers was not propagated to parent.", severityWriter,
			diagnosticWriter.getHighestSeverity(), 0);

		// Books //////////////////////////////////////////
		final Column columnBooks = ViewFactory.eINSTANCE.createColumn();
		parentColumn.getComposites().add(columnBooks);

		final Control controlBooks = ViewFactory.eINSTANCE.createControl();
		controlBooks.setTargetFeature(TestPackage.eINSTANCE.getBook_Title());
		controlBooks.getPathToFeature().add(TestPackage.eINSTANCE.getLibrary_Books());
		columnBooks.getComposites().add(controlBooks);

		final VDiagnostic diagnosticBooks = columnBooks.getDiagnostic();
		assertEquals("Validation result of books was not propagated to parent.", severityBooks,
			diagnosticBooks.getHighestSeverity(), 0);

		// View //////////////////////////////////////////////
		final int severity = severityWriter > severityBooks ? severityWriter : severityBooks;
		final VDiagnostic diagnosticLibary = view.getDiagnostic();
		assertEquals("Validation result was not propagated to parent.", severity,
			diagnosticLibary.getHighestSeverity(), 0);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Util from here //
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////

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
		writer.setFirstName("Hans");
		writer.setLastName("Meyer");
		return writer;
	}

	private Writer createInfoWriter() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		writer.setFirstName("H");
		writer.setLastName("M");
		return writer;
	}

	private Writer createWarningWriter() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		writer.setFirstName("Hans");
		writer.setLastName("Hans");
		return writer;
	}

	private Writer createErrorWriter() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		return writer;
	}

	private Writer createCancelWriter() {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		writer.setFirstName("Offensive");
		writer.setLastName("Offensive");
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
		book.setTitle("Eine Chronik");
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
