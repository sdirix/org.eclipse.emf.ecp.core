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
package org.eclipse.emf.ecp.internal.edit;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.util.ECPApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class StaticApplicableTester implements ECPApplicableTester {

	private final boolean singleValue;
	private final int priority;
	private final Class<?> supportedClassType;
	private final Class<? extends EObject> supportedEObject;
	private final String supportedFeature;

	public StaticApplicableTester(boolean singleValue, int priority, Class<?> supportedClassType,
		Class<? extends EObject> supportedEObject, String supportedFeature) {
		this.singleValue = singleValue;
		this.priority = priority;
		this.supportedClassType = supportedClassType;
		this.supportedEObject = supportedEObject;
		this.supportedFeature = supportedFeature;
	}

	/**
	 * {@inheritDoc}
	 */
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		// if the feature is a multiValue and the description is a singlevalue continue
		if (isSingleValue() == itemPropertyDescriptor.isMany(eObject)) {
			return NOT_APPLICABLE;
		}
		EStructuralFeature feature=(EStructuralFeature) itemPropertyDescriptor.getFeature(eObject);
		
		if(EAttribute.class.isInstance(feature)){
			Class<?> instanceClass = ((EAttribute)feature).getEAttributeType().getInstanceClass();
			if (instanceClass.isPrimitive()) {
				try {
					Class<?> primitive = (Class<?>) getSupportedClassType().getField("TYPE").get(null);
					if (!primitive.equals(instanceClass)) {
						return NOT_APPLICABLE;
					}
	
				} catch (IllegalArgumentException e) {
					return NOT_APPLICABLE;
				} catch (SecurityException e) {
					return NOT_APPLICABLE;
				} catch (IllegalAccessException e) {
					return NOT_APPLICABLE;
				} catch (NoSuchFieldException e) {
					return NOT_APPLICABLE;
				}
			}
			else if (!getSupportedClassType().isAssignableFrom(instanceClass)) {
				return NOT_APPLICABLE;
			}
		}
		else if(EReference.class.isInstance(feature)){
			Class<?> instanceClass = feature.getEType().getInstanceClass();
			if (!getSupportedClassType().isAssignableFrom(instanceClass)) {
				return NOT_APPLICABLE;
			}
		}
		if (getSupportedEObject().isInstance(eObject)
			&& (getSupportedFeature() == null || eObject.eClass()
				.getEStructuralFeature(getSupportedFeature()).equals(feature))) {
			return getPriority();
		}
		return NOT_APPLICABLE;
	}

	public boolean isSingleValue() {
		return singleValue;
	}

	public int getPriority() {
		return priority;
	}

	public Class<? extends EObject> getSupportedEObject() {
		return supportedEObject;
	}

	public String getSupportedFeature() {
		return supportedFeature;
	}

	public Class<?> getSupportedClassType() {
		return supportedClassType;
	}
}
