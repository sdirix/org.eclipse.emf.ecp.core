/**
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 */
package org.eclipse.emf.ecp.makeithappen.ui.emailcontrol;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.makeithappen.model.task.TaskPackage;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * The renderer tester for the email control renderer.
 *
 * @author Eclipse Modeling Project
 *
 */
@SuppressWarnings("restriction")
public class EmailControlRendererTest implements ECPRendererTester {

	/**
	 * @param feature the structural feature.
	 * @param vElement the VElement.
	 * @param context the view model context.
	 * @return whether the renderer is applicable for the feature.
	 */
	public int isApplicableForFeature(EStructuralFeature feature, VElement vElement, ViewModelContext context) {
		if (feature.equals(TaskPackage.eINSTANCE.getUser_Email())) {
			return 10;
		}
		return NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.ECPRendererTester#isApplicable(org.eclipse.emf.ecp.view.spi.model.VElement,
	 *      org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		final EStructuralFeature feature = VControl.class.cast(vElement).getDomainModelReference()
			.getEStructuralFeatureIterator().next();
		return isApplicableForFeature(feature, vElement, viewModelContext);
	}

}
