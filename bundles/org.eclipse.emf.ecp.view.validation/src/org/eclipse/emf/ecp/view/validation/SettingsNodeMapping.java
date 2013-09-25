/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.common.UniqueSetting;

/**
 * Maps a {@link UniqueSetting} to a {@link CachedGraphNode}.
 * 
 * @author emueller
 * 
 * @param <T> the value stored by the nodes
 */
public class SettingsNodeMapping<T> {

	private static final EStructuralFeature ALL_SETTINGS = null;

	private final Map<UniqueSetting, CachedGraphNode<T>> settings;
	private final Comparator<T> comparator;
	private final EObject model;

	public SettingsNodeMapping(EObject domainModel, Comparator<T> comparator) {
		this.model = domainModel;
		settings = new LinkedHashMap<UniqueSetting, CachedGraphNode<T>>();
		this.comparator = comparator;
	}

	public CachedGraphNode<T> getNode(EObject eObject, EStructuralFeature feature) {
		final UniqueSetting setting = UniqueSetting.createSetting(eObject, feature);
		final CachedGraphNode<T> node = settings.get(setting);

		if (node == null) {
			return null;
		}

		return node;
	}

	public CachedGraphNode<T> createNode(EObject eObject, EStructuralFeature feature, T value) {
		final UniqueSetting setting = UniqueSetting.createSetting(eObject, feature);
		CachedGraphNode<T> node = settings.get(setting);

		if (node == null) {
			node = createNode(setting, value);
			settings.put(setting, node);
		} else {
			node.setValue(value);
		}

		return node;
	}

	private CachedGraphNode<T> createNode(UniqueSetting setting, T initValue) {
		return new CachedGraphNode<T>(setting, initValue, isDomainObject(setting.getEObject()), comparator);
	}

	/**
	 * Check whether the given {@code child} is part of the domain model.
	 * Note that this method may returns {@code false} in case {@code child} has been deleted.
	 * 
	 * @param object
	 *            the child to be checked
	 * @return {@code true} if the child is part of the domain model, {@code false} otherwise
	 */
	private boolean isDomainObject(EObject object) {
		return object != null && isContainedInModel(object);
	}

	/**
	 * Removes the given {@link EObject} and all its {@link UniqueSetting}s from the map
	 * 
	 * @param eObject
	 */
	public void removeAll(EObject eObject) {
		final EList<EStructuralFeature> eAllStructuralFeatures = eObject.eClass().getEAllStructuralFeatures();
		for (final EStructuralFeature eStructuralFeature : eAllStructuralFeatures) {
			final UniqueSetting setting = UniqueSetting.createSetting(eObject, eStructuralFeature);
			settings.remove(setting);
		}
	}

	private boolean isContainedInModel(EObject possibleChild) {

		if (model == possibleChild) {
			return true;
		}

		if (possibleChild == null) {
			return false;
		}

		EObject container = possibleChild;
		while (container != null) {
			if (container == model) {
				return true;
			}
			container = container.eContainer();
		}

		return false;
	}

	public static EStructuralFeature allFeatures() {
		return ALL_SETTINGS;
	}
}
