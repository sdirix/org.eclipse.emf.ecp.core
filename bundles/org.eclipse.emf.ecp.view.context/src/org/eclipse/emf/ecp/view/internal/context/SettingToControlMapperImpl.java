/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler- initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.context;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.common.spi.UniqueSetting;
import org.eclipse.emf.ecp.view.spi.context.SettingToControlMapper;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.DomainModelReferenceChangeListener;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.mappingprovider.EMFFormsMappingProvider;

/**
 * Implementation of {@link SettingToControlMapper}.
 *
 * @author Lucas Koehler
 *
 */
@SuppressWarnings("deprecation")
public class SettingToControlMapperImpl implements SettingToControlMapper {
	private ViewModelContext viewModelContext;

	private final Set<EMFFormsMappingProvider> mappingProviders = new LinkedHashSet<EMFFormsMappingProvider>();
	private final ReportService reportService;

	/**
	 * A mapping between settings and controls.
	 */
	private final Map<UniqueSetting, Set<VControl>> settingToControlMap = new LinkedHashMap<UniqueSetting, Set<VControl>>();

	/**
	 * A mapping between controls and their domain model reference change listeners.
	 */
	private final Map<VControl, DomainModelReferenceChangeListener> controlChangeListener = new LinkedHashMap<VControl, DomainModelReferenceChangeListener>();

	// private final Map<EObject, Set<SettingToControlMapper>> childMappers = new LinkedHashMap<EObject,
	// Set<SettingToControlMapper>>();
	//
	// /**
	// * A mapping between setting to control mappers and their using elements.
	// */
	// private final Map<SettingToControlMapper, VElement> childMapperUsers = new LinkedHashMap<SettingToControlMapper,
	// VElement>();

