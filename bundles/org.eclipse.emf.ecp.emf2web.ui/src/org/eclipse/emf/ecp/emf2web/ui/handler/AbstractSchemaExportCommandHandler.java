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
package org.eclipse.emf.ecp.emf2web.ui.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.emf2web.controller.GenerationController;
import org.eclipse.emf.ecp.emf2web.controller.GenerationInfo;
import org.eclipse.emf.ecp.emf2web.exporter.GenerationExporter;
import org.eclipse.emf.ecp.emf2web.ui.messages.Messages;
import org.eclipse.emf.ecp.emf2web.ui.wizard.ExportSchemasWizard;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Abstract implementation for an handler responsible for exporting view models.
 *
 * @param <T> the type to export
 */
public abstract class AbstractSchemaExportCommandHandler<T extends EObject> extends AbstractHandler {

	/**
	 * This implementation uses the {@link #getObjects(ExecutionEvent)}
	 * and {@link #openWizard(Collection, ExecutionEvent, Shell)} methods to open an export wizard.
	 *
	 * @param event {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws ExecutionException {@inheritDoc}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			final Collection<T> objects = getObjects(event);
			if (objects == null || objects.isEmpty()) {
				return null;
			}

			final Shell shell = HandlerUtil.getActiveShell(event);
			openWizard(objects, event, shell);
			// BEGIN SUPRESS CATCH EXCEPTION
		} catch (final RuntimeException e) {
			// END SUPRESS CATCH EXCEPTION
			handleRuntimeException(e);
		}

		return null;
	}

	/**
	 * Is called if a RuntimeException occurs during execution.
	 *
	 * @param e the {@link RuntimeException}.
	 * @throws ExecutionException
	 *             Wraps the given RuntimeException and throws it.
	 */
	protected void handleRuntimeException(RuntimeException e) throws ExecutionException {
		final StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		final String stackTrace = sw.toString();

		final List<Status> childStatuses = new ArrayList<Status>();
		for (final String line : stackTrace.split(System.getProperty("line.separator"))) { //$NON-NLS-1$
			childStatuses.add(new Status(IStatus.ERROR, "org.eclipse.emf.ecp.emf2web.ui", line)); //$NON-NLS-1$
		}
		final MultiStatus status = new MultiStatus("org.eclipse.emf.ecp.emf2web.ui", IStatus.ERROR, //$NON-NLS-1$
			childStatuses.toArray(new Status[] {}),
			e.getLocalizedMessage(), e);

		ErrorDialog.openError(null, Messages.getString("AbstractSchemaExportCommandHandler.Error_Title"), //$NON-NLS-1$
			Messages.getString("AbstractSchemaExportCommandHandler.Error_Message"), //$NON-NLS-1$
			status);
		throw new ExecutionException(Messages.getString("AbstractSchemaExportCommandHandler.Error_Message"), e); //$NON-NLS-1$
	}

	/**
	 * The default implementation opens the {@link ExportSchemaWizard} using the generation handlers returned by
	 * {@link #getGenerationController()} and {@link #getGenerationExporter()}.
	 *
	 * @param objects
	 *            The objects which shall be exported.
	 * @param event
	 *            The {@link ExecutionEvent} which is given by the {@link #execute(ExecutionEvent)} method.
	 * @param shell
	 *            The shell for the wizard.
	 * @return
	 * 		The return value of the {@link WizardDialog}.
	 */
	protected int openWizard(Collection<? extends EObject> objects, ExecutionEvent event, Shell shell) {
		final List<GenerationInfo> generationInfos = getGenerationController().generate(objects);
		final URI locationProposal = getLocationProposal(event);
		final ExportSchemasWizard wizard = new ExportSchemasWizard(generationInfos, getGenerationExporter(),
			locationProposal);
		final WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.setPageSize(new Point(600, 600));
		return dialog.open();
	}

	/**
	 * Returns a proposal for the export location. The default implementation returns the container of the first
	 * selected element.
	 *
	 * @param event
	 *            The {@link ExecutionEvent} which is given by the {@link #execute(ExecutionEvent)} method.
	 * @return
	 * 		The location proposal for the export. {@code null} if no proposal could be determined.
	 */
	protected URI getLocationProposal(ExecutionEvent event) {
		IFile file = null;
		final IStructuredSelection selection = (IStructuredSelection) HandlerUtil
			.getCurrentSelection(event);
		final Object firstElement = selection.getFirstElement();
		if (IFile.class.isInstance(firstElement)) {
			file = IFile.class.cast(firstElement);
		}
		if (file == null) {
			final IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
			if (activeEditor != null) {
				if (IFileEditorInput.class.isInstance(activeEditor.getEditorInput())) {
					file = IFileEditorInput.class.cast(activeEditor.getEditorInput()).getFile();
				}
			}
		}
		if (file != null) {
			final IContainer container = file.getParent();
			return URI.createPlatformResourceURI(container.getFullPath().toString(), true);
		}

		return null;
	}

	/**
	 * Returns the views which shall be exported.
	 *
	 * @param event
	 *            The {@link ExecutionEvent} which is given by the {@link #execute(ExecutionEvent)} method.
	 * @return
	 * 		The collection of views which shall be exported.
	 */
	protected abstract Collection<T> getObjects(ExecutionEvent event);

	/**
	 * Returns the {@link GenerationController} which shall be used to generate the files.
	 *
	 * @return
	 * 		The {@link GenerationController} which shall be used.
	 */
	protected abstract GenerationController getGenerationController();

	/**
	 * Returns the {@link GenerationExporter} responsible for creating the files generated by the
	 * {@link GenerationController}.
	 *
	 * @return
	 * 		The {@link GenerationExporter} which shall be used.
	 */
	protected abstract GenerationExporter getGenerationExporter();
}
