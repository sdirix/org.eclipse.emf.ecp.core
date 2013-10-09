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

import static org.eclipse.emf.ecp.common.UniqueSetting.createSetting;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.common.UniqueSetting;
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
	private final BidirectionalMap<T, Renderable> ruleAndRenderables;

	/**
	 * Default constructor.
	 */
	public RuleRegistry() {
		settingToRules = new LinkedHashMap<UniqueSetting, Set<T>>();
		ruleAndRenderables = new BidirectionalMap<T, Renderable>();
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
			ruleAndRenderables.put(rule, renderable);
		} else if (condition instanceof LeafCondition) {
			final LeafCondition leafCondition = (LeafCondition) condition;

			leafCondition.getDomainModelReference().resolve(domainModel);

			final Iterator<Setting> settingIterator = leafCondition.getDomainModelReference().getIterator();
			while (settingIterator.hasNext()) {
				final Setting setting = settingIterator.next();
				// FIXME needed?
				// yes, because we need to compare settings with each other and the default implementation
				// does not provide and appropriate equals()/hashCode() implementation
				final UniqueSetting uniqueSetting = createSetting(setting.getEObject(), setting.getEStructuralFeature());

				mapSettingToRule(uniqueSetting, rule);

				ruleAndRenderables.put(rule, renderable);
			}

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

	/**
	 * Removes the given rule from the registry.
	 * 
	 * @param rule
	 *            the rule to be removed
	 */
	public void removeRule(T rule) {
		final Condition condition = rule.getCondition();
		if (condition != null) {
			removeCondition(condition, true);
		} else {
			final Collection<Set<T>> values = settingToRules.values();
			for (final Set<T> set : values) {
				set.remove(rule);
			}
			ruleAndRenderables.removeK(rule);
		}
	}

	/**
	 * Removes the given {@link Renderable} from the registry.
	 * 
	 * @param renderable
	 *            the renderable to be removed
	 */
	public void removeRenderable(Renderable renderable) {
		final T v = ruleAndRenderables.getV(renderable);
		ruleAndRenderables.removeV(renderable);
		final Collection<Set<T>> values = settingToRules.values();
		for (final Set<T> set : values) {
			set.remove(v);
		}
	}

	/**
	 * Removes the given condition from the registry.
	 * 
	 * @param condition
	 *            the condition to be removed
	 * @param removeSetting
	 *            whether the setting should also be removed. The setting has
	 *            an 1-n relationship with rules
	 */
	public void removeCondition(Condition condition, boolean removeSetting) {
		// we only have to care about leaf conditions since or/and conditions aren't even registered
		if (LeafCondition.class.isInstance(condition)) {
			final LeafCondition leafCondition = LeafCondition.class.cast(condition);
			final Iterator<Setting> settingIterator = leafCondition.getDomainModelReference().getIterator();
			while (settingIterator.hasNext()) {
				final Setting setting = settingIterator.next();
				// FIXME needed?
				// yes, because we need to compare settings with each other and the default implementation
				// does not provide and appropriate equals()/hashCode() implementation
				final UniqueSetting uniqueSetting = createSetting(setting.getEObject(), setting.getEStructuralFeature());

				final Set<T> set = settingToRules.get(uniqueSetting);

				if (set != null) {
					for (final T t : set) {
						ruleAndRenderables.removeK(t);
					}
				}

				if (removeSetting) {
					settingToRules.remove(uniqueSetting);
				}
			}
		}
	}

	/**
	 * Removes the condition from the registry.
	 * 
	 * @param condition
	 *            the condition to be removed
	 */
	public void removeCondition(Condition condition) {
		removeCondition(condition, false);
	}

	private void mapSettingToRule(final UniqueSetting setting, T rule) {
		if (!settingToRules.containsKey(setting)) {
			settingToRules.put(setting, new LinkedHashSet<T>());
		}
		settingToRules.get(setting).add(rule);
	}

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
			final Renderable renderable = ruleAndRenderables.getK(rule);
			result.put(rule, renderable);
		}

		return result;
	}
}
