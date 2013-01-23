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

package org.eclipse.emf.ecp.editor.descriptor;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * Abstract class reading annotations from feature.
 * 
 * @param <T> the Type of the Descriptor
 * @author Eugen Neufeld
 */
public abstract class AbstractAttributeDescriptor<T> {
	/**
	 * @param propertyDescriptor
	 *            the property descriptor
	 * @param modelElement
	 *            the model element
	 * @return Returns the property from a given propertyDescriptor as an A value.
	 */

	public T getValue(IItemPropertyDescriptor propertyDescriptor, EObject modelElement) {
		EAnnotation priority = ((EStructuralFeature) propertyDescriptor.getFeature(modelElement))
			.getEAnnotation("org.eclipse.emf.ecp.editor");
		if (priority == null || priority.getDetails() == null || priority.getDetails().get(getAnnotationName()) == null) {
			return getDefaultValue();
		}
		String s = priority.getDetails().get(getAnnotationName());
		return getTypedValue(s);
	}

	/**
	 * The name of the annotation to read.
	 * 
	 * @return the name of the annotation
	 */
	protected abstract String getAnnotationName();

	/**
	 * The default value to use if no annotation value is available.
	 * 
	 * @return the default value of the descriptor
	 */
	protected abstract T getDefaultValue();

	/**
	 * Convert read annotation value to the expected type.
	 * 
	 * @param value the value to convert
	 * @return the converted value
	 */
	protected abstract T getTypedValue(String value);
}
