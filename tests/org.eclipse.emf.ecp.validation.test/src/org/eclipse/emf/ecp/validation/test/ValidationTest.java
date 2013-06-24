package org.eclipse.emf.ecp.validation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.validation.api.IValidationService;
import org.eclipse.emf.ecp.validation.api.IValidationServiceProvider;
import org.eclipse.emf.ecp.validation.test.test.Library;
import org.eclipse.emf.ecp.validation.test.test.TestFactory;
import org.eclipse.emf.ecp.validation.test.test.TestPackage;
import org.eclipse.emf.ecp.validation.test.test.Writer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ValidationTest {

	private static IValidationServiceProvider validationServiceProvider;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		validationServiceProvider = Activator.getValidationService();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSingleObject() {

		Writer writer = TestFactory.eINSTANCE.createWriter();
		IValidationService validationService = validationServiceProvider.getValidationService(writer);

		validationService.validate(writer);
		Diagnostic diagnostic = validationService.getDiagnostic(writer);
		assertEquals(Diagnostic.ERROR, diagnostic.getSeverity());
		assertEquals(1, diagnostic.getChildren().size());
		assertEquals(2, diagnostic.getChildren().get(0).getData().size());
		assertEquals(TestPackage.eINSTANCE.getWriter_FirstName(), diagnostic.getChildren().get(0).getData().get(1));
	}

	@Test
	public void testCorrectValidation() {
		Writer writer = TestFactory.eINSTANCE.createWriter();
		IValidationService validationService = validationServiceProvider.getValidationService(writer);
		writer.setFirstName("Test");
		validationService.validate(writer);
		Diagnostic diagnostic = validationService.getDiagnostic(writer);
		assertEquals(Diagnostic.OK, diagnostic.getSeverity());
	}

	@Test
	public void testPropagation() {
		Library library = TestFactory.eINSTANCE.createLibrary();
		Writer writer = TestFactory.eINSTANCE.createWriter();
		library.setName("TesLib");
		library.getWriters().add(writer);

		IValidationService validationService = validationServiceProvider.getValidationService(library);

		Set<EObject> affectedElements = validationService.validate(writer);
		assertEquals(1, affectedElements.size());

		Diagnostic diagnosticWriter = validationService.getDiagnostic(writer);
		assertEquals(Diagnostic.ERROR, diagnosticWriter.getSeverity());

		Diagnostic diagnosticLib = validationService.getDiagnostic(library);
		assertEquals(Diagnostic.ERROR, diagnosticLib.getSeverity());

		assertEquals(1, diagnosticLib.getChildren().size());
		assertEquals(2, diagnosticLib.getChildren().get(0).getData().size());
		assertEquals(TestPackage.eINSTANCE.getWriter_FirstName(), diagnosticLib.getChildren().get(0).getData().get(1));
	}

	@Test
	public void testMultipleObjects() {
		Writer writer1 = TestFactory.eINSTANCE.createWriter();
		writer1.setFirstName("Hans");
		Writer writer2 = TestFactory.eINSTANCE.createWriter();
		Writer writer3 = TestFactory.eINSTANCE.createWriter();

		Library lib = TestFactory.eINSTANCE.createLibrary();
		lib.setName("Bücherei");
		lib.getWriters().add(writer1);
		lib.getWriters().add(writer2);
		lib.getWriters().add(writer3);

		Set<EObject> writers = new HashSet<EObject>();
		writers.add(writer1);
		writers.add(writer2);

		IValidationService validationService = validationServiceProvider.getValidationService(lib);
		validationService.validate(writers);

		Diagnostic diagnosticW1 = validationService.getDiagnostic(writer1);
		assertEquals(Diagnostic.OK, diagnosticW1.getSeverity());

		Diagnostic diagnosticW2 = validationService.getDiagnostic(writer2);
		assertEquals(Diagnostic.ERROR, diagnosticW2.getSeverity());
		assertEquals(1, diagnosticW2.getChildren().size());
		assertEquals(2, diagnosticW2.getChildren().get(0).getData().size());
		assertEquals(TestPackage.eINSTANCE.getWriter_FirstName(), diagnosticW2.getChildren().get(0).getData().get(1));

		Diagnostic diagnosticW3 = validationService.getDiagnostic(writer3);
		// TODO add correct assert
		// fail("When there is no (cached) value for a object you want to get a diagnostic for, "
		// + "returning OK may not be the best idea?! Return CANCEL maybe? discuss");
		assertEquals(Diagnostic.OK, diagnosticW3.getSeverity());
	}

	@Test
	public void testRootDiagnostic() {
		Writer writer1 = TestFactory.eINSTANCE.createWriter();
		Writer writer2 = TestFactory.eINSTANCE.createWriter();
		writer2.setFirstName("Hans");
		Library library = TestFactory.eINSTANCE.createLibrary();
		library.setName("Bücherei");
		library.getWriters().add(writer1);
		library.getWriters().add(writer2);

		Set<EObject> collection = new HashSet<EObject>();
		collection.add(library);
		collection.add(writer1);
		collection.add(writer2);

		IValidationService validationService = validationServiceProvider.getValidationService(library);
		validationService.validate(collection);

		Diagnostic diagnostic = validationService.getRootDiagnostic();
		assertEquals(Diagnostic.ERROR, diagnostic.getSeverity());

		writer1.setFirstName("Sepp");
		validationService.validate(collection);
		diagnostic = validationService.getRootDiagnostic();
		assertEquals(Diagnostic.OK, diagnostic.getSeverity());

		// warning when firstname is same as lastname
		writer1.setLastName("Sepp");
		validationService.validate(collection);
		diagnostic = validationService.getRootDiagnostic();
		assertEquals(Diagnostic.WARNING, diagnostic.getSeverity());

		writer2.setFirstName("");
		validationService.validate(collection);
		diagnostic = validationService.getRootDiagnostic();
		assertEquals(Diagnostic.ERROR, diagnostic.getSeverity());
	}

	@Test
	public void testECPProject() {
		try {
			ECPProject project = ECPUtil.getECPProjectManager().createProject(
				ECPUtil.getECPProviderRegistry().getProvider(EMFStoreProvider.NAME), "Project");

			Writer correctWriter = TestFactory.eINSTANCE.createWriter();
			correctWriter.setFirstName("Hans");
			Writer errorWriter = TestFactory.eINSTANCE.createWriter();

			project.getContents().add(errorWriter);
			project.getContents().add(correctWriter);

			IValidationService validationService = validationServiceProvider.getValidationService(project);

			validationService.validate(correctWriter);
			assertEquals(Diagnostic.OK, validationService.getDiagnostic(correctWriter).getSeverity());

			validationService.validate(errorWriter);
			Diagnostic diagnostic = validationService.getDiagnostic(errorWriter);
			assertEquals(Diagnostic.ERROR, diagnostic.getSeverity());
			assertEquals(1, diagnostic.getChildren().size());
			assertEquals(2, diagnostic.getChildren().get(0).getData().size());
			assertEquals(TestPackage.eINSTANCE.getWriter_FirstName(), diagnostic.getChildren().get(0).getData().get(1));

		} catch (ECPProjectWithNameExistsException ex) {
		}
	}

	@Test
	public void testAlreadyInMapping() {
		Writer writer = TestFactory.eINSTANCE.createWriter();
		IValidationService validationService1 = validationServiceProvider.getValidationService(writer);
		IValidationService validationService2 = validationServiceProvider.getValidationService(writer);
		assertEquals(validationService1, validationService2);

		validationServiceProvider.deleteValidationService(writer);
		IValidationService validationService3 = validationServiceProvider.getValidationService(writer);
		assertTrue(!validationService3.equals(validationService1));
	}
}
