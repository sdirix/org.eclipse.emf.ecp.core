/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mat Hansen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.common.internal.validation;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EValidator.SubstitutionLabelProvider;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emfforms.common.spi.validation.ValidationFilter;
import org.eclipse.emfforms.common.spi.validation.ValidationResultListener;
import org.eclipse.emfforms.common.spi.validation.ValidationService;
import org.eclipse.emfforms.common.spi.validation.Validator;
import org.eclipse.emfforms.common.spi.validation.exception.ValidationCanceledException;

/**
 * The implementation of {@link ValidationService}.
 *
 * @author Mat Hansen <mhansen@eclipsesource.com>
 */
public class ValidationServiceImpl implements ValidationService {

	private final Set<Validator> validators = new LinkedHashSet<Validator>();
	private final Set<ValidationFilter> validationFilters = new LinkedHashSet<ValidationFilter>();
	private final Set<ValidationResultListener> validationResultListeners = new LinkedHashSet<ValidationResultListener>();

	private SubstitutionLabelProvider substitutionLabelProvider;

	private boolean validationRunning;
	private boolean cancelationRequested;

	/**
	 * Default constructor.
	 */
	public ValidationServiceImpl() {
	}

	private boolean isFiltered(EObject eObject) {
		if (validationFilters.isEmpty()) {
			return false;
		}
		for (final ValidationFilter filter : validationFilters) {
			if (filter.skipValidation(eObject)) {
				return true;
			}
		}
		return false;
	}

	private boolean isIgnored(EObject eObject, Diagnostic diagnostic) {
		if (validationFilters.isEmpty()) {
			return false;
		}
		for (final ValidationFilter filter : validationFilters) {
			if (filter.ignoreDiagnostic(eObject, diagnostic)) {
				return true;
			}
		}
		return false;
	}

	private Diagnostic doValidate(EObject eObject) {

		Diagnostic diagnostic = null;
		try {

			diagnostic = getDiagnosticForEObject(eObject);
			return diagnostic;

		} finally {
			invokeValidationResultListeners(eObject, diagnostic);
		}

	}

	@Override
	public Diagnostic validate(EObject eObject) {
		if (isFiltered(eObject)) {
			return null;
		}
		return doValidate(eObject);
	}

	@Override
	public Set<Diagnostic> validate(Iterator<EObject> eObjects) throws ValidationCanceledException {

		final Set<Diagnostic> diagnostics = new LinkedHashSet<Diagnostic>();
		if (validationRunning) {
			return diagnostics; // prevent re-entry
		}

		validationRunning = true;
		try {
			while (!cancelationRequested && eObjects.hasNext()) {

				final EObject eObject = eObjects.next();
				if (isFiltered(eObject)) {
					continue;
				}

				final Diagnostic diagnostic = doValidate(eObject);

				if (isIgnored(eObject, diagnostic)) {
					continue;
				}
			}
		} finally {
			validationRunning = false;
		}

		if (cancelationRequested) {
			throw new ValidationCanceledException();
		}
		return diagnostics;
	}

	private void invokeValidationResultListeners(EObject eObject, Diagnostic diagnostic) {
		if (validationResultListeners.isEmpty()) {
			return;
		}

		for (final ValidationResultListener listener : validationResultListeners) {
			listener.onValidate(eObject, diagnostic);
		}
	}

	protected EValidator getEValidatorForEObject(EObject object) {
		final EValidator eValidator = EValidator.Registry.INSTANCE.getEValidator(object.eClass().getEPackage());

		if (eValidator == null) {
			return new EObjectValidator();
		}
		return eValidator;
	}

	/**
	 * Computes the {@link Diagnostic} for the given eObject.
	 *
	 * @param object the eObject to validate
	 * @return the diagnostic
	 */
	protected Diagnostic getDiagnosticForEObject(EObject object) {

		final EValidator eValidator = getEValidatorForEObject(object);
		final BasicDiagnostic diagnostic = Diagnostician.INSTANCE.createDefaultDiagnostic(object);
		final Map<Object, Object> context = new LinkedHashMap<Object, Object>();
		context.put(EValidator.class, eValidator);
		if (substitutionLabelProvider != null) {
			context.put(EValidator.SubstitutionLabelProvider.class, substitutionLabelProvider);
		}

		eValidator.validate(object, diagnostic, context);

		final Map<EStructuralFeature, DiagnosticChain> diagnosticMap = new LinkedHashMap<EStructuralFeature, DiagnosticChain>();
		for (final Diagnostic child : diagnostic.getChildren()) {
			if (DiagnosticChain.class.isInstance(child) && DiagnosticHelper.checkDiagnosticData(child)) {
				diagnosticMap.put(DiagnosticHelper.getEStructuralFeature(child.getData()),
					(DiagnosticChain) child);
			}
		}

		for (final Validator validator : validators) {
			final List<Diagnostic> additionValidation = validator.validate(object);
			if (additionValidation == null) {
				continue;
			}
			for (final Diagnostic additionDiagnostic : additionValidation) {
				if (diagnosticMap
					.containsKey(DiagnosticHelper.getEStructuralFeature(additionDiagnostic.getData()))) {
					diagnosticMap.get(DiagnosticHelper.getEStructuralFeature(additionDiagnostic.getData()))
						.add(additionDiagnostic);
				} else {
					diagnostic.add(additionDiagnostic);
				}

			}
		}
		return diagnostic;
	}

	@Override
	public void addValidator(Validator validator) {
		validators.add(validator);
	}

	@Override
	public void removeValidator(Validator validator) {
		validators.remove(validator);
	}

	@Override
	public void registerValidationFilter(ValidationFilter filter) {
		validationFilters.add(filter);
	}

	@Override
	public void unregisterValidationFilter(ValidationFilter filter) {
		validationFilters.remove(filter);
	}

	@Override
	public void registerValidationResultListener(ValidationResultListener listener) {
		validationResultListeners.add(listener);
	}

	@Override
	public void unregisterValidationResultListener(ValidationResultListener listener) {
		validationResultListeners.remove(listener);
	}

	@Override
	public void setSubstitutionLabelProvider(SubstitutionLabelProvider substitutionLabelProvider) {
		this.substitutionLabelProvider = substitutionLabelProvider;
	}

	@Override
	public boolean isBusy() {
		return validationRunning;
	}

	@Override
	public void cancel() {
		cancelationRequested = true;
	}

}
