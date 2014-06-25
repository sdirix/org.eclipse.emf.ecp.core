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
package org.eclipse.emf.ecp.view.template.internal.tooling.controls;

import java.util.Iterator;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.template.selector.viewModelElement.model.VTViewModelElementPackage;

/**
 * The tester for the {@link ViewModelSelectControlSWTRenderer}.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
public class AttributeSelectControlTester implements ECPRendererTester {

	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		final VDomainModelReference dmr = VControl.class.cast(vElement).getDomainModelReference();
		if (dmr == null) {
			return NOT_APPLICABLE;
		}
		final Iterator<EStructuralFeature> iterator = dmr.getEStructuralFeatureIterator();
		if (iterator == null) {
			return NOT_APPLICABLE;
		}
		if (!iterator.hasNext()) {
			return NOT_APPLICABLE;
		}
		final EStructuralFeature feature = iterator.next();
		if (feature == null) {
			return NOT_APPLICABLE;
		}
		if (VTViewModelElementPackage.eINSTANCE.getViewModelElementSelector_Attribute().equals(feature)) {
			return 5;
		}
		return NOT_APPLICABLE;
	}

}
