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
package org.eclipse.emf.ecp.emfstore.internal.ui.property;

import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProjectWrapper;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.emfstore.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.internal.client.model.ServerInfo;
import org.eclipse.emf.emfstore.internal.client.model.Usersession;
import org.eclipse.emf.emfstore.internal.client.model.accesscontrol.AccessControlHelper;
import org.eclipse.emf.emfstore.internal.server.exceptions.AccessControlException;

import org.eclipse.core.expressions.PropertyTester;

/**
 * This tests whether a user is the serveradmin on a specific repository.
 * 
 * @author Eugen Neufeld
 */
public final class EMFStoreIsServerAdminTester extends PropertyTester {

	/** {@inheritDoc} */
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (!(receiver instanceof EMFStoreProjectWrapper)) {
			return false;
		}
		EMFStoreProjectWrapper wrapper = (EMFStoreProjectWrapper) receiver;
		ServerInfo serverInfo = EMFStoreProvider.INSTANCE.getServerInfo((InternalRepository) wrapper.getRepository());

		Usersession usersession = serverInfo.getLastUsersession();
		boolean isAdmin = false;
		if (usersession != null && usersession.getACUser() != null) {
			AccessControlHelper accessControlHelper = new AccessControlHelper(usersession);
			try {
				accessControlHelper.checkServerAdminAccess();
				isAdmin = true;
			} catch (AccessControlException e) {
				Activator.log(e);
			}
		}

		return new Boolean(isAdmin).equals(expectedValue);
	}

}
