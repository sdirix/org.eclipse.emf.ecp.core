package org.eclipse.emf.ecp.validation.test;

import static org.junit.Assert.*;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecp.validation.api.IValidationService;
import org.eclipse.emf.ecp.validation.api.IValidationServiceProvider;
import org.eclipse.emf.ecp.validation.test.test.TestFactory;
import org.eclipse.emf.ecp.validation.test.test.TestPackage;
import org.eclipse.emf.ecp.validation.test.test.Writer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ValidationTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
		IValidationServiceProvider validationServiceProvider= Activator.getValidationService();
		Writer writer=TestFactory.eINSTANCE.createWriter();
		IValidationService validationService= validationServiceProvider.getValidationService(writer);
		
		validationService.validate(writer);
		Diagnostic diagnostic= validationService.getDiagnostic(writer);
		assertEquals(Diagnostic.ERROR, diagnostic.getSeverity());
		assertEquals(1, diagnostic.getChildren().size());
		assertEquals(2,diagnostic.getChildren().get(0).getData().size());
		assertEquals(TestPackage.eINSTANCE.getWriter_FirstName(),diagnostic.getChildren().get(0).getData().get(1));
	}

}
