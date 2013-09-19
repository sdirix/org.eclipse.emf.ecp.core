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

import static org.eclipse.emf.ecp.view.internal.rule.UniqueSetting.createSetting;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.rule.model.AndCondition;
import org.eclipse.emf.ecp.view.rule.model.Condition;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.OrCondition;
import org.eclipse.emf.ecp.view.rule.model.Rule;

/**
 * Rule registry that maintains which {@link Renderable}s
 * are affected if a setting is changed.
 * 
 * @author emueller
 * 
 * @param <T>
 *            the actual {@link Rule} type
 */
public class RuleRegistry<T extends Rule> {

	private final Map<UniqueSetting, Set<T>> settingToRules;
	private final Map<T, Renderable> ruleToRenderable;

	/**
	 * Default constructor.
	 */
	public RuleRegistry() {
		settingToRules = new LinkedHashMap<UniqueSetting, Set<T>>();
		ruleToRenderable = new LinkedHashMap<T, Renderable>();
	}

	/**
	 * Creates a setting from the given {@link EObject} and the {@link LeafCondition} and register it with the
	 * {@link Renderable}.
	 * 
	 * @param renderable
	 *            the {@link Renderable} to be updated in case the condition changes
	 * @param rule
	 *            the parent rule holding the {@link LeafCondition}
	 * @param condition
	 *            contains the attribute that the condition is depending on
	 * @param domainModel
	 *            the domain object that owns the attribute possibly being
	 *            changed
	 */
	public void register(Renderable renderable, T rule, Condition condition, EObject domainModel) {

		if (condition == null) {
			mapSettingToRule(createSetting(domainModel, AllEAttributes.get()), rule);
			ruleToRenderable.put(rule, renderable);
		} else if (condition instanceof LeafCondition) {
			final LeafCondition leafCondition = (LeafCondition) condition;

			leafCondition.getDomainModelReference().resolve(domainModel);
			final EObject target = leafCondition.getDomainModelReference().getDomainModel();

			if (target == null) {
				return;
			}

			final UniqueSetting setting = createSetting(target, leafCondition.getDomainModelReference()
				.getModelFeature());

			mapSettingToRule(setting, rule);

			ruleToRenderable.put(rule, renderable);

		} else if (condition instanceof OrCondition) {
			final OrCondition orCondition = (OrCondition) condition;
			for (final Condition cond : orCondition.getConditions()) {
				register(renderable, rule, cond, domainModel);
			}
		} else {
			final AndCondition andCondition = (AndCondition) condition;
			for (final Condition cond : andCondition.getConditions()) {
				register(renderable, rule, cond, domainModel);
			}
		}
	}

	private void mapSettingToRule(final UniqueSetting setting, T rule) {
		if (!settingToRules.containsKey(setting)) {
			settingToRules.put(setting, new LinkedHashSet<T>());
		}
		settingToRules.get(setting).add(rule);
	}

	// private static EObject getTargetEObject(LeafCondition condition, final EObject parent) {
	// final EList<EReference> pathToAttribute = condition.getDomainModelReference().getgetPathToAttribute();
	// EObject result = parent;
	// for (final EReference eReference : pathToAttribute) {
	//
	// EObject child = (EObject) result.eGet(eReference);
	//
	// if (child == null) {
	// child = EcoreUtil.create(eReference.getEReferenceType());
	// result.eSet(eReference, child);
	// }
	// result = child;
	// }
	//
	// return result;
	// }

	/**
	 * Returns the settings of this registry.
	 * 
	 * @return the settings of this registry.
	 */
	public Set<UniqueSetting> getSettings() {
		return settingToRules.keySet();
	}

	/**
	 * Returns all rules that would be affected if the value of given setting is changed.
	 * 
	 * @param setting
	 *            the setting
	 * @return a list of {@link Renderable}s that are affected of the setting changed
	 */
	public Map<Rule, Renderable> getAffectedRenderables(final UniqueSetting setting) {

		final Map<Rule, Renderable> result = new LinkedHashMap<Rule, Renderable>();
		Set<T> rules = settingToRules.get(setting);

		if (rules == null) {
			rules = settingToRules.get(createSetting(setting.getEObject(), AllEAttributes.get()));
		}

		if (rules == null) {
			return Collections.emptyMap();
		}

		for (final T rule : rules) {
			final Renderable renderable = ruleToRenderable.get(rule);
			result.put(rule, renderable);
		}

		return result;
	}
}
