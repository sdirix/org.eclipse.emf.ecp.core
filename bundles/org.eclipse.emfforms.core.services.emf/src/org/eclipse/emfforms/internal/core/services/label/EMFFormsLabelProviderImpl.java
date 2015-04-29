/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.label;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.common.spi.asserts.Assert;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emfforms.spi.common.locale.EMFFormsLocaleChangeListener;
import org.eclipse.emfforms.spi.common.locale.EMFFormsLocaleProvider;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.eclipse.emfforms.spi.core.services.emfspecificservice.EMFSpecificService;
import org.eclipse.emfforms.spi.core.services.label.EMFFormsLabelProvider;
import org.eclipse.emfforms.spi.core.services.label.NoLabelFoundException;
import org.eclipse.emfforms.spi.localization.EMFFormsLocalizationService;

/**
 * Implementation of {@link EMFFormsLabelProvider}. It provides a label service that delivers the display name and
 * description for a domain model reference and optionally an EObject.
 *
 * @author Eugen Neufeld
 *
 */
public class EMFFormsLabelProviderImpl implements EMFFormsLabelProvider, EMFFormsLocaleChangeListener {

	/**
	 * Key for the map of displayname observables.
	 *
	 * @author Eugen Neufeld
	 */
	private static class DisplayNameKey {
		private final String key;
		private final Class<?> bundleClass;

		public DisplayNameKey(String key, Class<?> bundleClass) {
			super();
			this.key = key;
			this.bundleClass = bundleClass;
		}

		String getKey() {
			return key;
		}

		Class<?> getBundleClass() {
			return bundleClass;
		}
	}

	/**
	 * Key for the map of description observables.
	 *
	 * @author Eugen Neufeld
	 */
	private static class DescriptionKey {
		private final String eClassName;
		private final String featureName;
		private final Class<?> bundleClass;

		public DescriptionKey(String eClassName, String featureName, Class<?> bundleClass) {
			super();
			this.eClassName = eClassName;
			this.featureName = featureName;
			this.bundleClass = bundleClass;
		}

		Class<?> getBundleClass() {
			return bundleClass;
		}

		String geteClassName() {
			return eClassName;
		}

		String getFeatureName() {
			return featureName;
		}
	}

	private static final String DISPLAY_NAME = "_UI_%1$s_%2$s_feature"; //$NON-NLS-1$
	private static final String DESCRIPTION = "_UI_%1$s_%2$s_description"; //$NON-NLS-1$
	private static final String DESCRIPTION_COMPOSITE = "_UI_PropertyDescriptor_description"; //$NON-NLS-1$
	private static final String TYPE = "_UI_%1$s_type"; //$NON-NLS-1$

	private EMFFormsDatabinding emfFormsDatabinding;
	private EMFFormsLocalizationService localizationService;
	private ReportService reportService;

	private final Map<DisplayNameKey, WritableValue> displayKeyObservableMap = new LinkedHashMap<DisplayNameKey, WritableValue>();
	private final Map<DescriptionKey, WritableValue> descriptionKeyObservableMap = new LinkedHashMap<DescriptionKey, WritableValue>();
	private EMFFormsLocaleProvider localeProvider;
	private EMFSpecificService emfSpecificService;

	/**
	 * Sets the {@link ReportService} service.
	 *
	 * @param reportService The ReportService service.
	 */
	protected void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * Sets the {@link EMFFormsDatabinding} service.
	 *
	 * @param emfFormsDatabinding The databinding service.
	 */
	protected void setEMFFormsDatabinding(EMFFormsDatabinding emfFormsDatabinding) {
		this.emfFormsDatabinding = emfFormsDatabinding;
	}

	/**
	 * Sets the {@link EMFFormsLocalizationService}.
	 *
	 * @param localizationService The {@link EMFFormsLocalizationService}
	 */
	protected void setEMFFormsLocalizationService(EMFFormsLocalizationService localizationService) {
		this.localizationService = localizationService;
	}

