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

import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.spi.swt.table.ECPCellEditorTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;

/**
 * Tester for a date cell editor.
 *
 * @author Eugen Neufeld
 *
 */
public class DateCellEditorTester implements ECPCellEditorTester {

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see ECPCellEditorTester#isApplicable(EObject, EStructuralFeature, ViewModelContext)
	 */
	@Override
	public int isApplicable(EObject eObject, EStructuralFeature feature, ViewModelContext viewModelContext) {
		if (EAttribute.class.isInstance(feature) && !feature.isMany()) {
			final Class<?> instanceClass = ((EAttribute) feature).getEAttributeType().getInstanceClass();
			if (instanceClass == null) {
				return NOT_APPLICABLE;
			}
			if (Date.class.isAssignableFrom(instanceClass)) {
				return 1;
			}
		}
		return NOT_APPLICABLE;
	}
}
