/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * Edgar Mueller - support for handling multiple possible new values
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.rule;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.rule.model.AndCondition;
import org.eclipse.emf.ecp.view.rule.model.Condition;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.OrCondition;

/**
 * Evaluates a given condition.
 * 
 * @author Eugen Neufeld
 * @author emueller
 */
public final class ConditionEvaluator {

	private ConditionEvaluator() {

	}

	/**
	 * Evaluates the given condition.
	 * 
	 * @param condition
	 *            the condition to be evaluated
	 * @return {@code true}, if the condition matches, {@code false} otherwise
	 */
	public static boolean evaluate(Condition condition) {

		if (AndCondition.class.isInstance(condition)) {
			return doEvaluate((AndCondition) condition);
		}
		if (OrCondition.class.isInstance(condition)) {
			return doEvaluate((OrCondition) condition);
		}
		if (LeafCondition.class.isInstance(condition)) {
			return doEvaluate((LeafCondition) condition);
		}
		return false;
	}

	/**
	 * Evaluates the given condition.
	 * 
	 * @param possibleNewValues
	 *            the new value that should be compared against the expected value of the condition
	 * @param condition
	 *            the condition to be evaluated
	 * @return {@code true}, if the condition matches, {@code false} otherwise
	 */
	public static boolean evaluate(Map<Setting, Object> possibleNewValues, Condition condition) {

		if (AndCondition.class.isInstance(condition)) {
			return doEvaluate(possibleNewValues, (AndCondition) condition);
		}
		if (OrCondition.class.isInstance(condition)) {
			return doEvaluate(possibleNewValues, (OrCondition) condition);
		}
		if (LeafCondition.class.isInstance(condition)) {
			return doEvaluate(possibleNewValues, (LeafCondition) condition);
		}
		return false;
	}

	private static boolean doEvaluate(Map<Setting, Object> possibleNewValues, AndCondition condition) {
		boolean result = true;
		for (final Condition innerCondition : condition.getConditions()) {
			result &= evaluate(possibleNewValues, innerCondition);
		}
		return result;
	}

	private static boolean doEvaluate(Map<Setting, Object> possibleNewValues, OrCondition condition) {
		boolean result = false;
		for (final Condition innerCondition : condition.getConditions()) {
			result |= evaluate(possibleNewValues, innerCondition);
		}
		return result;
	}

	private static boolean doEvaluate(AndCondition condition) {
		boolean result = true;
		for (final Condition innerCondition : condition.getConditions()) {
			result &= evaluate(innerCondition);
		}
		return result;
	}

	private static boolean doEvaluate(OrCondition condition) {
		boolean result = false;
		for (final Condition innerCondition : condition.getConditions()) {
			result |= evaluate(innerCondition);
		}
		return result;
	}

	private static boolean doEvaluate(Map<Setting, Object> possibleNewValues, LeafCondition condition) {

		boolean result = false;
		final Object expectedValue = condition.getExpectedValue();

		for (final Setting setting : possibleNewValues.keySet()) {
			try {
				result |= doEvaluate(setting, expectedValue, true, possibleNewValues.get(setting));
			} catch (final NotApplicableForEvaluationException e) {
				continue;
			}
		}

		return result;
	}

	private static boolean doEvaluate(LeafCondition condition) {
		final Iterator<Setting> settingIterator = condition.getDomainModelReference().getIterator();
		boolean result = false;
		final Object expectedValue = condition.getExpectedValue();
		while (settingIterator.hasNext()) {
			try {
				result |= doEvaluate(settingIterator.next(), expectedValue, false, null);
			} catch (final NotApplicableForEvaluationException e) {
				continue;
			}
		}
		return result;
	}

	private static boolean doEvaluate(Setting setting, Object expectedValue, boolean useNewValue, Object newValue)
		throws NotApplicableForEvaluationException {

		final EObject parent = setting.getEObject();
		final EStructuralFeature feature = setting.getEStructuralFeature();
		final EClass attributeClass = feature.getEContainingClass();
		if (!attributeClass.isInstance(parent)) {
			throw new NotApplicableForEvaluationException();
		}
		Object value;
		if (!useNewValue) {
			value = parent.eGet(feature);
		} else {
			value = newValue;
		}
		if (!feature.isMany()) {
			if (expectedValue == null) {
				return value == null;
			}

			return expectedValue.equals(value);
		}

		// EMF API
		@SuppressWarnings("unchecked")
		final List<Object> objects = (List<Object>) value;
		return objects.contains(expectedValue);
	}

}
