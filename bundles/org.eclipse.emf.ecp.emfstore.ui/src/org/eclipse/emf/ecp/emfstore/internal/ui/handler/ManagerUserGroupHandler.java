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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UIManageOrgUnitsController;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the EMFStore Manage Users/Groups Handler delegating to the EMFStore {@link UIManageOrgUnitsController}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ManagerUserGroupHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		final ECPRepository ecpRepository = (ECPRepository) ((IStructuredSelection) HandlerUtil
			.getActiveMenuSelection(event)).getFirstElement();
		final ESServer serverInfo = EMFStoreProvider.INSTANCE.getServerInfo((InternalRepository) ecpRepository);
		// TODO EMFStore Constructor is missing
		new UIManageOrgUnitsController(HandlerUtil.getActiveShell(event), serverInfo.getLastUsersession()).execute();
		return null;
	}

}
