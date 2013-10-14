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
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.common.BidirectionalMap;
import org.eclipse.emf.ecp.common.UniqueSetting;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.rule.model.AndCondition;
import org.eclipse.emf.ecp.view.rule.model.Condition;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.OrCondition;
import org.eclipse.emf.ecp.view.rule.model.Rule;
import org.eclipse.emf.ecp.view.rule.model.impl.LeafConditionImpl;

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

	/** Helper class for use with a rule that does not have a condition set. **/
	static class NoCondition extends LeafConditionImpl {
	}

	private static NoCondition noCondition = new NoCondition();
	private final Map<UniqueSetting, BidirectionalMap<LeafCondition, T>> settingToRules;
	private final BidirectionalMap<T, Renderable> rulesToRenderables;

	/**
	 * Default constructor.
	 */
	public RuleRegistry() {
		settingToRules = new LinkedHashMap<UniqueSetting, BidirectionalMap<LeafCondition, T>>();
		rulesToRenderables = new BidirectionalMap<T, Renderable>();
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
			mapSettingToRule(createSetting(domainModel, AllEAttributes.get()), noCondition, rule);
			rulesToRenderables.put(rule, renderable);
		} else if (condition instanceof LeafCondition) {
			final LeafCondition leafCondition = (LeafCondition) condition;

			leafCondition.getDomainModelReference().resolve(domainModel);

			final Iterator<Setting> settingIterator = leafCondition.getDomainModelReference().getIterator();
			while (settingIterator.hasNext()) {
				final Setting setting = settingIterator.next();
				// unique settings are needed to compare settings with each other.
				// The default implementation of ESettting
				// does not provide and appropriate equals()/hashCode() implementation
				final UniqueSetting uniqueSetting = createSetting(setting.getEObject(), setting.getEStructuralFeature());

				mapSettingToRule(uniqueSetting, leafCondition, rule);

				rulesToRenderables.put(rule, renderable);
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
	 * @return the {@link Renderable} that belonged to the removed rule
	 */
	public Renderable removeRule(T rule) {
		final Condition condition = rule.getCondition();
		if (condition != null) {
			return removeCondition(condition);
		}

		final Collection<BidirectionalMap<LeafCondition, T>> values = settingToRules.values();
		for (final BidirectionalMap<LeafCondition, T> bidirectionalMap : values) {
			if (bidirectionalMap.removeByValue(rule) != null) {
				break;
			}
		}

		final Renderable renderable = rulesToRenderables.getValue(rule);
		rulesToRenderables.removeByKey(rule);
		return renderable;

	}

	/**
	 * Removes the given {@link Renderable} from the registry.
	 * 
	 * @param renderable
	 *            the renderable to be removed
	 */
	public void removeRenderable(Renderable renderable) {
		rulesToRenderables.removeByValue(renderable);
	}

	/**
	 * Removes the given condition from the registry.
	 * 
	 * @param condition
	 *            the condition to be removed
	 * @return the {@link Renderable} that belonged to the removed condition
	 */
	public Renderable removeCondition(Condition condition) {
		Renderable ret = null;
		// we only have to care about leaf conditions since or/and conditions aren't even registered
		if (LeafCondition.class.isInstance(condition)) {
			final LeafCondition leafCondition = LeafCondition.class.cast(condition);
			final Iterator<Setting> settingIterator = leafCondition.getDomainModelReference().getIterator();

			final Setting setting = settingIterator.next();
			final UniqueSetting uniqueSetting = createSetting(setting.getEObject(), setting.getEStructuralFeature());

			final BidirectionalMap<LeafCondition, T> bidirectionalMap = settingToRules.get(uniqueSetting);
			if (bidirectionalMap != null) {
				final T removeByKey = bidirectionalMap.removeByKey(leafCondition);
				ret = rulesToRenderables.removeByKey(removeByKey);
			}
		}

		return ret;
	}

	private void mapSettingToRule(final UniqueSetting setting, LeafCondition condition, T rule) {
		if (!settingToRules.containsKey(setting)) {
			settingToRules.put(setting, new BidirectionalMap<LeafCondition, T>());
		}
		settingToRules.get(setting).put(condition, rule);
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
		BidirectionalMap<LeafCondition, T> bidirectionalMap = settingToRules.get(setting);

		if (bidirectionalMap == null) {
			bidirectionalMap = settingToRules.get(createSetting(setting.getEObject(), AllEAttributes.get()));
		}

		if (bidirectionalMap == null) {
			return Collections.emptyMap();
		}

		for (final T rule : bidirectionalMap.values()) {
			final Renderable renderable = rulesToRenderables.getValue(rule);
			result.put(rule, renderable);
		}

		return result;
	}
}
