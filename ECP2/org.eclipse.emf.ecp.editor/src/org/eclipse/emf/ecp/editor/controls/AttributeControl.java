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

package org.eclipse.emf.ecp.editor.controls;

import org.eclipse.emf.ecore.EAttribute;

import org.eclipse.swt.widgets.Composite;

import java.util.HashMap;
import java.util.Map;

/**
 * A control used for primitive types that may be represented by a textual representation.
 * 
 * @author helming
 * @author emueller
 * @author Eugen Neufeld
 */
public abstract class AttributeControl extends AbstractSingleControl {

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
	protected Class<EAttribute> getEStructuralFeatureType() {
		return EAttribute.class;
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
	 * @see org.eclipse.emf.ecp.editor.controls.AbstractSingleControl#getNumberOfAddtionalElements()
	 */
	@Override
	protected int getNumberOfAddtionalElements() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.editor.controls.AbstractSingleControl#createControlActions(org.eclipse.swt.widgets.Composite
	 * )
	 */
	@Override
	protected void createControlActions(Composite composite) {

	}

}
