/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.editor.controls;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.internal.core.swt.renderer.SimpleControlSWTRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;

/**
 * @author Alexandra Buzila
 * 
 */
public class ControlRootEClassControl2SWTRendererTester extends SimpleControlSWTRendererTester {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SimpleControlSWTRendererTester#isSingleValue()
	 */
	@Override
	protected boolean isSingleValue() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SimpleControlSWTRendererTester#getPriority()
	 */
	@Override
	protected int getPriority() {
		return 3;
	}

	private int isApplicableForFeature(EStructuralFeature feature, VElement vElement, ViewModelContext context) {
		if (feature.equals(VViewPackage.eINSTANCE.getView_RootEClass())) {
			return 10;
		}
		return NOT_APPLICABLE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SimpleControlSWTRendererTester#getSupportedClassType()
	 */
	@Override
	public int isApplicable(VElement vElement, ViewModelContext context) {
		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		final EStructuralFeature feature = VControl.class.cast(vElement).getDomainModelReference()
			.getEStructuralFeatureIterator().next();
		return isApplicableForFeature(feature, vElement, context);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SimpleControlSWTRendererTester#getSupportedClassType()
	 */
	@Override
	protected Class<?> getSupportedClassType() {
		return EClass.class;
	}

}
