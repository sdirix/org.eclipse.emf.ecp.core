/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.view.spi.model.reporting.AbstractReport;
import org.eclipse.emf.ecp.view.spi.model.reporting.ReportServiceConsumer;
import org.eclipse.emf.ecp.view.spi.model.util.ViewModelUtil;
import org.eclipse.emf.ecp.view.spi.swt.reporting.InvalidGridDescriptionReport;

/**
 *
 *
 * @author emueller
 * @since 1.5
 *
 */
public class InvalidGridDescriptionReportConsumer implements ReportServiceConsumer {

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.model.reporting.ReportServiceConsumer#reported(org.eclipse.emf.ecp.view.spi.model.reporting.AbstractReport)
	 */
	@Override
	public void reported(AbstractReport reportEntity) {
		if (InvalidGridDescriptionReport.class.isInstance(reportEntity) && ViewModelUtil.isDebugMode()) {
			// TODO: mimics existing behavior; should we rather show a dialog?
			throw new IllegalStateException("Invalid number of cells, expected exactly one cell!"); //$NON-NLS-1$
		}
	}
}
