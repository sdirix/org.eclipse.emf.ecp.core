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

package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.server.model.ProjectInfo;

/**
 * This class holds a {@link ServerInfo} and a corresponding {@link ProjectInfo} object.
 * 
 * @author Eugen Neufeld
 */
public class EMFStoreCheckoutData {
	private final ServerInfo serverInfo;

	private final ProjectInfo projectInfo;

	/**
	 * @param serverInfo the {@link ServerInfo} to use
	 * @param projectInfo the {@link ProjectInfo} to use
	 */
	public EMFStoreCheckoutData(ServerInfo serverInfo, ProjectInfo projectInfo) {
		super();
		this.serverInfo = serverInfo;
		this.projectInfo = projectInfo;
	}

	/**
	 * @return the serverInfo
	 */
	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	/**
	 * @return the projectInfo
	 */
	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

}
