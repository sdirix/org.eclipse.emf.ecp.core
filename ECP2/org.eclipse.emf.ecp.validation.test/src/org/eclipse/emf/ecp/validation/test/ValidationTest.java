package org.eclipse.emf.ecp.validation.test;

import static org.junit.Assert.*;

import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
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
		
		Writer writer=TestFactory.eINSTANCE.createWriter();
		IValidationService validationService= validationServiceProvider.getValidationService(writer);
		
		validationService.validate(writer);
		Diagnostic diagnostic= validationService.getDiagnostic(writer);
		assertEquals(Diagnostic.ERROR, diagnostic.getSeverity());
		assertEquals(1, diagnostic.getChildren().size());
		assertEquals(2,diagnostic.getChildren().get(0).getData().size());
		assertEquals(TestPackage.eINSTANCE.getWriter_FirstName(),diagnostic.getChildren().get(0).getData().get(1));
	}
	@Test
	public void testCorrectValidation(){
		Writer writer=TestFactory.eINSTANCE.createWriter();
		IValidationService validationService= validationServiceProvider.getValidationService(writer);
		writer.setFirstName("Test");
		validationService.validate(writer);
		Diagnostic diagnostic= validationService.getDiagnostic(writer);
		assertEquals(Diagnostic.OK, diagnostic.getSeverity());
	}
	@Test
	public void testPropagation(){
		Library library=TestFactory.eINSTANCE.createLibrary();
		Writer writer=TestFactory.eINSTANCE.createWriter();
		library.setName("TesLib");
		library.getWriters().add(writer);
		
		IValidationService validationService= validationServiceProvider.getValidationService(library);
		
		Set<EObject> affectedElements=validationService.validate(writer);
		assertEquals(1, affectedElements.size());
		
		Diagnostic diagnosticWriter= validationService.getDiagnostic(writer);
		assertEquals(Diagnostic.ERROR, diagnosticWriter.getSeverity());
		
		Diagnostic diagnosticLib= validationService.getDiagnostic(library);
		assertEquals(Diagnostic.ERROR, diagnosticLib.getSeverity());
		
		assertEquals(1, diagnosticLib.getChildren().size());
		assertEquals(2,diagnosticLib.getChildren().get(0).getData().size());
		assertEquals(TestPackage.eINSTANCE.getWriter_FirstName(),diagnosticLib.getChildren().get(0).getData().get(1));
	}

}
