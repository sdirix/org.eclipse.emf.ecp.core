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
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.rule;

import java.util.Iterator;
import java.util.List;

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
 * 
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
	 * @param possibleNewValue
	 *            the new value that should be compared against the expected value of the condition
	 * @param condition
	 *            the condition to be evaluated
	 * @return {@code true}, if the condition matches, {@code false} otherwise
	 */
	public static boolean evaluate(Object possibleNewValue, Condition condition) {

		if (AndCondition.class.isInstance(condition)) {
			return doEvaluate(possibleNewValue, (AndCondition) condition);
		}
		if (OrCondition.class.isInstance(condition)) {
			return doEvaluate(possibleNewValue, (OrCondition) condition);
		}
		if (LeafCondition.class.isInstance(condition)) {
			return doEvaluate(possibleNewValue, (LeafCondition) condition);
		}
		return false;
	}

	private static boolean doEvaluate(Object possibleNewValue, AndCondition condition) {
		boolean result = true;
		for (final Condition innerCondition : condition.getConditions()) {
			result &= evaluate(possibleNewValue, innerCondition);
		}
		return result;
	}

	private static boolean doEvaluate(Object possibleNewValue, OrCondition condition) {
		boolean result = false;
		for (final Condition innerCondition : condition.getConditions()) {
			result |= evaluate(possibleNewValue, innerCondition);
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

	private static boolean doEvaluate(Object possibleNewValue, LeafCondition condition) {
		final Iterator<Setting> settingIterator = condition.getDomainModelReference().getIterator();
		boolean result = false;
		final Object expectedValue = condition.getExpectedValue();

		// FIXME: duplicate code
		while (settingIterator.hasNext()) {
			final Setting setting = settingIterator.next();
			final EObject parent = setting.getEObject();
			final EStructuralFeature feature = setting.getEStructuralFeature();
			final EClass attributeClass = feature.getEContainingClass();
			if (!attributeClass.isInstance(parent)) {
				continue;
			}
			if (!feature.isMany()) {
				if (expectedValue == null) {
					result |= possibleNewValue == null;
				} else {
					result |= expectedValue.equals(possibleNewValue);
				}
			}
			else {
				// EMF API
				@SuppressWarnings("unchecked")
				final List<Object> objects = (List<Object>) possibleNewValue;
				result |= objects.contains(expectedValue);
			}
		}
		return result;
	}

	private static boolean doEvaluate(LeafCondition condition) {
		final Iterator<Setting> settingIterator = condition.getDomainModelReference().getIterator();
		boolean result = false;
		final Object expectedValue = condition.getExpectedValue();
		while (settingIterator.hasNext()) {
			final Setting setting = settingIterator.next();
			final EObject parent = setting.getEObject();
			final EStructuralFeature feature = setting.getEStructuralFeature();
			final EClass attributeClass = feature.getEContainingClass();
			if (!attributeClass.isInstance(parent)) {
				continue;
			}
			final Object actualValue = parent.eGet(feature);
			if (!feature.isMany()) {
				if (expectedValue == null) {
					result |= actualValue == null;
				} else {
					result |= expectedValue.equals(actualValue);
				}
			}
			else {
				// EMF API
				@SuppressWarnings("unchecked")
				final List<Object> objects = (List<Object>) actualValue;
				result |= objects.contains(condition.getExpectedValue());
			}
		}
		return result;
	}
}
