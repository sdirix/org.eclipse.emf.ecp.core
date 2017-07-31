/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * stefan - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.emf2web.graph.ui.json.internal.handler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.emf2web.controller.GenerationController;
import org.eclipse.emf.ecp.emf2web.exporter.FileGenerationExporter;
import org.eclipse.emf.ecp.emf2web.exporter.GenerationExporter;
import org.eclipse.emf.ecp.emf2web.graph.json.controller.SiriusJsonGenerationController;
import org.eclipse.emf.ecp.emf2web.ui.handler.AbstractSchemaExportCommandHandler;

/**
 * @author Stefan Dirix
 *
 */
public class ExportSiriusHandler extends AbstractSchemaExportCommandHandler<EObject> {

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.emf2web.ui.handler.AbstractSchemaExportCommandHandler#getObjects(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	protected Collection<EObject> getObjects(ExecutionEvent event) {
		final List<EObject> toExport = new LinkedList<EObject>();
		toExport.add(null);
		return toExport;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.emf2web.ui.handler.AbstractSchemaExportCommandHandler#getGenerationController()
	 */
	@Override
	protected GenerationController getGenerationController() {
		return new SiriusJsonGenerationController();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.emf2web.ui.handler.AbstractSchemaExportCommandHandler#getGenerationExporter()
	 */
	@Override
	protected GenerationExporter getGenerationExporter() {
		return new FileGenerationExporter();
	}

}
