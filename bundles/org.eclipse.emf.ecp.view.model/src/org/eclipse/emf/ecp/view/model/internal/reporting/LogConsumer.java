/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.internal.reporting;

import org.eclipse.emf.ecp.view.spi.model.impl.Activator;
import org.eclipse.emf.ecp.view.spi.model.reporting.AbstractReport;
import org.eclipse.emf.ecp.view.spi.model.reporting.ReportServiceConsumer;

/**
 * A {@link ReportServiceConsumer} that logs all all received {@code ReportEntities}.
 *
 * @author emueller
 */
public class LogConsumer implements ReportServiceConsumer {

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.model.reporting.ReportServiceConsumer#reported(org.eclipse.emf.ecp.view.spi.model.reporting.AbstractReport)
	 */
	@Override
	public void reported(AbstractReport reportEntity) {
		Activator.log(reportEntity);
	}

}
