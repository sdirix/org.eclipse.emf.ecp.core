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

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.context.AbstractViewService;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * @author emueller
 * 
 */
public class RuleServiceHelper extends AbstractViewService {

	private interface RenderablePredicate {

		boolean checkCurrentState(Renderable renderable);
	}

	private ViewModelContext context;

	/**
	 * Gets the involved {@link org.eclipse.emf.ecore.EObject EObjects}.
	 * 
	 * @param <T>
	 *            the type of the returned {@link EObject}s
	 * 
	 * @param setting
	 *            the setting
	 * @param newValue
	 *            the new value
	 * @param cls
	 *            the class type that has to be matched. Used for filtering
	 * @return the involved {@link Renderable}s that match the given type {@code T}
	 */
	public <T extends Renderable> Set<T> getInvolvedEObjects(Setting setting, Object newValue, Class<T> cls) {

		final Map<Renderable, Boolean> disabledRenderables = context.getService(RuleService.class)
			.getDisabledRenderables(setting, newValue);
		final Map<Renderable, Boolean> hiddenRenderables = context.getService(RuleService.class).getHiddenRenderables(
			setting, newValue);

		final Set<T> result = new LinkedHashSet<T>();
		result.addAll(collectFalseValues(cls, disabledRenderables, new RenderablePredicate() {

			public boolean checkCurrentState(Renderable renderable) {
				return renderable.isEnabled();
			}
		}));
		result.addAll(collectFalseValues(cls, hiddenRenderables, new RenderablePredicate() {

			public boolean checkCurrentState(Renderable renderable) {
				return renderable.isVisible();
			}

		}));

		return result;
	}

	@SuppressWarnings("unchecked")
	private <T extends Renderable> Set<T> collectFalseValues(Class<T> cls,
		final Map<Renderable, Boolean> renderableToStateMapping, RenderablePredicate renderablePredicate) {

		final Set<T> result = new LinkedHashSet<T>();

		for (final Map.Entry<Renderable, Boolean> entry : renderableToStateMapping.entrySet()) {

			final Renderable renderable = entry.getKey();
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
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#instantiate(org.eclipse.emf.ecp.view.context.ViewModelContext)
	 */
	@Override
	public void instantiate(ViewModelContext context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#dispose()
	 */
	@Override
	public void dispose() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#getPriority()
	 */
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 2;
	}
}
