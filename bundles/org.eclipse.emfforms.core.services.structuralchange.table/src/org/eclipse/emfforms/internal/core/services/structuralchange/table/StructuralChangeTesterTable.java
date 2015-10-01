/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.structuralchange.table;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.common.spi.asserts.Assert;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emfforms.internal.core.services.structuralchange.defaultheuristic.StructuralChangeTesterDefault;
import org.eclipse.emfforms.spi.core.services.structuralchange.StructuralChangeTesterInternal;
import org.osgi.service.component.annotations.Component;

/**
 * Implementation of {@link StructuralChangeTesterInternal} for {@link VTableDomainModelReference
 * VTableDomainModelReferences}.
 *
 * @author Lucas Koehler
 *
 */
// TODO: get rid of iterator usage
@SuppressWarnings("deprecation")
@Component(name = "StructuralChangeTesterTable")
public class StructuralChangeTesterTable implements StructuralChangeTesterInternal {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.structuralchange.StructuralChangeTesterInternal#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
	 */
	@Override
	public double isApplicable(VDomainModelReference reference) {
		Assert.create(reference).notNull();
		if (VTableDomainModelReference.class.isInstance(reference)) {
			return 5d;
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

		Assert.create(reference).notNull();
		Assert.create(notification).notNull();
		Assert.create(reference).ofClass(VTableDomainModelReference.class);

		final VTableDomainModelReference tableDMR = (VTableDomainModelReference) reference;

		if (tableDMR.getDomainModelEFeature() != null) {
			final StructuralChangeTesterDefault featurePathTester = new StructuralChangeTesterDefault();
			return featurePathTester.isStructureChanged(reference, domainRootObject, notification);
		}

		if (notification.getRawNotification().isTouch()) {
			return false;
		}

		Assert.create(tableDMR.getDomainModelReference()).notNull();

		final Iterator<EStructuralFeature> structuralFeatureIterator = tableDMR.getDomainModelReference()
			.getEStructuralFeatureIterator();
		while (structuralFeatureIterator.hasNext()) {
			final EStructuralFeature feature = structuralFeatureIterator.next();
			if (feature.equals(notification.getStructuralFeature())) {
				return true;
			}
		}

		return false;
	}

}
