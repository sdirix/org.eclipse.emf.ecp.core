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

import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;

/**
 * @author jfaltermeier
 *
 */
public class TableControlDetailDialogRendererTester extends AbstractTableControlRendererTester {

	// TODO remove isApplicable method in future versions, because of deprecated isEnableDetailEditingDialog()
	@SuppressWarnings("deprecation")
	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (!VTableControl.class.isInstance(vElement)) {
			return NOT_APPLICABLE;
		}
		final VTableControl tableControl = VTableControl.class.cast(vElement);
		if (tableControl.getDetailEditing() == DetailEditing.NONE && tableControl.isEnableDetailEditingDialog()) {
			return 3;
		}
		return super.isApplicable(vElement, viewModelContext);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.internal.table.swt.AbstractTableControlRendererTester#getDetailEditing()
	 */
	@Override
	protected DetailEditing getDetailEditing() {
		return DetailEditing.WITH_DIALOG;
	}

}
