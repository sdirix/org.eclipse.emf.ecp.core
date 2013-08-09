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
package org.eclipse.emf.ecp.view.rule;

import static org.eclipse.emf.ecp.view.rule.ReadOnlySetting.createSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.CompositeCollection;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.Rule;

/**
 * Rule registry that maintains which {@link Renderable}s
 * are affected if a {@link Setting} is changed.
 * 
 * @author emueller
 * 
 * @param <T>
 *            the actual {@link Rule} type
 */
public class RuleRegistry<T extends Rule> {

	private final Map<Setting, List<T>> settingToRules;
	private final Map<T, Renderable> ruleToRenderable;

	/**
	 * Default constructor.
	 */
	public RuleRegistry() {
		settingToRules = new LinkedHashMap<Setting, List<T>>();
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
	 * @param leafCondition
	 *            contains the attribute that the condition is depending on
	 * @param domainModel
	 *            the domain object that owns the attribute possibly being
	 *            changed
	 */
	public void register(Renderable renderable, T rule, LeafCondition leafCondition, EObject domainModel) {

		final Setting setting = createSetting(domainModel, leafCondition.getAttribute());

		if (!settingToRules.containsKey(setting)) {
			settingToRules.put(setting, new ArrayList<T>());
		}

		if (!settingToRules.get(setting).contains(rule)) {
			settingToRules.get(setting).add(rule);
			ruleToRenderable.put(rule, renderable);
		}
	}

	/**
	 * Returns all rules that would be affected if the value of given setting is changed.
	 * 
	 * @param setting
	 *            the setting
	 * @return a list of {@link Renderable}s that are affected of the setting changed
	 */
	public List<Renderable> getAffectedRenderables(final Setting setting) {

		final List<Renderable> collections = new ArrayList<Renderable>();
		final List<Renderable> leafs = new ArrayList<Renderable>();

		final List<T> rules = settingToRules.get(setting);

		if (rules == null) {
			return Collections.emptyList();
		}

		for (final T rule : rules) {
			final Renderable renderable = ruleToRenderable.get(rule);
			if (renderable instanceof CompositeCollection) {
				collections.add(renderable);
			} else {
				leafs.add(renderable);
			}
		}

		leafs.addAll(collections);

		return leafs;
	}
}
