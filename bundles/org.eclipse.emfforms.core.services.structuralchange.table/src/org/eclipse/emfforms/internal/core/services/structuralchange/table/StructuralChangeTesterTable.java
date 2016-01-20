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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.common.spi.asserts.Assert;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emfforms.internal.core.services.structuralchange.defaultheuristic.StructuralChangeTesterDefault;
import org.eclipse.emfforms.spi.core.services.structuralchange.EMFFormsStructuralChangeTester;
import org.eclipse.emfforms.spi.core.services.structuralchange.StructuralChangeTesterInternal;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Implementation of {@link StructuralChangeTesterInternal} for {@link VTableDomainModelReference
 * VTableDomainModelReferences}.
 *
 * @author Lucas Koehler
 *
 */
@Component(name = "StructuralChangeTesterTable")
public class StructuralChangeTesterTable implements StructuralChangeTesterInternal {

	private EMFFormsStructuralChangeTester emfFormsStructuralChangeTester;

	/**
	 * Sets the {@link EMFFormsStructuralChangeTester} service.
	 *
	 * @param emfFormsStructuralChangeTester The structural change tester
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
		if (notification.getRawNotification().isTouch()) {
			return false;
		}

		final VTableDomainModelReference tableDMR = (VTableDomainModelReference) reference;

		if (tableDMR.getDomainModelEFeature() != null) {
			final StructuralChangeTesterDefault featurePathTester = new StructuralChangeTesterDefault();
			return featurePathTester.isStructureChanged(reference, domainRootObject, notification);
		}

		Assert.create(tableDMR.getDomainModelReference()).notNull();

		return emfFormsStructuralChangeTester.isStructureChanged(tableDMR.getDomainModelReference(), domainRootObject,
			notification);
	}

}
