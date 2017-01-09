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
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.Diagnostician;
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

	private Map<EDataType, Set<IFeatureConstraint>> extensions = new LinkedHashMap<EDataType, Set<IFeatureConstraint>>();

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.common.validation.IFeatureConstraint#validate(org.eclipse.emf.ecore.EStructuralFeature,
	 *      java.lang.Object)
	 */
	@Override
	public Diagnostic validate(EStructuralFeature eStructuralFeature, Object value) {

		final EClassifier eType = eStructuralFeature.getEType();
		if (!EDataType.class.isInstance(eType)) {
			return new BasicDiagnostic();
		}

		final EDataType eDataType = EDataType.class.cast(eType);
		final EValidator validator = EValidator.Registry.INSTANCE.getEValidator(eDataType.getEPackage());
		final BasicDiagnostic diagnostics = Diagnostician.INSTANCE.createDefaultDiagnostic(eDataType, value);

		if (validator != null) {
			final Map<Object, Object> context = new LinkedHashMap<Object, Object>();
			validator.validate(eDataType, value, diagnostics, context);
		}

		final Set<IFeatureConstraint> featureConstraints = extensions.get(eType);
		if (featureConstraints != null) {
			for (final IFeatureConstraint constraint : featureConstraints) {
				final Diagnostic result = constraint.validate(eStructuralFeature, value);
				if (result.getSeverity() == Diagnostic.OK) {
					continue;
				}
				diagnostics.add(result);
			}
		}

		return diagnostics;
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
}
