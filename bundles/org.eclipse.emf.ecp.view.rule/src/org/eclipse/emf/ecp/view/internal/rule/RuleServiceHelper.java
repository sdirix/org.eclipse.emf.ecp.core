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
package org.eclipse.emf.ecp.view.internal.rule;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.context.ViewModelService;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.model.VElement;

/**
 * @author emueller
 * 
 */
public class RuleServiceHelper implements ViewModelService {

	/**
	 * A predicate that is used to determine the set of {@link VElement}s
	 * if {@link RuleServiceHelper#getInvolvedEObjects(Setting, Object, Class)} is called.
	 */
	private interface RenderablePredicate {
		boolean checkCurrentState(VElement renderable);
	}

	private ViewModelContext context;

	/**
	 * Gets the involved {@link org.eclipse.emf.ecore.EObject EObject}s that would be hidden
	 * or disabled if {@code newValue} would be set.
	 * 
	 * @param <T>
	 *            the type of the returned {@link EObject}s
	 * 
	 * @param setting
	 *            the setting
	 * @param newValue
	 *            the new value
	 * @param renderableClass
	 *            the class type that has to be matched. Used for filtering the result set
	 * @return the involved {@link VElement}s that match the given type {@code T}
	 */
	public <T extends VElement> Set<T> getInvolvedEObjects(Setting setting, Object newValue, EAttribute attribute,
		Class<T> renderableClass) {

		final Map<Setting, Object> newValues = new LinkedHashMap<EStructuralFeature.Setting, Object>();
		newValues.put(setting, newValue);

		final Map<VElement, Boolean> disabledRenderables = context.getService(RuleService.class)
			.getDisabledRenderables(newValues, attribute);
		final Map<VElement, Boolean> hiddenRenderables = context.getService(RuleService.class)
			.getHiddenRenderables(newValues, attribute);

		final Set<T> result = new LinkedHashSet<T>();

		result.addAll(collectFalseValues(renderableClass, disabledRenderables, createDisabledRenderablePredicate()));
		result.addAll(collectFalseValues(renderableClass, hiddenRenderables, createHiddenRenderablePredicate()));

		return result;
	}

	/**
	 * @return
	 */
	private RenderablePredicate createDisabledRenderablePredicate() {
		return new RenderablePredicate() {
			public boolean checkCurrentState(VElement renderable) {
				return renderable.isEnabled();
			}
		};
	}

	/**
	 * @return
	 */
	private RenderablePredicate createHiddenRenderablePredicate() {
		return new RenderablePredicate() {
			public boolean checkCurrentState(VElement renderable) {
				return renderable.isVisible();
			}

		};
	}

	/**
	 * Gets the involved {@link org.eclipse.emf.ecore.EObject EObject}s that would be hidden
	 * or disabled if {@code possibleNewValues} would be set.
	 * 
	 * @param <T>
	 *            the type of the returned {@link EObject}s
	 * 
	 * @param possibleNewValues
	 *            a mapping of settings to their would-be new value
	 * @param renderableClass
	 *            the class type that has to be matched. Used for filtering the result set
	 * @param changedAttribute the attribute that was changed
	 * @return the involved {@link VElement}s that match the given type {@code T}
	 */
	public <T extends VElement> Set<T> getInvolvedEObjects(Map<Setting, Object> possibleNewValues,
		EAttribute changedAttribute,
		Class<T> renderableClass) {

		final Set<T> result = new LinkedHashSet<T>();

		final Map<VElement, Boolean> hiddenRenderables = context.getService(RuleService.class)
			.getHiddenRenderables(possibleNewValues, changedAttribute);
		final Map<VElement, Boolean> disabledRenderables = context.getService(RuleService.class)
			.getDisabledRenderables(possibleNewValues, changedAttribute);
		result
			.addAll(collectFalseValues(renderableClass, disabledRenderables, createDisabledRenderablePredicate()));
		result.addAll(collectFalseValues(renderableClass, hiddenRenderables, createHiddenRenderablePredicate()));

		return result;
	}

	@SuppressWarnings("unchecked")
	private <T extends VElement> Set<T> collectFalseValues(Class<T> cls,
		final Map<VElement, Boolean> renderableToStateMapping, RenderablePredicate renderablePredicate) {

		final Set<T> result = new LinkedHashSet<T>();

		for (final Map.Entry<VElement, Boolean> entry : renderableToStateMapping.entrySet()) {

			final VElement renderable = entry.getKey();
			final Boolean newState = entry.getValue();
			if (newState) {
				continue;
			}
			if (renderablePredicate.checkCurrentState(renderable) == newState) {
				continue;
			}
			if (cls.isInstance(renderable)) {
				result.add((T) renderable);
			}
			else {
				EObject parent = renderable.eContainer();
				while (parent != null) {
					if (cls.isInstance(parent)) {
						result.add((T) parent);
						break;
					}
					parent = parent.eContainer();
				}
			}

		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelService#instantiate(org.eclipse.emf.ecp.view.context.ViewModelContext)
	 */
	public void instantiate(ViewModelContext context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelService#dispose()
	 */
	public void dispose() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelService#getPriority()
	 */
	public int getPriority() {
		return 2;
	}
}
