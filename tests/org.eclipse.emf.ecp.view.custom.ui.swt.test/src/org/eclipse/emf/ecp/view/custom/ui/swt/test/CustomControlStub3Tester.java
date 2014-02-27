/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * jfaltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.custom.ui.swt.test;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;

/**
 * @author jfaltermeier
 * 
 */
public class CustomControlStub3Tester implements ECPApplicableTester {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.edit.provider.IItemPropertyDescriptor,
	 *      org.eclipse.emf.ecore.EObject)
	 * @deprecated
	 */
	@Deprecated
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
	 */
	public int isApplicable(VDomainModelReference domainModelReference) {
		if (!VFeaturePathDomainModelReference.class.isInstance(domainModelReference)) {
			return NOT_APPLICABLE;
		}
		final VFeaturePathDomainModelReference modelReference = (VFeaturePathDomainModelReference) domainModelReference;
		if (modelReference.getDomainModelEFeature() != BowlingPackage.eINSTANCE.getLeague_Players()) {
			return NOT_APPLICABLE;
		}
		if (modelReference.getDomainModelEReferencePath().size() != 0) {
			return NOT_APPLICABLE;
		}
		return 2;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public int isApplicable(EObject eObject, EStructuralFeature eStructuralFeature) {
		// TODO Auto-generated method stub
		return 0;
	}

}
