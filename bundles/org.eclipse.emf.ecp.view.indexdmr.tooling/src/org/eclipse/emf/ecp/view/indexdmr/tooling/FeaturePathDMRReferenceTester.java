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
package org.eclipse.emf.ecp.view.indexdmr.tooling;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.indexdmr.model.VIndexDomainModelReference;
import org.eclipse.emf.ecp.view.spi.indexdmr.model.VIndexdmrPackage;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * A Tester for the FeaturePathControl which is added as a child of a {@link VIndexDomainModelReference}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class FeaturePathDMRReferenceTester implements
	ECPRendererTester {

	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {

		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}

		final VControl control = (VControl) vElement;
		final Setting setting = control.getDomainModelReference().getIterator()
			.next();
		if (VIndexDomainModelReference.class.isInstance(setting.getEObject())
			&& VIndexdmrPackage.eINSTANCE.getIndexDomainModelReference_TargetDMR() == setting
				.getEStructuralFeature()) {
			return 6;
		}

		return NOT_APPLICABLE;
	}
}
