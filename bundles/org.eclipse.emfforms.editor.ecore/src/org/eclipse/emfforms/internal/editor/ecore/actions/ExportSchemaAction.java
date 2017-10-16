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
package org.eclipse.emfforms.internal.editor.ecore.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecp.emf2web.controller.GenerationInfo;
import org.eclipse.emf.ecp.emf2web.exporter.FileGenerationExporter;
import org.eclipse.emf.ecp.emf2web.ui.json.internal.handler.PureSchemaGenerationController;
import org.eclipse.emf.ecp.emf2web.ui.wizard.ExportSchemasWizard;
import org.eclipse.emfforms.spi.swt.treemasterdetail.actions.MasterDetailAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @author Stefan Dirix
 *
 */
public class ExportSchemaAction extends MasterDetailAction {

	private static final String ICON_PATH = "icons/EcoreModelFile.gif";
	private static final String ACTION_NAME = "Export to JSON Schema";

	public ExportSchemaAction() {
		setLabel(ACTION_NAME);
		setImagePath(ICON_PATH);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return null;
	}

	@Override
	public boolean shouldShow(List<Object> objects) {
		if (objects == null || objects.size() == 0) {
			return false;
		}
		for (final Object object : objects) {
			if (!EClass.class.isInstance(object)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void execute(List<Object> objects) {
		final List<EClass> eClasses = new ArrayList<EClass>();
		for (final Object object : objects) {
			final EClass eClass = EClass.class.cast(object);
			eClasses.add(eClass);
		}
		final PureSchemaGenerationController controller = new PureSchemaGenerationController();
		final List<GenerationInfo> generationInfos = controller.generate(eClasses);

		final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.getActiveEditor();
		final IEditorInput editorInput = activeEditor.getEditorInput();

		URI proposal = null;
		if (FileEditorInput.class.isInstance(editorInput)) {
			final FileEditorInput fileInput = FileEditorInput.class.cast(editorInput);
			final IFile file = fileInput.getFile();
			if (file.getParent() != null) {
				proposal = URI.createPlatformResourceURI(file.getParent().getFullPath().toString(), true);
			}
		}

		final ExportSchemasWizard wizard = new ExportSchemasWizard(generationInfos, new FileGenerationExporter(),
			proposal);
		final WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
		dialog.setPageSize(new Point(600, 600));
		dialog.open();
	}

}
