/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.vertical.swt;

import java.util.Collection;

import org.eclipse.emf.ecp.view.spi.core.swt.SWTContainerRenderer;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.vertical.model.VVerticalLayout;

/**
 * The SWT Renderer for the {@link VVerticalLayout}. It renders all elements under each other.
 * 
 * @author Eugen Neufeld
 * @since 1.2
 * 
 */
public class VerticalLayoutSWTRenderer extends SWTContainerRenderer<VVerticalLayout> {

	private static final String CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_layout_vertical"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SWTContainerRenderer#getChildren()
	 */
	@Override
	protected Collection<VContainedElement> getChildren() {
		return getVElement().getChildren();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SWTContainerRenderer#getCustomVariant()
	 */
	@Override
	protected String getCustomVariant() {
		return CONTROL_COLUMN;
	}

}
