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
package org.eclipse.emf.ecp.edit.internal.swt.table;

import java.util.Date;
import java.util.Iterator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * Tester for a date cell editor.
 *
 * @author Eugen Neufeld
 *
 */
public class DateCellEditorTester implements ECPApplicableTester {

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.edit.provider.IItemPropertyDescriptor,
	 *      org.eclipse.emf.ecore.EObject)
	 * @deprecated
	 */
	@Override
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
	@Override
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
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	public int isApplicable(EObject eObject, EStructuralFeature feature) {
		if (EAttribute.class.isInstance(feature)) {
			final Class<?> instanceClass = ((EAttribute) feature).getEAttributeType().getInstanceClass();
			if (instanceClass == null) {
				return NOT_APPLICABLE;
			}
			if (Date.class.isAssignableFrom(instanceClass)) {
				return 1;
			}
		}
		return NOT_APPLICABLE;
	}
}
