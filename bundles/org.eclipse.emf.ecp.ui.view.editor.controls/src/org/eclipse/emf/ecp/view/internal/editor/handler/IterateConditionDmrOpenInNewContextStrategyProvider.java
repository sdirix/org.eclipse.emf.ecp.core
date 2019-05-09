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
package org.eclipse.emf.ecp.view.internal.editor.handler;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.view.spi.editor.controls.EStructuralFeatureSelectionValidator;
import org.eclipse.emf.ecp.view.spi.rule.model.IterateCondition;
import org.eclipse.emf.ecp.view.spi.rule.model.RulePackage;
import org.osgi.service.component.annotations.Component;

/**
 * Creates segment based item domain model references for {@link IterateCondition IterateConditions}.
 *
 * @author Lucas Koehler
 *
 */
@Component(name = "IterateConditionDmrOpenInNewContextStrategyProvider", property = "service.ranking:Integer=50")
public class IterateConditionDmrOpenInNewContextStrategyProvider
	extends RuleConditionDmrOpenInNewContextStrategyProvider {

	@Override
	protected boolean handles(EObject owner, EReference reference) {
		return isSegmentToolingEnabled()
			&& owner instanceof IterateCondition
			&& reference == RulePackage.Literals.ITERATE_CONDITION__ITEM_REFERENCE;
	}

	@Override
	protected EStructuralFeatureSelectionValidator getSelectionValidator() {
		return feature -> feature instanceof EReference && EReference.class.cast(feature).isMany()
			? null
			: "An iterate condition's domain model reference must point to a multi reference."; //$NON-NLS-1$
	}
}