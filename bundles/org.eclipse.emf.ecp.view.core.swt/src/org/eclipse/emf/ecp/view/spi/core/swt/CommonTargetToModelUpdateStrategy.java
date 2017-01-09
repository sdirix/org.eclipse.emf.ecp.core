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
package org.eclipse.emf.ecp.view.spi.core.swt;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.spi.model.VDiagnostic;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emfforms.spi.common.converter.ITargetToModelConverter;
import org.eclipse.emfforms.spi.common.validation.PreSetValidationService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * A common {@link EMFUpdateValueStrategy} that implements {@link #validateBeforeSet(Object)}.
 *
 * @since 1.12
 */
public class CommonTargetToModelUpdateStrategy extends EMFUpdateValueStrategy {

	private final EStructuralFeature eStructuralFeature;
	private final VElement vElement;
	private ITargetToModelConverter conversion;

	/**
	 * Constructor.
	 *
	 * @param vElement the {@link VElement}
	 * @param eStructuralFeature an {@link EStructuralFeature} that defines any validation constraints
	 */
	public CommonTargetToModelUpdateStrategy(VElement vElement, EStructuralFeature eStructuralFeature) {
		super(POLICY_UPDATE);
		this.vElement = vElement;
		this.eStructuralFeature = eStructuralFeature;
	}

	/**
	 * Constructor.
	 *
	 * @param vElement the {@link VElement}
	 * @param eStructuralFeature an {@link EStructuralFeature} that defines any validation constraints
	 * @param updatePolicy the update policy
	 */
	public CommonTargetToModelUpdateStrategy(VElement vElement, EStructuralFeature eStructuralFeature,
		int updatePolicy) {
		super(updatePolicy);
		this.vElement = vElement;
		this.eStructuralFeature = eStructuralFeature;
	}

	/**
	 * Constructor.
	 *
	 * @param vElement the {@link VElement}
	 * @param eStructuralFeature an {@link EStructuralFeature} that defines any validation constraints
	 * @param conversion a conversion that should take place during the target to model phase
	 */
	public CommonTargetToModelUpdateStrategy(VElement vElement, EStructuralFeature eStructuralFeature,
		ITargetToModelConverter conversion) {
		super(POLICY_UPDATE);
		this.vElement = vElement;
		this.eStructuralFeature = eStructuralFeature;
		this.conversion = conversion;
	}

	/**
	 * Constructor.
	 *
	 * @param vElement the {@link VElement}
	 * @param eStructuralFeature an {@link EStructuralFeature} that defines any validation constraints
	 * @param conversion a conversion that should take place during the target to model phase
	 * @param updatePolicy the update policy
	 */
	public CommonTargetToModelUpdateStrategy(VElement vElement, EStructuralFeature eStructuralFeature,
		ITargetToModelConverter conversion, int updatePolicy) {
		super(updatePolicy);
		this.vElement = vElement;
		this.eStructuralFeature = eStructuralFeature;
		this.conversion = conversion;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.databinding.UpdateValueStrategy#validateBeforeSet(java.lang.Object)
	 */
	@Override
	public IStatus validateBeforeSet(Object value) {
		final BundleContext bundleContext = FrameworkUtil
			.getBundle(getClass())
			.getBundleContext();
		final ServiceReference<PreSetValidationService> serviceReference = bundleContext
			.getServiceReference(PreSetValidationService.class);

		if (serviceReference == null) {
			return super.validateBeforeSet(value);
		}

		try {

			final PreSetValidationService service = bundleContext.getService(serviceReference);

			if (service == null) {
				return super.validateBeforeSet(value);
			}

			final Diagnostic result = service.validate(eStructuralFeature, value);

			if (result.getSeverity() == Diagnostic.OK) {
				return Status.OK_STATUS;
			}

			final VDiagnostic vDiagnostic = VViewFactory.eINSTANCE.createDiagnostic();
			vDiagnostic.getDiagnostics().add(result);
			vElement.setDiagnostic(vDiagnostic);
			return BasicDiagnostic.toIStatus(result);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.databinding.UpdateValueStrategy#convert(java.lang.Object)
	 */
	@Override
	public Object convert(Object value) {
		if (conversion != null) {
			return conversion.convert(value);
		}
		return super.convert(value);
	}

	/**
	 * Returns the {@link EStructuralFeature} that defines any validation constraints.
	 *
	 * @return the structural feature
	 */
	public EStructuralFeature getStructuralFeature() {
		return eStructuralFeature;
	}

	/**
	 * Returns the associated {@link VElement}.
	 *
	 * @return the {@link VElement}
	 */
	public VElement getVElement() {
		return vElement;
	}
}
