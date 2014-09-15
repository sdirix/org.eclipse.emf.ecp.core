/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.common.internal.databinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.common.spi.databinding.DatabindingProviderService;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

/**
 * @author Eugen
 *
 */
public class FeaturePathDatabindingProviderService implements
DatabindingProviderService<VFeaturePathDomainModelReference> {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.spi.databinding.DatabindingProviderService#getObservable(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <O extends IObservable> O getObservable(VFeaturePathDomainModelReference domainModelReference,
		Class<O> observableClass) {
		if (domainModelReference == null) {
			throw new IllegalArgumentException("The DomainModelReference must not be null."); //$NON-NLS-1$
		}
		if (!observableClass.isAssignableFrom(IObservableValue.class)) {
			throw new IllegalArgumentException("A FeaturePathDomainModelReference can't provide " //$NON-NLS-1$
				+ observableClass.getName());
		}
		final Setting lastSetting = getSetting(domainModelReference, SettingOption.Last);
		return (O) EMFEditObservables.observeValue(
			AdapterFactoryEditingDomain.getEditingDomainFor(lastSetting.getEObject()), lastSetting.getEObject(),
			lastSetting.getEStructuralFeature());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.spi.databinding.DatabindingProviderService#getProperty(org.eclipse.emf.ecp.view.spi.model.VDomainModelReference,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <P extends IProperty> P getProperty(VFeaturePathDomainModelReference domainModelReference,
		Class<P> propertyClass) {
		if (domainModelReference == null) {
			throw new IllegalArgumentException("The DomainModelReference must not be null."); //$NON-NLS-1$
		}
		if (!propertyClass.isAssignableFrom(IValueProperty.class)) {
			throw new IllegalArgumentException("A FeaturePathDomainModelReference can't provide " //$NON-NLS-1$
				+ propertyClass.getName());
		}
		final List<EStructuralFeature> fullList = new ArrayList<EStructuralFeature>(
			domainModelReference.getDomainModelEReferencePath());
		fullList.add(domainModelReference.getDomainModelEFeature());
		final FeaturePath featurePath = FeaturePath.fromList(fullList.toArray(new EStructuralFeature[0]));
		final Setting setting = getLastSettingForProperty(domainModelReference);
		return setting == null ? (P) EMFProperties.value(featurePath) : (P) EMFEditProperties.value(
			AdapterFactoryEditingDomain.getEditingDomainFor(setting.getEObject()), featurePath);
	}

	private Setting getLastSettingForProperty(VFeaturePathDomainModelReference domainModelReference) {
		try {
			return getSetting(domainModelReference, SettingOption.Last);
		} catch (final IllegalArgumentException ex) {
			// dmr not resolved -> try parent
		}
		if (!VDomainModelReference.class.isInstance(domainModelReference.eContainer())) {
			return null;
		}
		try {
			return getSetting((VDomainModelReference) domainModelReference.eContainer(), SettingOption.First);
		} catch (final IllegalArgumentException ex) {
			return null;
		}
	}

	private Setting getSetting(VDomainModelReference domainModelReference, SettingOption option) {
		final Iterator<Setting> iterator = domainModelReference.getIterator();
		if (iterator == null) {
			throw new IllegalArgumentException("The DomainModelReference must be resolved."); //$NON-NLS-1$
		}
		switch (option) {
		case Last:
			Setting lastSetting = null;
			int numberSettings = 0;
			while (iterator.hasNext()) {
				lastSetting = iterator.next();
				numberSettings++;
			}
			if (lastSetting == null || numberSettings != 1) {
				throw new IllegalArgumentException("The DomainModelReference must be resolved."); //$NON-NLS-1$
			}
			return lastSetting;

		case First:
			while (iterator.hasNext()) {
				return iterator.next();
			}
			throw new IllegalArgumentException("The DomainModelReference must be resolved."); //$NON-NLS-1$

		default:
			throw new IllegalArgumentException("Unknown option."); //$NON-NLS-1$
		}

	}

	/**
	 * Options for getting a setting from a dmr.
	 *
	 */
	private enum SettingOption {
		First, Last
	}

}
