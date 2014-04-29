/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.categorization.swt;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecp.view.spi.categorization.model.VAbstractCategorization;
import org.eclipse.emf.ecp.view.spi.categorization.model.VCategorization;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;

/**
 * @author Eugen
 * 
 */
public class CompositeCategorySWTTabRenderer extends AbstractSWTTabRenderer<VCategorization> {

	/**
	 * Default constructor.
	 */
	public CompositeCategorySWTTabRenderer() {
		super();
	}

	/**
	 * Test constructor.
	 * 
	 * @param factory the {@link SWTRendererFactory} to use.
	 */
	CompositeCategorySWTTabRenderer(SWTRendererFactory factory) {
		super(factory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.categorization.swt.AbstractSWTTabRenderer#getCategorizations()
	 */
	@Override
	protected EList<VAbstractCategorization> getCategorizations() {
		return getVElement().getCategorizations();
	}

}
