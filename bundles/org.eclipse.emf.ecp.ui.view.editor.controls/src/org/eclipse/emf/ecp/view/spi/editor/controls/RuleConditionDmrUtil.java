/*******************************************************************************
 * Copyright (c) 2011-2019 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.editor.controls;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.rule.model.Condition;
import org.eclipse.emf.ecp.view.spi.rule.model.IterateCondition;
import org.eclipse.emfforms.spi.common.report.AbstractReport;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.emf.EMFFormsDatabindingEMF;

/**
 * Utility class for common functionality needed for rule condition DMR tooling.
 *
 * @author Lucas Koehler
 * @since 1.21
 *
 */
public final class RuleConditionDmrUtil {

	private RuleConditionDmrUtil() {
		// do not instantiate utility class
	}

	/**
	 * Get the root EClass for the {@link VDomainModelReference} to create. If the rule {@link Condition} is contained
	 * in another Condition with a DMR, the root EClass is determined by resolving the container Condition(s) down from
	 * the domain root.
	 * Otherwise, the root EClass is the EClass of the domain root.
	 *
	 * @param databinding The {@link EMFFormsDatabindingEMF} used to resolve DMRs
	 * @param reportService The {@link ReportService} used to report databinding failures
	 * @param owner The condition containing the the DMR whose root EClass is needed
	 * @return The root EClass or nothing if it could not be determined
	 */
	public static Optional<EClass> getDmrRootEClass(EMFFormsDatabindingEMF databinding, ReportService reportService,
		EObject owner) {
		EObject testObject = owner;
		final List<IterateCondition> iterateConditions = new LinkedList<>();

		// Walk up the containment hierarchy up to the VView. Remember IterateConditions on the way
		while (!VView.class.isInstance(testObject) && testObject != null) {
			if (testObject instanceof IterateCondition) {
				iterateConditions.add((IterateCondition) testObject);
			}
			testObject = testObject.eContainer();
		}

		if (testObject instanceof VView) {
			// Reverse because we need to resolve top to bottom but added the elements bottom to top
			Collections.reverse(iterateConditions);
			EClass currentRoot = VView.class.cast(testObject).getRootEClass();
			// Resolve the nested iterate conditions from top (= view root EClass) to bottom
			for (final IterateCondition condition : iterateConditions) {
				final VDomainModelReference iterateDmr = condition.getItemReference();
				try {
					final IValueProperty<?, ?> valueProperty = databinding.getValueProperty(iterateDmr, currentRoot);
					if (valueProperty.getValueType() instanceof EReference) {
						currentRoot = EReference.class.cast(valueProperty.getValueType()).getEReferenceType();
					} else {
						return Optional.empty();
					}
				} catch (final DatabindingFailedException ex) {
					reportService.report(
						new AbstractReport(ex, "Could not determine root EClass for the DMR of Condition {0}.", owner)); //$NON-NLS-1$
					return Optional.empty();
				}
			}
			return Optional.of(currentRoot);
		}
		return Optional.empty();
	}
}
