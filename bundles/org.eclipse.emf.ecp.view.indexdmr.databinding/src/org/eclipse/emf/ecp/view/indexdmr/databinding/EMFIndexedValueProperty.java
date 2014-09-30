/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.indexdmr.databinding;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.internal.EMFValueProperty;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class provides a ValueProperty for EClass Mappings.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
public class EMFIndexedValueProperty extends EMFValueProperty {

	private final int index;

	/**
	 * Constructor for a EClassMapping ValueProperty.
	 * 
	 * @param mappedEClass the EClass being mapped
	 * @param eStructuralFeature the {@link EStructuralFeature} of the map
	 */
	public EMFIndexedValueProperty(int index,
		EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
		this.index = index;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doGetValue(Object source) {
		final Object result = super.doGetValue(source);
		final EList<Object> list = (EList<Object>) result;
		return list.get(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetValue(Object source, Object value) {
		final Object result = super.doGetValue(source);
		final EList<Object> list = (EList<Object>) result;
		list.add(index,value);
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += " index " + index; //$NON-NLS-1$
		return s;
	}
}
