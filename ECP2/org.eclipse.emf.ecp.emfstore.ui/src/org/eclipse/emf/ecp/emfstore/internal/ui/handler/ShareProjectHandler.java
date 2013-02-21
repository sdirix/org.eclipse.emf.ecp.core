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
 *******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.internal.wizards.ShareWizard;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.internal.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UIShareProjectController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the EMFStore ShareProject Handler delegating to the EMFStore {@link UIShareProjectController}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ShareProjectHandler extends AbstractHandler {
	/** {@inheritDoc} **/
	public Object execute(ExecutionEvent event) throws ExecutionException {
		InternalProject project = (InternalProject) ((IStructuredSelection) HandlerUtil.getActiveMenuSelection(event))
			.getFirstElement();

		ShareWizard rw = new ShareWizard();
		rw.init(project.getProvider());

		WizardDialog wd = new WizardDialog(HandlerUtil.getActiveShell(event), rw);
		int result = wd.open();
		if (result == WizardDialog.OK) {
			// TODO internal cast again
			InternalRepository repository = (InternalRepository) rw.getSelectedRepository();
			project.undispose(repository);
			ProjectSpace projectSpace = EMFStoreProvider.INSTANCE.getProjectSpace(project);
			// TODO Ugly
			if (projectSpace.getUsersession() == null) {
				ServerInfo serverInfo = EMFStoreProvider.INSTANCE.getServerInfo(project.getRepository());
				projectSpace.setUsersession(serverInfo.getLastUsersession());
			}
			new UIShareProjectController(HandlerUtil.getActiveShell(event), projectSpace).execute();

			project.notifyObjectsChanged(new Object[] { project }, false);
			repository.notifyObjectsChanged(new Object[] { repository });
		}
		return null;
	}
}
