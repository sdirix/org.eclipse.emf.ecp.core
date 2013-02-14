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

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProjectWrapper;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UIDeleteRemoteProjectController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the EMFStore DeleteOnServer Handler delegating to the EMFStore {@link UIDeleteRemoteProjectController}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class DeleteOnServerHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if (sel instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) sel;
			Object selection = ssel.getFirstElement();
			if (selection instanceof EMFStoreProjectWrapper) {
				EMFStoreProjectWrapper projectWrapper = (EMFStoreProjectWrapper) selection;

				InternalRepository repo = (InternalRepository) projectWrapper.getRepository();
				ServerInfo serverInfo = EMFStoreProvider.INSTANCE.getServerInfo(repo);
				new UIDeleteRemoteProjectController(HandlerUtil.getActiveShell(event), serverInfo.getLastUsersession(),
					projectWrapper.getCheckoutData()).execute();
				repo.notifyObjectsChanged(new Object[] { repo });
			}
		}
		return null;
	}

}
