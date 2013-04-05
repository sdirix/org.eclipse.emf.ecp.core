/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProjectManager.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProjectWrapper;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.emfstore.internal.ui.Activator;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UICheckoutController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the EMFStore Checkout Branch Handler delegating to the EMFStore {@link UICheckoutController}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class CheckoutBranchHandler extends AbstractHandler {
	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		if (sel instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) sel;
			Object selection = ssel.getFirstElement();
			if (selection instanceof EMFStoreProjectWrapper) {
				EMFStoreProjectWrapper projectWrapper = (EMFStoreProjectWrapper) selection;

				checkoutBranch(projectWrapper, HandlerUtil.getActiveShell(event));

			}
		}

		return null;
	}

	/**
	 * @param projectWrapper
	 */
	private void checkoutBranch(EMFStoreProjectWrapper projectWrapper, Shell shell) {
		ESLocalProject localProject = new UICheckoutController(shell, projectWrapper.getCheckoutData(), true).execute();
		if (localProject != null) {
			InputDialog id = new InputDialog(shell, "Checkout Branch", "Enter Name for checked out project:",
				localProject.getProjectName(), new IInputValidator() {

					public String isValid(String newText) {
						if (ECPProjectManager.INSTANCE.getProject(newText) == null) {
							return null;
						}
						return "A project with this name already exists!";

					}
				});
			int inputResult = id.open();
			if (Window.OK == inputResult) {
				ECPProperties projectProperties = ECPUtil.createProperties();

				projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, localProject.getLocalProjectId()
					.getId());
				try {
					ECPProjectManager.INSTANCE.createProject(projectWrapper.getRepository(), id.getValue(),
						projectProperties);
				} catch (ProjectWithNameExistsException ex) {
					Activator.log(ex);
				}
			}
		}
	}
}
