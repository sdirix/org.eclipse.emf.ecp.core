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
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.table;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.util.ECPApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class NumberCellEditorTester implements ECPApplicableTester {

	public NumberCellEditorTester() {
	}

	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor.getFeature(null);
		if (EAttribute.class.isInstance(feature)) {
			Class<?> instanceClass = ((EAttribute) feature).getEAttributeType().getInstanceClass();
			if (Number.class.isAssignableFrom(instanceClass)) {
				return 1;
			}
		}
		return NOT_APPLICABLE;
	}

}
