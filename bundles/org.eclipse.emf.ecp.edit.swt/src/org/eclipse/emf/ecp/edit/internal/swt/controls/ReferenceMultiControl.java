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
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.actions.AddReferenceAction;
import org.eclipse.emf.ecp.edit.internal.swt.actions.ECPSWTAction;
import org.eclipse.emf.ecp.edit.internal.swt.actions.NewReferenceAction;
import org.eclipse.emf.ecp.edit.util.StaticApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
/**
 * This class defines a Control which is used for displaying {@link EStructuralFeature}s which have a multi reference.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ReferenceMultiControl extends MultiControl {
	/**
	 * Constructor for a multi reference control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public ReferenceMultiControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}

	@Override
	protected ECPSWTAction[] instantiateActions() {
		ECPSWTAction[] actions = new ECPSWTAction[2];
			actions[0]=new AddReferenceAction(getModelElementContext(), getItemPropertyDescriptor(), getStructuralFeature());
			actions[1]=new NewReferenceAction(getModelElementContext(), getItemPropertyDescriptor(), getStructuralFeature());
			return actions;
	}
	@Override
	protected int getTesterPriority(StaticApplicableTester tester, IItemPropertyDescriptor itemPropertyDescriptor,
		EObject eObject) {
		return ReferenceMultiControlTester.getTesterPriority(tester, itemPropertyDescriptor, eObject);
	}
}
