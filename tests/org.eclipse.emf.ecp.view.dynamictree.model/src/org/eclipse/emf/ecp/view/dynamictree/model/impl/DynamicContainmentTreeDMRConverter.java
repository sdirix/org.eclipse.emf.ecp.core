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
package org.eclipse.emf.ecp.view.dynamictree.model.impl;

import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTreeDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * A {@link DomainModelReferenceConverter} that converts a {@link DynamicContainmentTreeDomainModelReference} to an
 * {@link IListProperty IListProperty} or an {@link IValueProperty}.
 *
 * @author Lucas Koehler
 *
 */
public class DynamicContainmentTreeDMRConverter implements DomainModelReferenceConverter {
	private EMFFormsDatabinding emfFormsDatabinding;
	private ServiceReference<EMFFormsDatabinding> databindingServiceReference;

	/**
	 * Sets the {@link EMFFormsDatabinding}.
	 *
	 * @param emfFormsDatabinding the emfFormsDatabinding to set
	 */
	void setEMFFormsDatabinding(EMFFormsDatabinding emfFormsDatabinding) {
		this.emfFormsDatabinding = emfFormsDatabinding;
	}

	/**
	 * Unsets the {@link EMFFormsDatabinding}.
	 */
	void unsetEMFFormsDatabinding() {
		emfFormsDatabinding = null;
	}

	/**
	 * This method is called by the OSGI framework when this {@link DomainModelReferenceConverter} is activated. It
	 * retrieves the {@link EMFFormsDatabinding EMF Forms databinding service}.
	 *
	 * @param bundleContext The {@link BundleContext} of this classes bundle.
	 */
	protected final void activate(BundleContext bundleContext) {
		databindingServiceReference = bundleContext.getServiceReference(EMFFormsDatabinding.class);
		setEMFFormsDatabinding(bundleContext.getService(databindingServiceReference));

	}

