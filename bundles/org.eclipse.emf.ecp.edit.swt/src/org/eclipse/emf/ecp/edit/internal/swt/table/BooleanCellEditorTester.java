/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * jfaltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.table;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * Tester for {@link BooleanCellEditor}.
 *
 * @author jfaltermeier
 *
 */
@SuppressWarnings("deprecation")
public class BooleanCellEditorTester implements ECPApplicableTester {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.edit.provider.IItemPropertyDescriptor,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		final EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor.getFeature(null);
		return isApplicable(eObject, feature);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
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
			if (Boolean.class.isAssignableFrom(instanceClass)) {
				return 3;
			}
			if (instanceClass.isPrimitive()) {
				try {
					final Class<?> primitive = (Class<?>) Boolean.class.getField("TYPE").get(null); //$NON-NLS-1$
					if (primitive.equals(instanceClass)) {
						return 3;
					}
					return NOT_APPLICABLE;
				} catch (final IllegalArgumentException e) {
					return NOT_APPLICABLE;
				} catch (final SecurityException e) {
					return NOT_APPLICABLE;
				} catch (final IllegalAccessException e) {
					return NOT_APPLICABLE;
				} catch (final NoSuchFieldException e) {
					return NOT_APPLICABLE;
				}
			}

		}
		return NOT_APPLICABLE;
	}
}
