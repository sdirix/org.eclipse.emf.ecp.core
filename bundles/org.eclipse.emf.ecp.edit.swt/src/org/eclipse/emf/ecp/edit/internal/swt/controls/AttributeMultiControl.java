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
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.internal.swt.actions.AddAttributeAction;
import org.eclipse.emf.ecp.edit.internal.swt.actions.ECPSWTAction;
import org.eclipse.emf.ecp.edit.util.ECPStaticApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * This class defines a Control which is used for displaying {@link EStructuralFeature}s which have a multi attribute.
 * 
 * @author Eugen Neufeld
 * 
 */
public class AttributeMultiControl extends MultiControl {

	@Override
	protected ECPSWTAction[] instantiateActions() {
		final ECPSWTAction[] actions = new ECPSWTAction[1];
		actions[0] = new AddAttributeAction(getModelElementContext(), getItemPropertyDescriptor(),
			getStructuralFeature());
		return actions;
	}

	@Override
	protected int getTesterPriority(ECPStaticApplicableTester tester, IItemPropertyDescriptor itemPropertyDescriptor,
		EObject eObject) {
		return AttributeMultiControlTester.getTesterPriority(tester,
			(EStructuralFeature) itemPropertyDescriptor.getFeature(eObject), eObject);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getUnsetLabelText()
	 */
	@Override
	protected String getUnsetLabelText() {
		return ControlMessages.AttributeMultiControl_NotSetClickToSet;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getUnsetButtonTooltip()
	 */
	@Override
	protected String getUnsetButtonTooltip() {
		return ControlMessages.AttributeMultiControl_Unset;
	}

}
