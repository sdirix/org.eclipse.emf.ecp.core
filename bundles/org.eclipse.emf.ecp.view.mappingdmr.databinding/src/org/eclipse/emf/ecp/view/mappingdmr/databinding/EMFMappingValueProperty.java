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
package org.eclipse.emf.ecp.view.mappingdmr.databinding;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.databinding.internal.EMFValueProperty;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class provides a ValueProperty for EClass Mappings.
 *
 * @author Eugen Neufeld
 *
 */
@SuppressWarnings("restriction")
public class EMFMappingValueProperty extends EMFValueProperty {

	private final EClass mappedEClass;

	/**
	 * Constructor for a EClassMapping ValueProperty.
	 *
	 * @param mappedEClass the EClass being mapped
	 * @param eStructuralFeature the {@link EStructuralFeature} of the map
	 */
	public EMFMappingValueProperty(EClass mappedEClass,
		EStructuralFeature eStructuralFeature) {
		super(eStructuralFeature);
		this.mappedEClass = mappedEClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doGetValue(Object source) {
		final Object result = super.doGetValue(source);
		final EMap<EClass, Object> map = (EMap<EClass, Object>) result;
		return map.get(mappedEClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSetValue(Object source, Object value) {
		final Object result = super.doGetValue(source);
		final EMap<EClass, Object> map = (EMap<EClass, Object>) result;
		map.put(mappedEClass, value);
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += " mapping " + mappedEClass.getName(); //$NON-NLS-1$
		return s;
	}
}
