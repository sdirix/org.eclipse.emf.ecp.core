/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Stefan Dirix - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.emf2web.ui.wizard;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.ecp.emf2web.controller.GenerationInfo;
import org.eclipse.emf.ecp.emf2web.exporter.GenerationExporter;
import org.eclipse.emf.ecp.emf2web.ui.messages.Messages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

/**
 * Generic wizards for {@link GenerationExporter}s.
 *
 * @author Stefan Dirix
 *
 */
public class ExportSchemasWizard extends Wizard {

	/**
	 * Collection of {@link GenerationInfo}s.
	 */
	private final Collection<? extends GenerationInfo> generationInfos;
	/**
	 * The {@link GenerationExporter} responsible for export.
	 */
	private final GenerationExporter exporter;

	/**
	 * Constructor.
	 *
	 * @param generationInfos Collection of information used by the exporter to export.
	 * @param exporter The exporter responsible to export the given generation information.
	 */
	public ExportSchemasWizard(Collection<? extends GenerationInfo> generationInfos, GenerationExporter exporter) {
		setWindowTitle(Messages.getString("ExportSchemasWizard.WindowTitle")); //$NON-NLS-1$
		this.generationInfos = generationInfos;
		this.exporter = exporter;
	}

	@Override
	public void addPages() {
		for (final GenerationInfo generationInfo : generationInfos) {
			addPage(new SelectLocationPage(generationInfo));
		}
	}

	@Override
	public boolean performFinish() {
		try {
			exporter.export(generationInfos);
			MessageDialog.openInformation(getShell(), Messages.getString("ExportSchemasWizard.DialogTitle_Success"), //$NON-NLS-1$
				Messages.getString("ExportSchemasWizard.DialogMessage_Success")); //$NON-NLS-1$
			return true;
		} catch (final IOException e) {
			MessageDialog.openError(getShell(), Messages.getString("ExportSchemasWizard.DialogTitle_Error"), //$NON-NLS-1$
				e.getMessage());
		}
		return false;
	}

}
