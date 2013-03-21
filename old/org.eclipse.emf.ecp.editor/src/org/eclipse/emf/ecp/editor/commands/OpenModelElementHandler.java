/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.common.model.ECPModelelementContext;
import org.eclipse.emf.ecp.common.model.ECPWorkspaceManager;
import org.eclipse.emf.ecp.common.model.NoWorkspaceException;
import org.eclipse.emf.ecp.editor.Activator;
import org.eclipse.emf.ecp.editor.MEEditorInput;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This handler handles openModelEelement command. This handler
 * is to be executed indirectly using IHandlerService.executeCommand()
 * method. The Command itself does not have any UI representation.
 *         
 * @author Hodaie          
 */
public class OpenModelElementHandler extends AbstractHandler {

	private static final String FEATURE_TO_MARK_EVALUATIONCONTEXT_VARIABLE = "featureToMark";

	/**
	 * {@inheritDoc} 
	 * 
	 * <br/>
	 * We added this package and command to the editor plug-in,
	 * we needed to open a model element from model.edit plug-in and to avoid
	 * circular references we had to execute this command indirectly using
	 * IHandlerServise.executeCommand.
	 */

	public Object execute(ExecutionEvent event) throws ExecutionException {

		Object o = ((IStructuredSelection) HandlerUtil.getCurrentSelection(event)).getFirstElement();

		EObject me = (EObject) o;

		ECPModelelementContext context = null;
		try {
			context = ECPWorkspaceManager.getInstance().getWorkSpace().getProject(me);
		} catch (NoWorkspaceException e1) {
			
		}

		EStructuralFeature problemFeature;

		try {
			problemFeature = (EStructuralFeature) HandlerUtil.getVariableChecked(event,
				FEATURE_TO_MARK_EVALUATIONCONTEXT_VARIABLE);
		} catch (ExecutionException executionException) {
			problemFeature = null;
		}

		if (o != null) {
			MEEditorInput input;
			if (problemFeature == null) {
				input = new MEEditorInput(me, context);
			} else {
				input = new MEEditorInput(me, context, problemFeature);
			}
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.openEditor(input, "org.eclipse.emf.ecp.editor", true);
			} catch (PartInitException e) {
				Activator.logException(e);
			}
		}

		return null;
	}

}