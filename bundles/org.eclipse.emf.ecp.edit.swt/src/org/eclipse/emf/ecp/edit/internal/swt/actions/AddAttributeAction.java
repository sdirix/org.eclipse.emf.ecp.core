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
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import java.lang.reflect.InvocationTargetException;

/**
 * The action to allow adding of attribute values to multi attribute controls.
 * 
 * @author Eugen Neufeld
 * 
 */
public class AddAttributeAction extends ECPSWTAction {

	/**
	 * The constructor for the add attribute action.
	 * 
	 * @param modelElementContext the {@link ECPControlContext} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 */
	public AddAttributeAction(ECPControlContext modelElementContext, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature) {
		super(modelElementContext, itemPropertyDescriptor, feature);
		// TODO remove PlatformUI
		setImageDescriptor(Activator.getImageDescriptor("icons/add.png")); //$NON-NLS-1$
		setToolTipText(ActionMessages.AddAttributeAction_AddEntry);
	}

	@Override
	public void run() {
		super.run();
		// TODO show message if something goes wrong
		Object defaultValue = getFeature().getEType().getDefaultValue();
		if (defaultValue == null) {
			try {
				defaultValue = getFeature().getEType().getInstanceClass().getConstructor().newInstance();
			} catch (InstantiationException e) {
				Activator.logException(e);
			} catch (IllegalAccessException e) {
				Activator.logException(e);
			} catch (IllegalArgumentException e) {
				Activator.logException(e);
			} catch (InvocationTargetException e) {
				Activator.logException(e);
			} catch (NoSuchMethodException e) {
				Activator.logException(e);
			} catch (SecurityException e) {
				Activator.logException(e);
			}
		}
		getModelElementContext()
			.getEditingDomain()
			.getCommandStack()
			.execute(
				AddCommand.create(getModelElementContext().getEditingDomain(), getModelElementContext()
					.getModelElement(), getFeature(), defaultValue));

	}
}