	/**
	 * Sets the {@link EMFFormsLocaleProvider}.
	 *
	 * @param localeProvider The {@link EMFFormsLocaleProvider}
	 */
	protected void setEMFFormsLocaleProvider(EMFFormsLocaleProvider localeProvider) {
		this.localeProvider = localeProvider;
		this.localeProvider.addEMFFormsLocaleChangeListener(this);
	}

	/**
	 * Sets the {@link EMFSpecificService}.
	 *
	 * @param emfSpecificService The {@link EMFSpecificService}
	 */
	protected void setEMFSpecificService(EMFSpecificService emfSpecificService) {
		this.emfSpecificService = emfSpecificService;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see EMFFormsLabelProvider#getDisplayName(VDomainModelReference)
	 */
	@Override
	public IObservableValue getDisplayName(VDomainModelReference domainModelReference) throws NoLabelFoundException {
		Assert.create(domainModelReference).notNull();

		IValueProperty valueProperty;
		try {
			valueProperty = emfFormsDatabinding.getValueProperty(domainModelReference, null);
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			throw new NoLabelFoundException(ex);
		}
		final EStructuralFeature structuralFeature = (EStructuralFeature) valueProperty.getValueType();
		final EClass eContainingClass = structuralFeature.getEContainingClass();
		if (eContainingClass.isAbstract() || eContainingClass.isInterface()) {
			return getObservableValue(getFallbackLabel(structuralFeature));
		}
		final EObject tempInstance = EcoreUtil.create(eContainingClass);
		final Class<?> editBundleClass = getEditBundleClass(tempInstance);
		final String key = String.format(DISPLAY_NAME, tempInstance.eClass().getName(), structuralFeature.getName());
		final WritableValue value = getObservableValue(getDisplayName(editBundleClass, key));
		displayKeyObservableMap.put(new DisplayNameKey(key, editBundleClass), value);
		return value;
	}

	/**
	 * Creates a fall back label from a {@link EStructuralFeature}.
	 *
	 * @param structuralFeature The {@link EStructuralFeature}
	 * @return The fall back label
	 */
	private String getFallbackLabel(final EStructuralFeature structuralFeature) {
		final String[] split = structuralFeature.getName().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"); //$NON-NLS-1$
		final char[] charArray = split[0].toCharArray();
		charArray[0] = Character.toUpperCase(charArray[0]);
		split[0] = new String(charArray);
		final StringBuilder sb = new StringBuilder();
		for (final String str : split) {
			sb.append(str);
			sb.append(" "); //$NON-NLS-1$
		}
		return sb.toString().trim();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see EMFFormsLabelProvider#getDisplayName(VDomainModelReference,EObject)
	 */
	@Override
	public IObservableValue getDisplayName(VDomainModelReference domainModelReference, EObject rootObject)
		throws NoLabelFoundException {
		Assert.create(domainModelReference).notNull();
		Assert.create(rootObject).notNull();

		IObservableValue observableValue;
		try {
			observableValue = emfFormsDatabinding.getObservableValue(domainModelReference,
				rootObject);
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			throw new NoLabelFoundException(ex);
		}
		final IObserving observing = (IObserving) observableValue;
		final EStructuralFeature structuralFeature = (EStructuralFeature) observableValue.getValueType();
		final EObject value = (EObject) observing.getObserved();
		observableValue.dispose();

		final Class<?> editBundleClass = getEditBundleClass(value);
		final String key = String.format(DISPLAY_NAME, value.eClass().getName(), structuralFeature.getName());
		final WritableValue displayObserveValue = getObservableValue(getDisplayName(editBundleClass, key));
		displayKeyObservableMap.put(new DisplayNameKey(key, editBundleClass), displayObserveValue);
		return displayObserveValue;
	}

	private Class<?> getEditBundleClass(final EObject value) {
		return emfSpecificService.getIItemPropertySource(value).getClass();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see EMFFormsLabelProvider#getDescription(VDomainModelReference)
	 */
	@Override
	public IObservableValue getDescription(VDomainModelReference domainModelReference) throws NoLabelFoundException {
		Assert.create(domainModelReference).notNull();

		IValueProperty valueProperty;
		try {
			valueProperty = emfFormsDatabinding.getValueProperty(domainModelReference, null);
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			throw new NoLabelFoundException(ex);
		}
		final EStructuralFeature structuralFeature = (EStructuralFeature) valueProperty.getValueType();
		final EClass eContainingClass = structuralFeature.getEContainingClass();
		if (eContainingClass.isAbstract() || eContainingClass.isInterface()) {
			return getObservableValue(getFallbackLabel(structuralFeature));
		}
		final EObject tempInstance = EcoreUtil.create(eContainingClass);
		final Class<?> editBundleClass = getEditBundleClass(tempInstance);
		final WritableValue writableValue = getObservableValue(getDescription(tempInstance.eClass().getName(),
			structuralFeature.getName(), editBundleClass));
		descriptionKeyObservableMap.put(new DescriptionKey(tempInstance.eClass().getName(),
			structuralFeature.getName(), editBundleClass), writableValue);
		return writableValue;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see EMFFormsLabelProvider#getDescription(VDomainModelReference,EObject)
	 */
	@Override
	public IObservableValue getDescription(VDomainModelReference domainModelReference, EObject rootObject)
		throws NoLabelFoundException {
		Assert.create(domainModelReference).notNull();
		Assert.create(rootObject).notNull();

		IObservableValue observableValue;
		try {
			observableValue = emfFormsDatabinding.getObservableValue(domainModelReference,
				rootObject);
		} catch (final DatabindingFailedException ex) {
			reportService.report(new DatabindingFailedReport(ex));
			throw new NoLabelFoundException(ex);
		}
		final IObserving observing = (IObserving) observableValue;
		final EStructuralFeature structuralFeature = (EStructuralFeature) observableValue.getValueType();
		final EObject value = (EObject) observing.getObserved();
		observableValue.dispose();

		final Class<?> editBundleClass = getEditBundleClass(value);
		final WritableValue writableValue = getObservableValue(getDescription(value.eClass().getName(),
			structuralFeature.getName(), editBundleClass));
		descriptionKeyObservableMap.put(new DescriptionKey(value.eClass().getName(),
			structuralFeature.getName(), editBundleClass), writableValue);
		return writableValue;
	}

	private WritableValue getObservableValue(String value) {
		return new WritableValue(value, String.class);
	}

	private String getDisplayName(Class<?> bundleClass, String key) {
		return localizationService.getString(bundleClass, key);
	}

	private String getDescription(String eClassName, String featureName, Class<?> bundleClass) {
		final String keyDefault = String.format(DESCRIPTION, eClassName, featureName);
		final String description = localizationService.getString(bundleClass,
			keyDefault);
		if (description != keyDefault) {
			return description;
		}
		final String descriptionWithSubstitution = localizationService.getString(bundleClass,
			DESCRIPTION_COMPOSITE);
		final String key = String.format(DISPLAY_NAME, eClassName, featureName);
		final String featureSubstitution = getDisplayName(bundleClass, key);
		final String eObjectSubstitution = localizationService.getString(bundleClass,
			String.format(TYPE, eClassName));
		return MessageFormat.format(descriptionWithSubstitution, featureSubstitution, eObjectSubstitution);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.common.locale.EMFFormsLocaleChangeListener#notifyLocaleChange()
	 */
	@Override
	public void notifyLocaleChange() {
		for (final DisplayNameKey displayNameKey : displayKeyObservableMap.keySet()) {
			final WritableValue writableValue = displayKeyObservableMap.get(displayNameKey);
			writableValue.setValue(getDisplayName(displayNameKey.getBundleClass(), displayNameKey.getKey()));
		}
		for (final DescriptionKey descriptionKey : descriptionKeyObservableMap.keySet()) {
			final WritableValue writableValue = descriptionKeyObservableMap.get(descriptionKey);
			writableValue.setValue(getDescription(descriptionKey.geteClassName(), descriptionKey.getFeatureName(),
				descriptionKey.getBundleClass()));
		}
	}
}
