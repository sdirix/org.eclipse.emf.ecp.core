/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.internal.validation.PreSetValidationServiceImpl;
import org.eclipse.emf.ecp.view.validation.test.model.Gender;
import org.eclipse.emf.ecp.view.validation.test.model.TestPackage;
import org.eclipse.emfforms.spi.common.validation.IFeatureConstraint;
import org.junit.Test;

public class PreSetValidationService_Test {

	private final PreSetValidationServiceImpl service = new PreSetValidationServiceImpl();

	@Test
	public void violateMaxLength() {
		final Diagnostic result = service.validate(
			TestPackage.eINSTANCE.getPerson_FirstName(),
			"more than 10 chars");
		assertEquals(result.getSeverity(), Diagnostic.ERROR);
	}

	@Test
	public void maxLength() {
		final Diagnostic result = service.validate(
			TestPackage.eINSTANCE.getPerson_FirstName(),
			"valid");
		assertEquals(result.getSeverity(), Diagnostic.OK);
	}

	@Test
	public void enums() {
		final Diagnostic result = service.validate(
			TestPackage.eINSTANCE.getPerson_Gender(),
			Gender.MALE);
		assertEquals(result.getSeverity(), Diagnostic.OK);
	}

	@Test
	public void pattern() {
		final Diagnostic result = service.validate(
			TestPackage.eINSTANCE.getPerson_LastName(), "VALID");
		assertEquals(result.getSeverity(), Diagnostic.OK);
	}

	@Test
	public void invalidPattern() {
		final Diagnostic result = service.validate(
			TestPackage.eINSTANCE.getPerson_LastName(), "invalid");
		assertEquals(result.getSeverity(), Diagnostic.ERROR);
	}

	@Test
	public void validEnum() {
		final Diagnostic result = service.validate(TestPackage.eINSTANCE.getPerson_Gender(), "Male");
		assertEquals(result.getSeverity(), Diagnostic.OK);
	}

	@Test
	public void invalidEnum() {
		final Diagnostic result = service.validate(TestPackage.eINSTANCE.getPerson_Gender(), "Mal");
		assertEquals(result.getSeverity(), Diagnostic.ERROR);
	}

	@Test
	public void custom() {
		final PreSetValidationServiceImpl s = new PreSetValidationServiceImpl();
		s.addConstraintValidator(TestPackage.eINSTANCE.getCustomDataType(), new IFeatureConstraint() {
			@Override
			public Diagnostic validate(EStructuralFeature eStructuralFeature, Object value) {
				final EClassifier eType = eStructuralFeature.getEType();

				if (!TestPackage.eINSTANCE.getCustomDataType().isInstance(eType)
					|| value.equals("FOO")) {
					return new BasicDiagnostic();
				}

				return BasicDiagnostic.toDiagnostic(
					new Status(IStatus.ERROR, "", IStatus.ERROR, "Value is not FOO", null));
			}
		});

		final Diagnostic result = s.validate(
			TestPackage.eINSTANCE.getPerson_Custom(), "FOO");
		assertEquals(result.getSeverity(), Diagnostic.OK);
	}

	@Test
	public void invalidCustom() {
		final PreSetValidationServiceImpl s = new PreSetValidationServiceImpl();
		s.addConstraintValidator(TestPackage.eINSTANCE.getCustomDataType(), new IFeatureConstraint() {
			@Override
			public Diagnostic validate(EStructuralFeature eStructuralFeature, Object value) {

				if (value.equals("FOO")) {
					return new BasicDiagnostic();
				}

				return BasicDiagnostic.toDiagnostic(
					new Status(IStatus.ERROR, "test", IStatus.ERROR, "Value is not FOO", null));
			}
		});

		final Diagnostic result = s.validate(
			TestPackage.eINSTANCE.getPerson_Custom(), "BAR");
		assertEquals(result.getSeverity(), Diagnostic.ERROR);
	}
}
