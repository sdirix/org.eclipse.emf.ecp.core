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
package org.eclipse.emf.ecp.view.internal.validation;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.util.EObjectValidator.DynamicEDataTypeValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.common.validation.IFeatureConstraint;
import org.eclipse.emfforms.spi.common.validation.PreSetValidationService;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * Implementation of the {@link PreSetValidationService}.
 *
 * @author emueller
 *
 */
@Component(name = "PreSetValidationServiceImpl", service = PreSetValidationService.class)
public class PreSetValidationServiceImpl implements PreSetValidationService {

	private static final String LOOSE_PATTERN_KEY = "loosePattern"; //$NON-NLS-1$
	private static final String LOOSE_MIN_LENGTH = "looseMinLength"; //$NON-NLS-1$
	private static final String MULTI_LITERAL_SEP = "|"; //$NON-NLS-1$
	private static final String ESCAPED_MULTI_LITERAL_SEP = "\\|"; //$NON-NLS-1$
	private Map<EDataType, Set<IFeatureConstraint>> extensions = new LinkedHashMap<EDataType, Set<IFeatureConstraint>>();

	@Override
	public Diagnostic validate(final EStructuralFeature eStructuralFeature, Object value) {
		return validate(eStructuralFeature, value, null);
	}

	@Override
	public Diagnostic validate(final EStructuralFeature eStructuralFeature, Object value, Map<Object, Object> context) {
		return validate(
			new PreSetValidator() {
				@Override
				public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics,
					Map<Object, Object> context) {
					final EValidator validator = EValidator.Registry.INSTANCE
						.getEValidator(eStructuralFeature.getEType().getEPackage());
					return validator.validate(eDataType, value, diagnostics, context);
				}
			},
			eStructuralFeature,
			value,
			context);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.common.validation.PreSetValidationService#validateLoose(org.eclipse.emf.ecore.EStructuralFeature,
	 *      java.lang.Object)
	 */
	@Override
	public Diagnostic validateLoose(EStructuralFeature eStructuralFeature, Object value) {
		final EClassifier eType = eStructuralFeature.getEType();

		if (!EDataType.class.isInstance(eType) || EDataType.class.cast(eType).getEPackage() == EcorePackage.eINSTANCE) {
			return new BasicDiagnostic();
		}

		return validate(
			new DynamicLoosePatternEValidator(new LooseEValidator(), (EDataType) eType),
			eStructuralFeature,
			value,
			null);
	}

	@SuppressWarnings("unchecked")
	private Diagnostic validate(PreSetValidator validator, EStructuralFeature eStructuralFeature, Object value,
		Map<Object, Object> context) {
		final EClassifier eType = eStructuralFeature.getEType();

		if (!EDataType.class.isInstance(eType) || EDataType.class.cast(eType).getEPackage() == EcorePackage.eINSTANCE) {
			return new BasicDiagnostic();
		}

		final EDataType eDataType = EDataType.class.cast(eType);

		final BasicDiagnostic diagnostics = Diagnostician.INSTANCE.createDefaultDiagnostic(eDataType, value);

		// try to validate given value as enum literal
		if (eDataType instanceof EEnum && value instanceof String) {
			if (validateEEnum((EEnum) eDataType, (String) value)) {
				return new BasicDiagnostic();
			}
		} else if (eDataType instanceof EEnum && value instanceof List<?>) {
			try {
				if (validateEEnum((EEnum) eDataType, (List<Enumerator>) value)) {
					return new BasicDiagnostic();
				}
			} catch (final ClassCastException ex) {
				// ignore and continue with regular validation
			}
		}

		if (validator != null) {
			validator.validate(eDataType, value, diagnostics, context);
		}

		final Set<IFeatureConstraint> featureConstraints = extensions.get(eType);
		if (featureConstraints != null) {
			for (final IFeatureConstraint constraint : featureConstraints) {
				final Diagnostic result = constraint.validate(eStructuralFeature, value, context);
				if (result.getSeverity() == Diagnostic.OK) {
					continue;
				}
				diagnostics.add(result);
			}
		}

		return diagnostics;
	}

	private static boolean validateEEnum(final EEnum eEnum, final String valueString) {
		if (valueString.contains(MULTI_LITERAL_SEP)) {
			boolean isValid = true;
			final String[] literals = valueString.split(ESCAPED_MULTI_LITERAL_SEP);
			for (final String literal : literals) {
				isValid &= validateLiteral(eEnum, literal.trim());
			}
			return isValid;
		}
		return validateLiteral(eEnum, valueString);
	}

	private static boolean validateEEnum(final EEnum eEnum, final List<Enumerator> enumerators) {
		boolean isValid = true;
		for (final Enumerator enumerator : enumerators) {
			isValid &= validateLiteral(eEnum, enumerator.getLiteral());
		}
		return isValid;
	}

	private static Optional<String> findLooseConstraint(EDataType eDataType, String looseConstrainKey) {
		return Optional
			.ofNullable(EcoreUtil.getAnnotation(eDataType, ExtendedMetaData.ANNOTATION_URI, looseConstrainKey));
	}

	private static boolean validateLiteral(EEnum eEnum, String literal) {
		for (final EEnumLiteral enumLiteral : eEnum.getELiterals()) {
			if (literal.equals(enumLiteral.getLiteral())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void addConstraintValidator(EDataType eDataType, IFeatureConstraint extension) {
		if (!extensions.containsKey(eDataType)) {
			extensions.put(eDataType, new LinkedHashSet<IFeatureConstraint>());
		}
		extensions.get(eDataType).add(extension);
	}

	/**
	 * Called by the framework when the component gets activated.
	 *
	 * @param bundleContext The {@link BundleContext}
	 */
	@Activate
	protected void activate(BundleContext bundleContext) {
		extensions = new LinkedHashMap<EDataType, Set<IFeatureConstraint>>();
	}

	/**
	 * Called by the framework when the component gets deactivated.
	 *
	 * @param bundleContext The {@link BundleContext}
	 */
	@Deactivate
	protected void deactivate(BundleContext bundleContext) {
		extensions = null;
	}

	/**
	 * An {@link EObjectValidator} that considers loose constraints of any annotation details entry.
	 *
	 */
	class LooseEValidator extends EObjectValidator implements PreSetValidator {
		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecore.util.EObjectValidator#validatePattern(org.eclipse.emf.ecore.EDataType,
		 *      java.lang.Object, org.eclipse.emf.ecore.EValidator.PatternMatcher[][],
		 *      org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
		 */
		@Override
		protected boolean validatePattern(EDataType eDataType, Object value, PatternMatcher[][] patterns,
			DiagnosticChain diagnostics, Map<Object, Object> context) {

			final Optional<String> loosePattern = findLooseConstraint(eDataType, LOOSE_PATTERN_KEY);

			if (loosePattern.isPresent()) {
				return super.validatePattern(eDataType, value, new PatternMatcher[][] {
					{
						new PatternMatcher() {
							@Override
							public boolean matches(String value) {
								final Pattern pattern = Pattern.compile(loosePattern.get());
								final Matcher matcher = pattern.matcher(value);
								return matcher.matches();
							}
						}
					}
				}, diagnostics, context);
			}

			return super.validatePattern(eDataType, value, patterns, diagnostics, context);
		}
	}

	/**
	 * An {@link DynamicEDataTypeValidator} that considers loose constraints of any annotation details entry.
	 *
	 */
	class DynamicLoosePatternEValidator extends DynamicEDataTypeValidator implements PreSetValidator {

		/**
		 * Constructor.
		 *
		 * @param eObjectValidator an instance of an {@link EObjectValidator}
		 * @param eDataType the {@link EDataType} to be validated
		 */
		DynamicLoosePatternEValidator(EObjectValidator eObjectValidator, EDataType eDataType) {
			eObjectValidator.super(eDataType);
		}

		/**
		 * {@inheritDoc}
		 *
		 * @see org.eclipse.emf.ecore.util.EObjectValidator.DynamicEDataTypeValidator#validateSchemaConstraints(org.eclipse.emf.ecore.EDataType,
		 *      java.lang.Object, org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
		 */
		@Override
		protected boolean validateSchemaConstraints(EDataType eDataType, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {

			final Optional<String> looseMinLength = findLooseConstraint(eDataType, LOOSE_MIN_LENGTH);

			if (looseMinLength.isPresent()) {
				effectiveMinLength = Integer.parseInt(looseMinLength.get());
				super.validateSchemaConstraints(eDataType, value, diagnostics, context);
			}

			return super.validateSchemaConstraints(eDataType, value, diagnostics, context);
		}
	}
}
