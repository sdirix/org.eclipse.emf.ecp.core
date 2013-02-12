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
package org.eclipse.emf.ecp.edit.internal.swt.actions;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.ui.ISharedImages;

/**
 * The action to allow adding of attribute values to multi attribute controls.
 * @author Eugen Neufeld
 * 
 */
public class AddAttributeAction extends ECPSWTAction {

	
	/**
	 * The constructor for the add attribute action.
	 * @param modelElementContext the {@link EditModelElementContext} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 */
	public AddAttributeAction(EditModelElementContext modelElementContext,
		IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature) {
		super(modelElementContext, itemPropertyDescriptor, feature);
		// TODO remove PlatformUI
		setImageDescriptor(Activator.getDefault().getWorkbench().getSharedImages()
			.getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		setToolTipText("Add Entry");
	}

	@Override
	public void run() {
		super.run();
		AddCommand.create(getModelElementContext().getEditingDomain(), getModelElementContext().getModelElement(),
			getFeature(), getFeature().getEType().getDefaultValue()).execute();

	}
}
