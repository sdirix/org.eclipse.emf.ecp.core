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
package org.eclipse.emf.ecp.view.internal.control.multireference;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * Tester for MultiReference Control.
 *
 * @author Eugen Neufeld
 *
 */
public class MultiReferenceRendererTester implements ECPRendererTester {

	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		final VControl vControl = (VControl) vElement;
		final VDomainModelReference domainModelReference = vControl.getDomainModelReference();
		if (domainModelReference == null) {
			return NOT_APPLICABLE;
		}
		final VControl control = (VControl) vElement;
		final IValueProperty valueProperty = Activator.getDefault().getEMFFormsDatabinding()
			.getValueProperty(control.getDomainModelReference());
		final EStructuralFeature feature = (EStructuralFeature) valueProperty.getValueType();
		if (!feature.isMany()) {
			return NOT_APPLICABLE;
		}
		if (EAttribute.class.isInstance(feature)) {
			return NOT_APPLICABLE;
		}
		return 5;
	}

}