	/**
	 * Creates a new instance of {@link SettingToControlMapperImpl}.
	 * <br />
	 * <strong>Note:</strong> {@link #instantiate(ViewModelContext)} has to be called before this instance can be used.
	 *
	 * @param reportService The {@link ReportService}
	 * @param mappingProviders The {@link EMFFormsMappingProvider mapping providers}
	 */
	public SettingToControlMapperImpl(ReportService reportService, Set<EMFFormsMappingProvider> mappingProviders) {
		this.reportService = reportService;
		this.mappingProviders.addAll(mappingProviders);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<VControl> getControlsFor(Setting setting) {
		return settingToControlMap.get(UniqueSetting.createSetting(setting));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<VElement> getControlsFor(UniqueSetting setting) {
		final Set<VElement> elements = new LinkedHashSet<VElement>();
		final Set<VControl> currentControls = settingToControlMap.get(setting);
		if (currentControls != null) {
			for (final VElement control : currentControls) {
				if (!control.isEnabled() || !control.isVisible() || control.isReadonly()) {
					continue;
				}
				elements.add(control);
			}
		}

		return elements;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateControlMapping(VControl vControl) {
		if (vControl == null) {
			return;
		}
		// delete old mapping
		for (final UniqueSetting setting : settingToControlMap.keySet()) {
			settingToControlMap.get(setting).remove(vControl);
		}
		// update mapping
		final Map<UniqueSetting, Set<VControl>> map = getSettingsToControlMapFor(vControl,
			viewModelContext.getDomainModel());
		for (final UniqueSetting setting : map.keySet()) {
			if (!settingToControlMap.containsKey(setting)) {
				settingToControlMap.put(setting, new LinkedHashSet<VControl>());
			}
			settingToControlMap.get(setting).addAll(map.get(setting));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void vControlRemoved(VControl vControl) {
		if (vControl.getDomainModelReference() == null) {
			return;
		}

		final Map<UniqueSetting, Set<VControl>> map = getSettingsToControlMapFor(vControl,
			viewModelContext.getDomainModel());
		for (final UniqueSetting setting : map.keySet()) {
			if (settingToControlMap.containsKey(setting)) {
				settingToControlMap.get(setting).removeAll(map.get(setting));
				if (settingToControlMap.get(setting).size() == 0) {
					settingToControlMap.remove(setting);
				}
			}
		}

		vControl.getDomainModelReference().getChangeListener().remove(controlChangeListener.get(vControl));
		controlChangeListener.remove(vControl);
		viewModelContext.unregisterDomainChangeListener(vControl.getDomainModelReference());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void vControlAdded(VControl vControl) {
		if (vControl.getDomainModelReference() == null) {
			return;
		}

		checkAndUpdateSettingToControlMapping(vControl);
		// final Map<UniqueSetting, Set<VControl>> map = getSettingsToControlMapFor(vControl,
		// viewModelContext.getDomainModel());
		// for (final UniqueSetting setting : map.keySet()) {
		// if (!settingToControlMap.containsKey(setting)) {
		// settingToControlMap.put(setting, new LinkedHashSet<VControl>());
		// }
		// settingToControlMap.get(setting).addAll(map.get(setting));
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkAndUpdateSettingToControlMapping(EObject eObject) {
		if (VControl.class.isInstance(eObject)) {
			final VControl vControl = (VControl) eObject;
			if (vControl.getDomainModelReference() == null) {
				return;
			}

			updateControlMapping(vControl);

			final DomainModelReferenceChangeListener changeListener = new DomainModelReferenceChangeListener() {

				@Override
				public void notifyChange() {
					updateControlMapping(vControl);
				}
			};
			final DomainModelReferenceChangeListener oldListener = controlChangeListener.put(vControl, changeListener);
			if (oldListener != null) {
				vControl.getDomainModelReference().getChangeListener().remove(oldListener);
			}
			vControl.getDomainModelReference().getChangeListener().add(changeListener);
			viewModelContext.registerDomainChangeListener(vControl.getDomainModelReference());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#instantiate(org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public void instantiate(ViewModelContext context) {
		viewModelContext = context;
	}

	private Map<UniqueSetting, Set<VControl>> getSettingsToControlMapFor(VControl vControl, EObject domainObject) {
		EMFFormsMappingProvider bestMappingProvider = null;
		double bestScore = EMFFormsMappingProvider.NOT_APPLICABLE;

		for (final EMFFormsMappingProvider mappingProvider : mappingProviders) {
			if (mappingProvider.isApplicable(vControl, domainObject) > bestScore) {
				bestMappingProvider = mappingProvider;
				bestScore = mappingProvider.isApplicable(vControl, domainObject);
			}
		}
		if (bestMappingProvider == null) {
			reportService.report(new AbstractReport("Warning: No applicable EMFFormsMappingProvider was found.")); //$NON-NLS-1$
			return Collections.emptyMap();
		}
		return bestMappingProvider.getMappingFor(vControl, domainObject);
	}

	// /**
	// * {@inheritDoc}
	// */
	// @Override
	// public void addChildMapper(VElement vElement, EObject eObject, ViewModelContext childContext) {
	// final SettingToControlMapper mapper = new SettingToControlMapperImpl(reportService, mappingProviders);
	// mapper.instantiate(childContext);
	// if (!childMappers.containsKey(eObject)) {
	// childMappers.put(eObject, new LinkedHashSet<SettingToControlMapper>());
	// }
	// childMappers.get(eObject).add(mapper);
	// childMapperUsers.put(mapper, vElement);
	// }
	//
	// /**
	// * {@inheritDoc}
	// */
	// @Override
	// public void removeChildMapper(EObject eObject) {
	// final Set<SettingToControlMapper> removedMappers = childMappers.remove(eObject);
	// if (removedMappers != null) {
	// for (final SettingToControlMapper removedMapper : removedMappers) {
	// childMapperUsers.remove(removedMapper);
	// }
	// }
	// }

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#dispose()
	 */
	@Override
	public void dispose() {
		settingToControlMap.clear();

		for (final VControl vControl : controlChangeListener.keySet()) {
			if (vControl.getDomainModelReference() != null) {
				vControl.getDomainModelReference().getChangeListener().remove(controlChangeListener.get(vControl));
			}
			viewModelContext.unregisterDomainChangeListener(vControl.getDomainModelReference());
		}
		controlChangeListener.clear();

		// childMappers.clear();
		// childMapperUsers.clear();

		viewModelContext = null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.context.ViewModelService#getPriority()
	 */
	@Override
	public int getPriority() {
		return 0;
	}
}
