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
package org.eclipse.emf.ecp.view.editor.controls;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.util.ECPApplicableTester;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * {@link ECPApplicableTester} for checking the {@link RuleFeaturePathControl}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class RuleFeaturePathTester implements ECPApplicableTester {

	/**
	 * Default constrcutor.
	 */
	public RuleFeaturePathTester() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.util.ECPApplicableTester#isApplicable(org.eclipse.emf.edit.provider.IItemPropertyDescriptor,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		return check(eObject, (EStructuralFeature) itemPropertyDescriptor.getFeature(eObject));
	}

	private int check(EObject eObject, EStructuralFeature feature) {
		if (!VFeaturePathDomainModelReference.class.isInstance(eObject)) {
			return NOT_APPLICABLE;
		}
		if (ViewPackage.eINSTANCE.getVFeaturePathDomainModelReference_DomainModelEFeature() != feature) {
			return NOT_APPLICABLE;
		}
		if (!LeafCondition.class.isInstance(eObject.eContainer())) {
			return NOT_APPLICABLE;
		}
		return 3;
	}

}
