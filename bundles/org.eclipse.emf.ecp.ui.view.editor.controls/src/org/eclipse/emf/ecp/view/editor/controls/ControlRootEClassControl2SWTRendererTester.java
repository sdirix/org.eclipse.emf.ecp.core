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

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;

/**
 * @author Alexandra Buzila
 * 
 */
public class ControlRootEClassControl2SWTRendererTester implements ECPRendererTester {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.internal.core.swt.renderer.SimpleControlRendererTester#getSupportedClassType()
	 */
	@Override
	public int isApplicable(VElement vElement, ViewModelContext context) {
		if (!VControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		if (!VControl.class.cast(vElement).getDomainModelReference().getIterator().hasNext()) {
			return NOT_APPLICABLE;
		}
		final Setting setting = VControl.class.cast(vElement).getDomainModelReference().getIterator().next();
		return isApplicable(setting);
	}

	/**
	 * Test if setting contains the correct data.
	 * 
	 * @param setting the {@link Setting} to check
	 * @return the priority of the control
	 */
	protected int isApplicable(Setting setting) {
		if (!VView.class.isInstance(setting.getEObject())) {
			return NOT_APPLICABLE;
		}
		if (VViewPackage.eINSTANCE.getView_RootEClass() != setting.getEStructuralFeature()) {
			return NOT_APPLICABLE;
		}
		if (setting.get(true) == null) {
			return NOT_APPLICABLE;
		}
		return 3;
	}

}
