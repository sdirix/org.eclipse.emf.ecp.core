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
import org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;

public class CustomControlStub2Tester implements ECPApplicableTester {

	public CustomControlStub2Tester() {
		// TODO Auto-generated constructor stub
	}

	@Deprecated
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int isApplicable(VDomainModelReference domainModelReference) {
		if (!VFeaturePathDomainModelReference.class.isInstance(domainModelReference)) {
			return NOT_APPLICABLE;
		}
		final VFeaturePathDomainModelReference modelReference = (VFeaturePathDomainModelReference) domainModelReference;
		if (modelReference.getDomainModelEFeature() != BowlingPackage.eINSTANCE.getMerchandise_Name()) {
			return NOT_APPLICABLE;
		}
		if (modelReference.getDomainModelEReferencePath().size() != 1) {
			return NOT_APPLICABLE;
		}
		if (modelReference.getDomainModelEReferencePath().get(0) != BowlingPackage.eINSTANCE
			.getFan_FavouriteMerchandise()) {
			return NOT_APPLICABLE;
		}
		return 2;
	}

}
