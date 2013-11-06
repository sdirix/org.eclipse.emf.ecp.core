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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.common.BidirectionalMap;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.rule.model.AndCondition;
import org.eclipse.emf.ecp.view.rule.model.Condition;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.OrCondition;
import org.eclipse.emf.ecp.view.rule.model.Rule;
import org.eclipse.emf.ecp.view.rule.model.impl.LeafConditionImpl;

/**
 * Rule registry that maintains which {@link VElement}s
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
	private final Map<EStructuralFeature, BidirectionalMap<LeafCondition, T>> featuresToRules;
	private final BidirectionalMap<T, VElement> rulesToRenderables;

	/**
	 * Default constructor.
	 */
	public RuleRegistry() {
		featuresToRules = new LinkedHashMap<EStructuralFeature, BidirectionalMap<LeafCondition, T>>();
		rulesToRenderables = new BidirectionalMap<T, VElement>();
	}

	/**
	 * Creates a setting from the given {@link EObject} and the {@link LeafCondition} and register it with the
	 * {@link VElement}.
	 * 
	 * @param renderable
	 *            the {@link VElement} to be updated in case the condition changes
	 * @param rule
	 *            the parent rule holding the {@link LeafCondition}
	 * @param condition
	 *            contains the attribute that the condition is depending on
	 * @param domainModel
	 *            the domain object that owns the attribute possibly being
	 *            changed
	 */
	public void register(VElement renderable, T rule, Condition condition, EObject domainModel) {

		if (condition instanceof LeafCondition) {
			final LeafCondition leafCondition = (LeafCondition) condition;

			leafCondition.getDomainModelReference().resolve(domainModel);

			final Iterator<EStructuralFeature> featureIterator = leafCondition.getDomainModelReference()
				.getEStructuralFeatureIterator();
			while (featureIterator.hasNext()) {
				final EStructuralFeature eStructuralFeature = featureIterator.next();
				mapFeatureToRule(eStructuralFeature, leafCondition, rule);
			}
			rulesToRenderables.put(rule, renderable);

		} else if (condition instanceof OrCondition) {
			final OrCondition orCondition = (OrCondition) condition;
			for (final Condition cond : orCondition.getConditions()) {
				register(renderable, rule, cond, domainModel);
			}
		} else if (condition instanceof AndCondition) {
			final AndCondition andCondition = (AndCondition) condition;
			for (final Condition cond : andCondition.getConditions()) {
				register(renderable, rule, cond, domainModel);
			}
		} else {
			mapFeatureToRule(AllEAttributes.get(), noCondition, rule);
			rulesToRenderables.put(rule, renderable);
		}
	}

	/**
	 * Removes the given rule from the registry.
	 * 
	 * @param rule
	 *            the rule to be removed
	 * @return the {@link VElement} that belonged to the removed rule
	 */
	public VElement removeRule(T rule) {
		final Condition condition = rule.getCondition();
		if (condition != null) {
			return removeCondition(condition);
		}

		final Collection<BidirectionalMap<LeafCondition, T>> values = featuresToRules.values();
		for (final BidirectionalMap<LeafCondition, T> bidirectionalMap : values) {
			if (bidirectionalMap.removeByValue(rule) != null) {
				break;
			}
		}

		final VElement renderable = rulesToRenderables.getValue(rule);
		rulesToRenderables.removeByKey(rule);
		return renderable;

	}

	/**
	 * Removes the given {@link VElement} from the registry.
	 * 
	 * @param renderable
	 *            the renderable to be removed
	 */
	public void removeRenderable(VElement renderable) {
		rulesToRenderables.removeByValue(renderable);
	}

	/**
	 * Removes the given condition from the registry.
	 * 
	 * @param condition
	 *            the condition to be removed
	 * @return the {@link VElement} that belonged to the removed condition
	 */
	public VElement removeCondition(Condition condition) {
		VElement ret = null;
		// we only have to care about leaf conditions since or/and conditions aren't even registered
		if (LeafCondition.class.isInstance(condition)) {
			final LeafCondition leafCondition = LeafCondition.class.cast(condition);
			final Iterator<Setting> settingIterator = leafCondition.getDomainModelReference().getIterator();

			final Setting setting = settingIterator.next();

			final BidirectionalMap<LeafCondition, T> bidirectionalMap = featuresToRules.get(setting
				.getEStructuralFeature());

			if (bidirectionalMap != null) {
				final T removeByKey = bidirectionalMap.removeByKey(leafCondition);
				ret = rulesToRenderables.removeByKey(removeByKey);
			}
		}

		return ret;
	}

	private void mapFeatureToRule(final EStructuralFeature attribute, LeafCondition condition, T rule) {
		if (!featuresToRules.containsKey(attribute)) {
			featuresToRules.put(attribute, new BidirectionalMap<LeafCondition, T>());
		}
		featuresToRules.get(attribute).put(condition, rule);
	}

	/**
	 * Returns the settings of this registry.
	 * 
	 * @return the settings of this registry.
	 */
	public Set<EStructuralFeature> getAttributes() {
		return featuresToRules.keySet();
	}

	/**
	 * Returns all rules that would be affected if the value of given feature is changed.
	 * 
	 * @param feature
	 *            the feature
	 * @return a list of {@link VElement}s that are affected of the feature change
	 */
	public Map<T, VElement> getAffectedRenderables(final EStructuralFeature feature) {

		final Map<T, VElement> result = new LinkedHashMap<T, VElement>();
		BidirectionalMap<LeafCondition, T> bidirectionalMap = featuresToRules.get(feature);

		if (bidirectionalMap == null) {
			bidirectionalMap = featuresToRules.get(AllEAttributes.get());
		}

		if (bidirectionalMap == null) {
			return Collections.emptyMap();
		}

		for (final T rule : bidirectionalMap.values()) {
			final VElement renderable = rulesToRenderables.getValue(rule);
			result.put(rule, renderable);
		}

		return result;
	}
}
