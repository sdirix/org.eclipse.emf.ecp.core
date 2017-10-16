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
package org.eclipse.emf.ecp.emf2web.ui.json.internal.handler;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecp.emf2web.controller.GenerationController;
import org.eclipse.emf.ecp.emf2web.exporter.FileGenerationExporter;
import org.eclipse.emf.ecp.emf2web.exporter.GenerationExporter;
import org.eclipse.emf.ecp.emf2web.ui.handler.AbstractSchemaExportCommandHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Stefan Dirix
 *
 */
public class ExportSchemaHandler extends AbstractSchemaExportCommandHandler<EClass> {

	@Override
	protected Collection<EClass> getObjects(ExecutionEvent event) {
		final IStructuredSelection selection = (IStructuredSelection) HandlerUtil
			.getCurrentSelection(event);

		final List<EClass> loadedObjects = new LinkedList<EClass>();

		@SuppressWarnings("unchecked")
		final Iterator<Object> it = selection.iterator();
		while (it.hasNext()) {
			final Object selectedObject = it.next();
			if (selectedObject instanceof EClass) {
				loadedObjects.add(EClass.class.cast(selectedObject));
			}
		}
		return loadedObjects;
	}

	@Override
	protected GenerationController getGenerationController() {
		return new PureSchemaGenerationController();
	}

	@Override
	protected GenerationExporter getGenerationExporter() {
		return new FileGenerationExporter();
	}

}
