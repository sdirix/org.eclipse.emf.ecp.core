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
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.reference;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * Tester for a link cell editor.
 * 
 * @author Eugen Neufeld
 * 
 */
public class LinkCellEditorTester implements ECPApplicableTester {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.edit.provider.IItemPropertyDescriptor,
	 *      org.eclipse.emf.ecore.EObject)
	 * @deprecated
	 */
	@Deprecated
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		final EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor.getFeature(null);
		return isApplicable(eObject, feature);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
	 * 
	 */
	public int isApplicable(VDomainModelReference domainModelReference) {
		final Iterator<Setting> iterator = domainModelReference.getIterator();
		int count = 0;
		Setting setting = null;
		while (iterator.hasNext()) {
			count++;
			setting = iterator.next();
		}
		if (count != 1) {
			return NOT_APPLICABLE;
		}
		return isApplicable(setting.getEObject(), setting.getEStructuralFeature());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public int isApplicable(EObject eObject, EStructuralFeature feature) {
		if (EReference.class.isInstance(feature)) {
			return 1;
		}
		return NOT_APPLICABLE;
	}

}
