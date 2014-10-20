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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * A {@link ReportServiceConsumer} that display a dialog
 * for each report.
 *
 * @author emueller
 * @since 1.5
 *
 */
public class DebugSWTReportConsumer implements ReportServiceConsumer {

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.model.reporting.ReportServiceConsumer#reported(org.eclipse.emf.ecp.view.spi.model.reporting.AbstractReport)
	 */
	@Override
	public void reported(AbstractReport reportEntity) {
		MessageDialog.openWarning(
			Display.getDefault().getActiveShell(), "Debug", //$NON-NLS-1$
			reportEntity.getMessage());
	}

}
