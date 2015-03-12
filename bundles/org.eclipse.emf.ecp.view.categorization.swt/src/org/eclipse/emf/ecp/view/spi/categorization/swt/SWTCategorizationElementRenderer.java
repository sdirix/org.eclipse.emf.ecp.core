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
 * Johannes Falterimeier - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.categorization.swt;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecp.view.spi.categorization.model.VAbstractCategorization;
import org.eclipse.emf.ecp.view.spi.categorization.model.VCategorizationElement;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.reporting.ReportService;
import org.eclipse.emfforms.spi.swt.core.EMFFormsRendererFactory;

/**
 * The Class ViewSWTRenderer.
 *
 * @author Eugen Neufeld
 */
public class SWTCategorizationElementRenderer extends AbstractJFaceTreeRenderer<VCategorizationElement> {

	/**
	 * Default constructor.
	 *
	 * @param vElement the view model element to be rendered
	 * @param viewContext the view context
	 * @param reportService the {@link ReportService}
	 * @param emfFormsRendererFactory The {@link EMFFormsRendererFactory}
	 */
	public SWTCategorizationElementRenderer(VCategorizationElement vElement, ViewModelContext viewContext,
		ReportService reportService,
		EMFFormsRendererFactory emfFormsRendererFactory) {
		super(vElement, viewContext, reportService, emfFormsRendererFactory);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.categorization.swt.AbstractJFaceTreeRenderer#getCategorizations()
	 */
	@Override
	protected EList<VAbstractCategorization> getCategorizations() {
		return getVElement().getCategorizations();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.categorization.swt.AbstractJFaceTreeRenderer#getCategorizationElement()
	 */
	@Override
	protected VCategorizationElement getCategorizationElement() {
		return getVElement();
	}

}
