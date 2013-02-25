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
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESLocalProjectImpl;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESServerImpl;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UICommitProjectController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the EMFStore Commit Handler delegating to the EMFStore {@link UICommitProjectController}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class CommitProjectHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		InternalProject project = (InternalProject) ((IStructuredSelection) HandlerUtil.getActiveMenuSelection(event))
			.getFirstElement();
		ESLocalProjectImpl localProject = (ESLocalProjectImpl) EMFStoreProvider.INSTANCE.getProjectSpace(project);
		// TODO EMFStore how to set usersession?
		// -> why is this necessary? The project is already checked out
		if (localProject.getUsersession() == null) {
			ESServerImpl server = (ESServerImpl) EMFStoreProvider.INSTANCE.getServerInfo(project.getRepository());
			ServerInfo serverInfo = server.getInternalAPIImpl();
			localProject.getInternalAPIImpl().setUsersession(serverInfo.getLastUsersession());
		}
		// ESUIControllerFactory.INSTANCE.commitProject(HandlerUtil.getActiveShell(event), projectSpace);
		new UICommitProjectController(HandlerUtil.getActiveShell(event), localProject.getInternalAPIImpl()).execute();
		// is structural because of possible merge
		project.notifyObjectsChanged(new Object[] { project }, true);
		project.getRepository().notifyObjectsChanged(new Object[] { project.getRepository() });
		return null;
	}
}