	/**
	 * This method is called by the OSGI framework when this {@link DomainModelReferenceConverter} is deactivated.
	 * It frees the {@link EMFFormsDatabinding EMF Forms databinding service}.
	 *
	 * @param bundleContext The {@link BundleContext} of this classes bundle.
	 */
	protected final void deactivate(BundleContext bundleContext) {
		unsetEMFFormsDatabinding();
		bundleContext.ungetService(databindingServiceReference);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter#isApplicable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
	 */
	@Override
	public double isApplicable(VDomainModelReference domainModelReference) {
		if (domainModelReference == null) {
			throw new IllegalArgumentException("The given VDomainModelReference must not be null."); //$NON-NLS-1$
		}
		if (domainModelReference instanceof DynamicContainmentTreeDomainModelReference) {
			return 10d;
		}
		return NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter#convertToValueProperty(VDomainModelReference,EObject)
	 */
	@Override
	public IValueProperty convertToValueProperty(VDomainModelReference domainModelReference, EObject object)
		throws DatabindingFailedException {
		final DynamicContainmentTreeDomainModelReference dynamicContainmentTreeReference = getAndCheckDynamicContainmentTreeDMR(domainModelReference);

		final int index = getIndex(dynamicContainmentTreeReference);

		final EMFIndexedValuePropertyDelegator indexedProperty = getIndexedRootProperty(
			dynamicContainmentTreeReference, index, object);
		final IValueProperty valuePropertyFromBase = emfFormsDatabinding
			.getValueProperty(dynamicContainmentTreeReference.getPathFromBase(), object);

		return indexedProperty.value(valuePropertyFromBase);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter#convertToListProperty(VDomainModelReference,EObject)
	 */
	@Override
	public IListProperty convertToListProperty(VDomainModelReference domainModelReference, EObject object)
		throws DatabindingFailedException {
		final DynamicContainmentTreeDomainModelReference dynamicContainmentTreeReference = getAndCheckDynamicContainmentTreeDMR(domainModelReference);

		final int index = getIndex(dynamicContainmentTreeReference);

		final EMFIndexedValuePropertyDelegator indexedProperty = getIndexedRootProperty(
			dynamicContainmentTreeReference, index, object);
		final IListProperty listPropertyFromBase = emfFormsDatabinding
			.getListProperty(dynamicContainmentTreeReference.getPathFromBase(), object);

		return indexedProperty.list(listPropertyFromBase);
	}

	private EMFIndexedValuePropertyDelegator getIndexedRootProperty(
		final DynamicContainmentTreeDomainModelReference dynamicContainmentTreeReference, final int index,
		EObject object)
		throws DatabindingFailedException, IllegalListTypeException {
		final IValueProperty valuePropertyFromRoot = emfFormsDatabinding
			.getValueProperty(dynamicContainmentTreeReference.getPathFromRoot(), object);
		final EStructuralFeature structuralFeature = (EStructuralFeature) valuePropertyFromRoot.getValueType();
		checkListType(structuralFeature);

		final EMFIndexedValuePropertyDelegator indexedProperty = new EMFIndexedValuePropertyDelegator(
			valuePropertyFromRoot, index);
		return indexedProperty;
	}

	private DynamicContainmentTreeDomainModelReference getAndCheckDynamicContainmentTreeDMR(
		VDomainModelReference domainModelReference) throws DatabindingFailedException {
		if (domainModelReference == null) {
			throw new IllegalArgumentException("The given VDomainModelReference must not be null."); //$NON-NLS-1$
		}
		if (!DynamicContainmentTreeDomainModelReference.class.isInstance(domainModelReference)) {
			throw new IllegalArgumentException(
				"DomainModelReference must be an instance of DynamicContainmentTreeDomainModelReference."); //$NON-NLS-1$
		}

		final DynamicContainmentTreeDomainModelReference dynamicContainmentTreeReference = DynamicContainmentTreeDomainModelReference.class
			.cast(domainModelReference);
		if (dynamicContainmentTreeReference.getPathFromRoot() == null) {
			throw new DatabindingFailedException(
				"The field pathFromRoot of the given DynamicContainmentTreeDomainModelReference must not be null."); //$NON-NLS-1$
		}
		if (dynamicContainmentTreeReference.getPathFromBase() == null) {
			throw new DatabindingFailedException(
				"The field pathFromBase of the given DynamicContainmentTreeDomainModelReference must not be null."); //$NON-NLS-1$
		}
		return dynamicContainmentTreeReference;
	}

	private int getIndex(DynamicContainmentTreeDomainModelReference reference) throws DatabindingFailedException {
		EObject parent = reference.eContainer();
		while (!DynamicContainmentItem.class.isInstance(parent) && parent != null) {
			parent = parent.eContainer();
		}
		if (parent == null) {
			throw new DatabindingFailedException(
				"The base item index of the DynamicContainmentTreeDomainModelReference could not be resolved because its container is null."); //$NON-NLS-1$
		}
		final DynamicContainmentItem packingItem = (DynamicContainmentItem) parent;
		if (packingItem.getBaseItemIndex() == null) {
			throw new DatabindingFailedException(
				"The base item index of the DynamicContainmentTreeDomainModelReference must not be null."); //$NON-NLS-1$
		}
		return packingItem.getBaseItemIndex();
	}

	/**
	 * Checks whether the given structural feature references a proper list to generate a value or list property.
	 *
	 * @param structuralFeature The feature to check
	 * @throws IllegalListTypeException if the structural feature doesn't reference a proper list.
	 */
	private void checkListType(EStructuralFeature structuralFeature) throws IllegalListTypeException {
		if (!structuralFeature.isMany()) {
			throw new IllegalListTypeException(
				"The DynamicContainmentTreeDomainModelReference's base feature must reference a list."); //$NON-NLS-1$
		}
		if (!EReference.class.isInstance(structuralFeature)) {
			throw new IllegalListTypeException(
				"The DynamicContainmentTreeDomainModelReference's base feature must reference a list of EObjects."); //$NON-NLS-1$
		}
	}
}
