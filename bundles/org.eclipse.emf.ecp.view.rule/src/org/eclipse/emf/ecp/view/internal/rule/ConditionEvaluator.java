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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
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
	 * @param eObject
	 *            the affected object the condition's attribute is pointing at
	 * @param condition
	 *            the condition to be evaluated
	 * @return {@code true}, if the condition matches, {@code false} otherwise
	 */
	public static boolean evaluate(EObject eObject, Condition condition) {

		if (AndCondition.class.isInstance(condition)) {
			return doEvaluate(eObject, (AndCondition) condition);
		}
		if (OrCondition.class.isInstance(condition)) {
			return doEvaluate(eObject, (OrCondition) condition);
		}
		if (LeafCondition.class.isInstance(condition)) {
			return doEvaluate(eObject, (LeafCondition) condition);
		}
		return false;
	}

	/**
	 * Evaluates the given condition.
	 * 
	 * @param newValue
	 *            the new value that should be compared against the expected value of the condition
	 * @param condition
	 *            the condition to be evaluated
	 * @return {@code true}, if the condition matches, {@code false} otherwise
	 */
	public static boolean evaluate(Object newValue, Condition condition) {

		if (AndCondition.class.isInstance(condition)) {
			return doEvaluate(newValue, (AndCondition) condition);
		}
		if (OrCondition.class.isInstance(condition)) {
			return doEvaluate(newValue, (OrCondition) condition);
		}
		if (LeafCondition.class.isInstance(condition)) {
			return doEvaluate(newValue, (LeafCondition) condition);
		}
		return false;
	}

	private static boolean doEvaluate(Object newValue, AndCondition condition) {
		boolean result = true;
		for (final Condition innerCondition : condition.getConditions()) {
			result &= evaluate(newValue, innerCondition);
		}
		return result;
	}

	private static boolean doEvaluate(Object newValue, OrCondition condition) {
		boolean result = false;
		for (final Condition innerCondition : condition.getConditions()) {
			result |= evaluate(newValue, innerCondition);
		}
		return result;
	}

	private static boolean doEvaluate(EObject eObject, AndCondition condition) {
		boolean result = true;
		for (final Condition innerCondition : condition.getConditions()) {
			result &= evaluate(eObject, innerCondition);
		}
		return result;
	}

	private static boolean doEvaluate(EObject eObject, OrCondition condition) {
		boolean result = false;
		for (final Condition innerCondition : condition.getConditions()) {
			result |= evaluate(eObject, innerCondition);
		}
		return result;
	}

	private static boolean doEvaluate(Object newValue, LeafCondition condition) {
		return condition.getExpectedValue().equals(newValue);
	}

	private static boolean doEvaluate(EObject eObject, LeafCondition condition) {
		final EClass attributeClass = condition.getDomainModelReference().getModelFeature().getEContainingClass();
		final EObject parent = condition.getDomainModelReference().getDomainModel();
		// final List<EReference> referencePath = condition.getPathToAttribute();
		// for (final EReference eReference : referencePath) {
		// if (eReference.getEReferenceType().isInstance(parent)) {
		// break;
		// }
		// if (!eReference.getEContainingClass().equals(parent.eClass())) {
		// continue;
		// }
		//
		// final EObject child = (EObject) parent.eGet(eReference);
		//
		// if (child == null) {
		// break;
		// }
		//
		// parent = child;
		// }

		if (!attributeClass.isInstance(parent)) {
			return false;
		}

		return condition.getExpectedValue().equals(parent.eGet(condition.getDomainModelReference().getModelFeature()));
	}
}
