/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.table.swt;

import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;

/**
 * Abstract {@link ECPRendererTester} for {@link VTableControl VTableControls} based on
 * {@link VTableControl#getDetailEditing()}.
 *
 * @author jfaltermeier
 *
 */
public abstract class AbstractTableControlRendererTester implements ECPRendererTester {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.ECPRendererTester#isApplicable(org.eclipse.emf.ecp.view.spi.model.VElement,
	 *      org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VTableControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		if (getDetailEditing() == VTableControl.class.cast(vElement).getDetailEditing()) {
			return 2;
		}
		return NOT_APPLICABLE;
	}

	/**
	 * Returns the {@link DetailEditing} of the {@link VTableControl} for which the tester will not return
	 * {@link ECPRendererTester#NOT_APPLICABLE}.
	 *
	 * @return the detail editing value
	 */
	protected abstract DetailEditing getDetailEditing();

}
