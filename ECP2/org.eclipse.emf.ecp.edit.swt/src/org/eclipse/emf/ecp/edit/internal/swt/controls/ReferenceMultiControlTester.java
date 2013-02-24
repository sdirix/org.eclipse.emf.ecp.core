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
package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ControlDescription;
import org.eclipse.emf.ecp.edit.ControlFactory;
import org.eclipse.emf.ecp.editor.util.ECPApplicableTester;
import org.eclipse.emf.ecp.editor.util.StaticApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
/**
 * This is a dynamic tester for an reference multi control. It tests whether there is a control with a static tester which would fit. 
 * @author Eugen Neufeld
 *
 */
public class ReferenceMultiControlTester implements ECPApplicableTester {

	/**{@inheritDoc} **/
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		int bestPriority=NOT_APPLICABLE;
		for(ControlDescription description:ControlFactory.INSTANCE.getControlDescriptors()){
			if(StaticApplicableTester.class.isInstance(description.getTester())){
				StaticApplicableTester tester=(StaticApplicableTester) description.getTester();
				int priority=getTesterPriority(tester,itemPropertyDescriptor,eObject);
				if(bestPriority<priority){
					bestPriority=priority;
				}
			}
			else{
				continue;
			}
		}
		return bestPriority;
	}
	/**
	 * Calculates the priority of the attribute tester.
	 * @param tester the tester to get the priority for 
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor}
	 * @param eObject the {@link EObject}
	 * @return the priority
	 */
	public static int getTesterPriority(StaticApplicableTester tester,IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		
		if(!itemPropertyDescriptor.isMany(eObject)){
			return NOT_APPLICABLE;
		}
		EStructuralFeature feature=(EStructuralFeature) itemPropertyDescriptor.getFeature(eObject);
		if(EAttribute.class.isInstance(feature)){
				return NOT_APPLICABLE;
		}
		else if(EReference.class.isInstance(feature)){
			if(((EReference)feature).isContainment()){
				return NOT_APPLICABLE;
			}
			Class<?> instanceClass = feature.getEType().getInstanceClass();
			if (!tester.getSupportedClassType().isAssignableFrom(instanceClass)) {
				return NOT_APPLICABLE;
			}
		}
		if(!tester.isSingleValue()){
			return NOT_APPLICABLE;
		}
		if (tester.getSupportedEObject().isInstance(eObject)
			&& (tester.getSupportedFeature() == null || eObject.eClass()
				.getEStructuralFeature(tester.getSupportedFeature()).equals(feature))) {
			return tester.getPriority()+1;
		}
		return NOT_APPLICABLE;
	}

}
