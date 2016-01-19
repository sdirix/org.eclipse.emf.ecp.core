/*******************************************************************************
 * Copyright (c) 2011-2016 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.custom.model.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.custom.model.VCustomDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emfforms.spi.core.services.structuralchange.EMFFormsStructuralChangeTester;
import org.eclipse.emfforms.spi.core.services.structuralchange.StructuralChangeTesterInternal;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Tester for structural changes of CustomDMRs.
 *
 * @author Eugen Neufeld
 * @since 1.8
 *
 */
@Component
public class StructuralChangeCustomDMRTester implements StructuralChangeTesterInternal {

	private EMFFormsStructuralChangeTester emfFormsStructuralChangeTester;

	/**
	 * Called by the framework to set the {@link EMFFormsStructuralChangeTester}.
	 *
	 * @param emfFormsStructuralChangeTester The {@link EMFFormsStructuralChangeTester}
	 */

	@Reference
	protected void setEMFFormsStructuralChangeTester(EMFFormsStructuralChangeTester emfFormsStructuralChangeTester) {
		this.emfFormsStructuralChangeTester = emfFormsStructuralChangeTester;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.structuralchange.StructuralChangeTesterInternal#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
	 */
	@Override
	public double isApplicable(VDomainModelReference reference) {
		if (VCustomDomainModelReference.class.isInstance(reference)) {
			return 5;
		}
		return NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.structuralchange.StructuralChangeTesterInternal#isStructureChanged(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification)
	 */
	@Override
	public boolean isStructureChanged(VDomainModelReference reference, EObject domainRootObject,
		ModelChangeNotification notification) {
		final VCustomDomainModelReference customDMR = VCustomDomainModelReference.class.cast(reference);
		if (!customDMR.isControlChecked()) {
			return false;
		}
		boolean result = true;
		for (final VDomainModelReference dmr : customDMR.getDomainModelReferences()) {
			result &= emfFormsStructuralChangeTester.isStructureChanged(dmr, domainRootObject, notification);
		}
		return result;
	}

}
