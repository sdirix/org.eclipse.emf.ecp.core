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
package org.eclipse.emf.ecp.view.editor.controls;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * @author Eugen Neufeld
 * 
 */
public class RuleFeaturePathControl extends ControlTargetFeatureControl {

	/**
	 * This control is only showed for {@link org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference
	 * VFeaturePathDomainModelReferences} which
	 * are beneath a {@link org.eclipse.emf.ecp.view.rule.model.LeafCondition LeafCondition}.
	 * 
	 * @param showLabel whether a label should be shown
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} of this control
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param embedded whether this control is embedded
	 */
	public RuleFeaturePathControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.editor.controls.ControlTargetFeatureControl#allowMultiSelection()
	 */
	@Override
	protected boolean allowMultiSelection() {
		return true;
	}

}
