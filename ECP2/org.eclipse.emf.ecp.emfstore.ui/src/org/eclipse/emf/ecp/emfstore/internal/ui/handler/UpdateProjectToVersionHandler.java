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

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESLocalProjectImpl;
import org.eclipse.emf.emfstore.internal.client.model.impl.api.ESUsersessionImpl;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UIUpdateProjectToVersionController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the EMFStore UpdateProjectToVersion Handler delegating to the EMFStore
 * {@link UIUpdateProjectToVersionController}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class UpdateProjectToVersionHandler extends AbstractHandler {
	/** {@inheritDoc} **/
	public Object execute(ExecutionEvent event) throws ExecutionException {
		InternalProject project = (InternalProject) ((IStructuredSelection) HandlerUtil.getActiveMenuSelection(event))
			.getFirstElement();
		ESLocalProject projectSpace = EMFStoreProvider.INSTANCE.getProjectSpace(project);
		// TODO Ugly
		if (projectSpace.getUsersession() == null) {
			ESServer serverInfo = EMFStoreProvider.INSTANCE.getServerInfo(project.getRepository());
			((ESLocalProjectImpl) projectSpace).toInternalAPI().setUsersession(
				((ESUsersessionImpl) serverInfo.getLastUsersession()).toInternalAPI());
		}
		new UIUpdateProjectToVersionController(HandlerUtil.getActiveShell(event), projectSpace).execute();
		project.notifyObjectsChanged(new Object[] { project }, true);

		return null;
	}

}
