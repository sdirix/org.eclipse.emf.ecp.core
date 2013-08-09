/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.rule;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;

/**
 * <p>
 * Convenience class for managing settings.
 * </p>
 * <p>
 * Provides an equals method that returns {@code true} if the {@link EObject} and the and the {@link EStructuralFeature}
 * are the same.
 * </p>
 * 
 * @author emueller
 */
public final class ReadOnlySetting implements Setting {

	private final EObject eObject;
	private final EAttribute attribute;

	/**
	 * Convenience method for creating a setting.
	 * 
	 * @param eObject
	 *            an {@link EObject}
	 * @param attribute
	 *            an {@link EAttribute} of the given {@code eObject}
	 * @return the constructed setting
	 */
	public static Setting createSetting(EObject eObject, EAttribute attribute) {
		return new ReadOnlySetting(eObject, attribute);
	}

	/**
	 * Constructor.
	 * 
	 * @param eObject
	 *            an {@link EObject}
	 * @param attribute
	 *            an {@link EAttribute} of the given {@code eObject}
	 */
	private ReadOnlySetting(EObject eObject, EAttribute attribute) {
		this.eObject = eObject;
		this.attribute = attribute;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.EStructuralFeature.Setting#getEObject()
	 */
	public EObject getEObject() {
		return eObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.EStructuralFeature.Setting#getEStructuralFeature()
	 */
	public EStructuralFeature getEStructuralFeature() {
		return attribute;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.EStructuralFeature.Setting#get(boolean)
	 */
	public Object get(boolean resolve) {
		return eObject.eGet(attribute);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.EStructuralFeature.Setting#set(java.lang.Object)
	 */
	public void set(Object newValue) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.EStructuralFeature.Setting#isSet()
	 */
	public boolean isSet() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.EStructuralFeature.Setting#unset()
	 */
	public void unset() {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ReadOnlySetting)) {
			return false;
		}

		final ReadOnlySetting otherReadOnlySetting = (ReadOnlySetting) obj;
		return otherReadOnlySetting.attribute.equals(attribute) && otherReadOnlySetting.eObject.equals(eObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return eObject.hashCode() + attribute.hashCode() * 17;
	}
}
