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
package org.eclipse.emf.ecp.view.custom.ui.swt.test;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class CustomControlStubTester implements ECPApplicableTester {

	public CustomControlStubTester() {
		// TODO Auto-generated constructor stub
	}

	@Deprecated
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		return NOT_APPLICABLE;
	}

	public int isApplicable(VDomainModelReference domainModelReference) {
		// TODO Auto-generated method stub
		return !domainModelReference.getIterator().hasNext() ? 1 : NOT_APPLICABLE;
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
}
