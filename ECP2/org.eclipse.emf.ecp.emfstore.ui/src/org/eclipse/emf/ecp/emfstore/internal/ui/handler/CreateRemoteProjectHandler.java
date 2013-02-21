/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 ******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UICreateRemoteProjectController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the EMFStore Register EPackage Handler delegating to the EMFStore {@link RegisterEPackageHandler}.
 * 
 * @author Tobias Verhoeven
 * 
 */
public class CreateRemoteProjectHandler extends AbstractHandler {

	/** {@inheritDoc} **/
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final InternalRepository ecpRepository = (InternalRepository) ((IStructuredSelection) HandlerUtil
			.getActiveMenuSelection(event)).getFirstElement();
		final ServerInfo serverInfo = EMFStoreProvider.INSTANCE.getServerInfo(ecpRepository);
		// FIXME:
		InputDialog dialog = new InputDialog(HandlerUtil.getActiveShell(event), "Remote Project Name",
			"Please enter a name", "", null);

		String projectName = null;
		if (dialog.open() == Dialog.OK) {
			projectName = dialog.getValue();
		}

		if (projectName == null) {
			return null;
		}

		new UICreateRemoteProjectController(HandlerUtil.getActiveShell(event), serverInfo.getLastUsersession(),
			projectName, "").execute();
		ecpRepository.notifyObjectsChanged(new Object[] { ecpRepository });
		return null;
	}
}
