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
package org.eclipse.emf.ecp.internal.editor.controls.attribute.multi;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecp.editor.controls.AbstractMultiControl;
import org.eclipse.emf.edit.command.AddCommand;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the default implementation for attribute multi controls.
 * 
 * @author Eugen Neufeld
 * 
 */
public abstract class AttributeMultiControl extends AbstractMultiControl {

	private Action[] actions;

	private static final Map<Class<?>, Class<?>> PRIMITIVES = new HashMap<Class<?>, Class<?>>();
	static {
		PRIMITIVES.put(Boolean.class, boolean.class);
		PRIMITIVES.put(Byte.class, byte.class);
		PRIMITIVES.put(Short.class, short.class);
		PRIMITIVES.put(Character.class, char.class);
		PRIMITIVES.put(Integer.class, int.class);
		PRIMITIVES.put(Long.class, long.class);
		PRIMITIVES.put(Float.class, float.class);
		PRIMITIVES.put(Double.class, double.class);
	}

	@Override
	protected boolean isAssignable(Class<?> featureClass) {
		if (featureClass.isPrimitive()) {
			return PRIMITIVES.get(getSupportedClassType()).isAssignableFrom(featureClass);
		}
		return super.isAssignable(featureClass);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.controls.attribute.multi.AbstractMultiControl#getToolbarActions()
	 */
	@Override
	protected Action[] getToolbarActions() {
		if (actions == null) {
			AddAction addAction = new AddAction();
			addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
			addAction.setToolTipText("Add Entry");
			actions = new Action[] { addAction };
		}
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.controls.AbstractControl#getEStructuralFeatureType()
	 */
	@Override
	protected Class<EAttribute> getEStructuralFeatureType() {
		return EAttribute.class;
	}

	/**
	 * This return the default Value which is set to the created attribute.
	 * 
	 * @return the Object being the default value
	 */
	protected abstract Object getDefaultValue();

	/**
	 * This class is the default AddAction being added for MultiAttributeControls.
	 * 
	 * @author Eugen Neufeld
	 * 
	 */
	private final class AddAction extends Action {

		@Override
		public void run() {
			AddCommand.create(getContext().getEditingDomain(), getContext().getModelElement(), getStructuralFeature(),
				getDefaultValue(), getNumElements()).execute();
			super.run();
		}
	}

}
