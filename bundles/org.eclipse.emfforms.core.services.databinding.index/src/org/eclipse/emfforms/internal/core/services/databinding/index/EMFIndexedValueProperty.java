/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * Lucas Koehler - moved, adjusted comments
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.databinding.index;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.internal.EMFValueProperty;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class provides a ValueProperty that supports addressing specific elements of a list by an index.
 *
 * @author Eugen Neufeld
 * @author Lucas Koehler
 *
 */
@SuppressWarnings("restriction")
public class EMFIndexedValueProperty extends EMFValueProperty {

	private final int index;

	/**
	 * Constructor for an Index ValueProperty.
	 *
	 * @param index The index
	 * @param eStructuralFeature
	 *            the {@link EStructuralFeature} of the indexed feature
	 */
	public EMFIndexedValueProperty(int index,
		EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
		if (index < 0) {
			throw new IllegalArgumentException(
				"\t \t \t \t \t Who thinks it's a good idea to use a negative list index?!"); //$NON-NLS-1$
		}
		this.index = index;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doGetValue(Object source) {
		final Object result = super.doGetValue(source);
		final EList<Object> list = (EList<Object>) result;
		if (list != null && index >= list.size()) {
			return null;
		}
		return list.get(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetValue(Object source, Object value) {
		final Object result = super.doGetValue(source);
		final EList<Object> list = (EList<Object>) result;
		list.add(index, value);
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += " index " + index; //$NON-NLS-1$
		return s;
	}
}
