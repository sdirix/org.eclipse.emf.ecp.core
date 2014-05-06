/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
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

import java.util.Arrays;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextFactory;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.validation.test.model.Book;
import org.eclipse.emf.ecp.view.validation.test.model.Librarian;
import org.eclipse.emf.ecp.view.validation.test.model.Library;
import org.eclipse.emf.ecp.view.validation.test.model.TestFactory;
import org.eclipse.emf.ecp.view.validation.test.model.TestPackage;
import org.eclipse.emf.ecp.view.validation.test.model.Writer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author jfaltermeier
 *
 */
public class DynamicDMRTest {

	private static final String EMPTY = "";
	private static final String NAME_OK = "other";

	private VView view;
	private EObject domain;

	@Before
	public void before() {
		view = VViewFactory.eINSTANCE.createView();
	}

	@Ignore
	@Test
	public void testInitMissingContainmentElement() {
		// setup
		libraryAsDomain();
		addLibrarianNameControl();
		// act
		initService();
		// assert
		assertValidation(Diagnostic.OK, false, true);
	}

	@Ignore
	@Test
	public void testInitMissingReferencedElement() {
		// setup
		bookAsDomain();
		addWriterNameControl();
		// act
		initService();
		// assert
		assertValidation(Diagnostic.OK, false, true);
	}

	@Ignore
	@Test
	public void testRemoveContainmentElement() {
		// setup
		libraryAsDomain();
		addLibrarianNameControl();
		changeDomain(librarian(EMPTY));
		initService();
		assertValidation(Diagnostic.ERROR, true, false);
		// act
		changeDomain((Librarian) null);
		// assert
		assertValidation(Diagnostic.OK, false, true);
	}

	@Ignore
	@Test
	public void testRemoveReferencedElement() {
		// setup
		bookAsDomain();
		addWriterNameControl();
		changeDomain(writer(EMPTY));
		initService();
		assertValidation(Diagnostic.ERROR, true, false);
		// act
		changeDomain((Writer) null);
		// assert
		assertValidation(Diagnostic.OK, false, true);
	}

	@Ignore
	@Test
	public void testAddMissingContainmentElement() {
		// setup
		libraryAsDomain();
		addLibrarianNameControl();
		initService();
		assertValidation(Diagnostic.OK, false, true);
		// act
		changeDomain(librarian(EMPTY));
		// assert
		assertValidation(Diagnostic.ERROR, true, false);
	}

	@Ignore
	@Test
	public void testAddMissingReferencedElement() {
		// setup
		bookAsDomain();
		addWriterNameControl();
		initService();
		assertValidation(Diagnostic.OK, false, true);
		// act
		changeDomain(writer(EMPTY));
		// assert
		assertValidation(Diagnostic.ERROR, true, false);
	}

	@Ignore
	@Test
	public void testReplaceContainmentElement() {
		// setup
		libraryAsDomain();
		addLibrarianNameControl();
		changeDomain(librarian(EMPTY));
		initService();
		assertValidation(Diagnostic.ERROR, true, false);
		// act
		changeDomain(librarian(NAME_OK));
		// assert
		assertValidation(Diagnostic.OK, true, false);
	}

	@Ignore
	@Test
	public void testReplaceReferencedElement() {
		// setup
		bookAsDomain();
		addWriterNameControl();
		changeDomain(writer(EMPTY));
		initService();
		assertValidation(Diagnostic.ERROR, true, false);
		// act
		changeDomain(writer(NAME_OK));
		// assert
		assertValidation(Diagnostic.OK, true, false);
	}

	private void initService() {
		ViewModelContextFactory.INSTANCE.createViewModelContext(view, domain);
	}

	private void libraryAsDomain() {
		view.setRootEClass(TestPackage.eINSTANCE.getLibrary());
		domain = TestFactory.eINSTANCE.createLibrary();
	}

	private void bookAsDomain() {
		view.setRootEClass(TestPackage.eINSTANCE.getBook());
		domain = TestFactory.eINSTANCE.createBook();
	}

	private void addLibrarianNameControl() {
		final VControl control = VViewFactory.eINSTANCE.createControl();
		initControl(control, TestPackage.eINSTANCE.getLibrarian_Name(), TestPackage.eINSTANCE.getLibrary_Librarian());
		view.getChildren().add(control);
	}

	private void addWriterNameControl() {
		final VControl control = VViewFactory.eINSTANCE.createControl();
		initControl(control, TestPackage.eINSTANCE.getWriter_FirstName(), TestPackage.eINSTANCE.getBook_Writers());
		view.getChildren().add(control);
	}

	private void initControl(VControl control, EStructuralFeature feature, EReference... references) {
		control.setDomainModelReference(feature, Arrays.asList(references));
	}

	private Librarian librarian(String name) {
		final Librarian librarian = TestFactory.eINSTANCE.createLibrarian();
		librarian.setName(name);
		return librarian;
	}

	private Writer writer(String name) {
		final Writer writer = TestFactory.eINSTANCE.createWriter();
		writer.setFirstName(name);
		return writer;
	}

	private void changeDomain(Librarian librarian) {
		final Library library = (Library) domain;
		library.setLibrarian(librarian);
	}

	private void changeDomain(Writer writer) {
		final Book book = (Book) domain;
		book.setWriters(writer);
	}

	private void assertValidation(int severity, boolean enablement, boolean readOnly) {
		final VControl control = (VControl) view.getChildren().get(0);
		assertEquals(severity, control.getDiagnostic().getHighestSeverity());
		assertEquals(enablement, control.isEnabled());
		assertEquals(readOnly, control.isReadonly());
	}

}