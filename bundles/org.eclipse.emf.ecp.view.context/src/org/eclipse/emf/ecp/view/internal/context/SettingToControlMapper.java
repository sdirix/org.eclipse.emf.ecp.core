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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.common.spi.UniqueSetting;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelService;
import org.eclipse.emf.ecp.view.spi.model.DomainModelReferenceChangeListener;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * Helper class for the {@link ViewModelContextImpl} to manage the setting to control mappings.
 *
 * @author Lucas Koehler
 *
 */
// FIXME remove asap
@SuppressWarnings("deprecation")
public class SettingToControlMapper implements ViewModelService {
	private ViewModelContext viewModelContext;

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

	// /**
	// * A mapping between setting to control mappers and their using elements.
	// */
	// private final Map<SettingToControlMapper, VElement> childMapperUsers = new LinkedHashMap<SettingToControlMapper,
	// VElement>();

	/**
	 * Returns all controls which are associated with the provided {@link Setting}. The {@link Setting} is converted to
	 * a {@link UniqueSetting}.
	 *
	 * @param setting the {@link Setting} to search controls for
	 * @return the Set of all controls associated with the provided setting or null if no controls can be found
	 */
	public Set<VControl> getControlsFor(Setting setting) {
		return settingToControlMap.get(UniqueSetting.createSetting(setting));
	}

	/**
	 * Returns all controls which are associated with the provided {@link UniqueSetting}.
	 *
	 * @param setting the {@link UniqueSetting} to search controls for
	 * @return the Set of all controls associated with the provided setting or null if no controls can be found
	 */
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
	 * Updates the setting to control mapping for the given {@link VControl}.
	 *
	 * @param vControl The {@link VControl}
	 */
	public void updateControlMapping(VControl vControl) {
		if (vControl == null) {
			return;
		}
		// delete old mapping
		for (final UniqueSetting setting : settingToControlMap.keySet()) {
			settingToControlMap.get(setting).remove(vControl);
		}
		if (vControl.getDomainModelReference() == null) {
			return;
		}
		final Iterator<Setting> iterator = vControl.getDomainModelReference().getIterator();
		while (iterator.hasNext()) {
			final Setting setting = iterator.next();
			if (setting == null) {
				continue;
			}
			final UniqueSetting uniqueSetting = UniqueSetting.createSetting(setting);
			if (!settingToControlMap.containsKey(uniqueSetting)) {
				settingToControlMap.put(uniqueSetting, new LinkedHashSet<VControl>());
			}
			settingToControlMap.get(uniqueSetting).add(vControl);
		}
	}

	/**
	 * Removes a {@link VControl} from the setting to control mapping.
	 *
	 * @param vControl The {@link VControl} to remove
	 */
	public void vControlRemoved(VControl vControl) {
		if (vControl.getDomainModelReference() == null) {
			return;
		}

		final Iterator<Setting> iterator = vControl.getDomainModelReference().getIterator();
		while (iterator.hasNext()) {
			final Setting next = iterator.next();
			final UniqueSetting uniqueSetting = UniqueSetting.createSetting(next);
			if (settingToControlMap.containsKey(uniqueSetting)) {
				settingToControlMap.get(uniqueSetting).remove(vControl);
				if (settingToControlMap.get(uniqueSetting).size() == 0) {
					settingToControlMap.remove(uniqueSetting);
				}
			}
		}

		vControl.getDomainModelReference().getChangeListener().remove(controlChangeListener.get(vControl));
		controlChangeListener.remove(vControl);
		viewModelContext.unregisterDomainChangeListener(vControl.getDomainModelReference());
	}

	/**
	 * Adds a {@link VControl} to the setting to control mapping.
	 *
	 * @param vControl The {@link VControl} to add
	 */
	public void vControlAdded(VControl vControl) {
		if (vControl.getDomainModelReference() == null) {
			return;
		}

		checkAndUpdateSettingToControlMapping(vControl);
	}

	/**
	 * Checks and updates the mapping for the given {@link EObject}.
	 *
	 * @param eObject The {@link EObject}
	 */
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
			controlChangeListener.put(vControl, changeListener);

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

	// /**
	// * Adds a child {@link SettingToControlMapper} to this mapper.
	// *
	// * @param vElement The {@link VElement} that uses the new mapper
	// * @param eObject the domain object of the {@link ViewModelContext}
	// * @param childContext The {@link ViewModelContext} of the new child mapper
	// */
	// public void addChildMapper(VElement vElement, EObject eObject, ViewModelContext childContext) {
	// final SettingToControlMapper mapper = new SettingToControlMapper();
	// mapper.instantiate(childContext);
	// if (!childMappers.containsKey(eObject)) {
	// childMappers.put(eObject, new LinkedHashSet<SettingToControlMapper>());
	// }
	// childMappers.get(eObject).add(mapper);
	// childMapperUsers.put(mapper, vElement);
	// }

	// /**
	// * Removes a child {@link SettingToControlMapper} from this mapper.
	// *
	// * @param eObject Remove all child mappers for this domain object
	// */
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
