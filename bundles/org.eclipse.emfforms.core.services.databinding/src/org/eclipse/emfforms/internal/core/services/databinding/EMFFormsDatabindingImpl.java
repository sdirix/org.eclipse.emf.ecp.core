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
package org.eclipse.emfforms.internal.core.services.databinding;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emfforms.spi.core.services.databinding.DomainModelReferenceConverter;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;

/**
 * EMF implementation of {@link EMFFormsDatabinding}.
 * 
 * @author Lucas Koehler
 *
 */
public class EMFFormsDatabindingImpl implements EMFFormsDatabinding {

	private final Set<DomainModelReferenceConverter> referenceConverters = new LinkedHashSet<DomainModelReferenceConverter>();
	private final Realm realm;

	/**
	 * Creates a new instance of {@link EMFFormsDatabindingImpl}.
	 */
	public EMFFormsDatabindingImpl() {
		realm = Realm.getDefault();
	}

	/**
	 * Creates a new instance of {@link EMFFormsDatabindingImpl} with a given {@link Realm}.
	 *
	 * @param realm The {@link Realm} to be used by this {@link EMFFormsDatabindingImpl}
	 */
	EMFFormsDatabindingImpl(Realm realm) {
		this.realm = realm;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding#getObservableValue(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public IObservableValue getObservableValue(VDomainModelReference domainModelReference, EObject object) {
		if (domainModelReference == null) {
			throw new IllegalArgumentException("The given VDomainModelReference must not be null."); //$NON-NLS-1$
		}
		if (object == null) {
			throw new IllegalArgumentException("The given EObject must not be null."); //$NON-NLS-1$
		}

		final IValueProperty valueProperty = getValueProperty(domainModelReference);
		return valueProperty.observe(realm, object);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding#getValueProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference)
	 */
	@Override
	public IValueProperty getValueProperty(VDomainModelReference domainModelReference) {
		if (domainModelReference == null) {
			throw new IllegalArgumentException("The given VDomainModelReference must not be null."); //$NON-NLS-1$
		}
		double highestPriority = DomainModelReferenceConverter.NOT_APPLICABLE;
		DomainModelReferenceConverter bestConverter = null;
		for (final DomainModelReferenceConverter converter : referenceConverters) {
			final double priority = converter.isApplicable(domainModelReference);
			if (priority > highestPriority) {
				highestPriority = priority;
				bestConverter = converter;
			}
		}

		if (bestConverter != null) {
			return bestConverter.convert(domainModelReference);
		}

		throw new IllegalStateException("No applicable DomainModelReferenceConverter could be found."); //$NON-NLS-1$
	}

	/**
	 * Adds the given {@link DomainModelReferenceConverter} to the Set of reference converters.
	 *
	 * @param converter The {@link DomainModelReferenceConverter} to add
	 */
	protected void addDomainModelReferenceConverter(DomainModelReferenceConverter converter) {
		referenceConverters.add(converter);
	}

	/**
	 * Removes the given {@link DomainModelReferenceConverter} to the Set of reference converters.
	 *
	 * @param converter The {@link DomainModelReferenceConverter} to remove
	 */
	protected void removeDomainModelReferenceConverter(DomainModelReferenceConverter converter) {
		referenceConverters.remove(converter);
	}
}
