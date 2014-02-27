/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.custom.model;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * A Tester for Hardcoded domain model references.
 * 
 * @author Eugen Neufeld
 * @since 1.2
 * 
 */
public abstract class HardcodedTester implements ECPApplicableTester {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.edit.provider.IItemPropertyDescriptor,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@Deprecated
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		// TODO Auto-generated method stub
		return NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public int isApplicable(EObject eObject, EStructuralFeature eStructuralFeature) {
		return NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
	 * @deprecated
	 */
	@Deprecated
	public int isApplicable(VDomainModelReference domainModelReference) {
		if (!VHardcodedDomainModelReference.class.isInstance(domainModelReference)) {
			return NOT_APPLICABLE;
		}
		final VHardcodedDomainModelReference hardcodedDomainModelReference = (VHardcodedDomainModelReference) domainModelReference;

		if (!hardcodedDomainModelReference.getControlId().equals(getId())) {
			return NOT_APPLICABLE;
		}
		return 5;
	}

	/**
	 * The id of the valid control.
	 * 
	 * @return the id as defined in the extension point
	 */
	protected abstract String getId();
}
