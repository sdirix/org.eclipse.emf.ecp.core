/**
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 */
package org.eclipse.emf.ecp.view.label.rap;

import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.label.model.VLabelPackage;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Alexandra Buzila
 * 
 */
public class LabelRapRendererTester implements ECPRendererTester {

	/**
	 * 
	 */
	public LabelRapRendererTester() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.view.model.common.SimpleControlRendererTester#
	 * isApplicable(org.eclipse.emf.ecp.view.spi.model.VElement,
	 * org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VLabelPackage.eINSTANCE.getLabel().isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		if (!FrameworkUtil.getBundle(Display.class).getSymbolicName()
			.contains(".rwt")) { //$NON-NLS-1$
			return NOT_APPLICABLE;
		}

		return 3;
	}

}
