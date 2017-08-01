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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.emf2web.controller.GenerationController;
import org.eclipse.emf.ecp.emf2web.exporter.FileGenerationExporter;
import org.eclipse.emf.ecp.emf2web.exporter.GenerationExporter;
import org.eclipse.emf.ecp.emf2web.graph.json.controller.SiriusJsonGenerationController;
import org.eclipse.emf.ecp.emf2web.ui.handler.AbstractSchemaExportCommandHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.viewpoint.description.Group;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

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
		final IStructuredSelection selection = (IStructuredSelection) HandlerUtil
			.getCurrentSelection(event);

		final List<EObject> loadedObjects = new LinkedList<EObject>();
		final List<IFile> failedFiles = new LinkedList<IFile>();

		@SuppressWarnings("unchecked")
		final Iterator<Object> it = selection.iterator();
		while (it.hasNext()) {
			final Object selectedObject = it.next();
			if (selectedObject instanceof IFile) {
				final IFile file = (IFile) selectedObject;
				final String path = file.getLocation().toString();
				final EObject view = loadObject(path);
				if (Group.class.isInstance(view)) {
					loadedObjects.add(view);
				} else {
					System.err.println("Root object of " + path + " was not a Sirius Group");
					failedFiles.add(file);
				}
				loadedObjects.add(view);
			}
		}

		if (!failedFiles.isEmpty()) {
			showErrorMessage(failedFiles, loadedObjects.isEmpty());
			if (loadedObjects.isEmpty()) {
				return null;
			}
		}

		return loadedObjects;
	}

	private void showErrorMessage(Collection<IFile> files, boolean allFailed) {
		final Iterator<IFile> viewIt = files.iterator();
		String viewNames = viewIt.next().getName();
		while (viewIt.hasNext()) {
			viewNames += ", " + viewIt.next().getName(); //$NON-NLS-1$
		}

		MessageDialog.openError(Display.getDefault().getActiveShell(),
			"Error while resolving odesign files: ", viewNames);
	}

	private EObject loadObject(String path) {
		final ResourceSet resourceSet = new ResourceSetImpl();
		final URI fileURI = URI.createFileURI(path);
		final Resource resource = resourceSet.getResource(fileURI, true);
		if (resource != null) {
			return resource.getContents().get(0);
		}
		return null;
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
