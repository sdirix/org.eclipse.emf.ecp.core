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

import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.exception.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.client.ILocalProject;
import org.eclipse.emf.emfstore.client.IRemoteProject;

/**
 * This is the EMFStore implementation of a {@link ECPCheckoutSource}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class EMFStoreProjectWrapper implements ECPCheckoutSource {

	private final InternalRepository repository;

	private final IRemoteProject remoteProject;

	/**
	 * The Constructor fro creating an {@link EMFStoreProjectWrapper}.
	 * 
	 * @param repository the repository for this CheckoutSource
	 * @param checkoutData the container holding relevant information
	 */
	public EMFStoreProjectWrapper(InternalRepository repository, IRemoteProject remoteProject) {
		this.repository = repository;
		this.remoteProject = remoteProject;
	}

	/** {@inheritDoc} **/
	public ECPRepository getRepository() {
		return repository;
	}

	/** {@inheritDoc} **/
	public ECPProvider getProvider() {
		return repository.getProvider();
	}

	/** {@inheritDoc} **/
	public String getDefaultCheckoutName() {
		return remoteProject.getProjectName();
	}

	/** {@inheritDoc} **/
	public void checkout(String projectName, ECPProperties projectProperties) throws ProjectWithNameExistsException {

		ILocalProject projectSpace = EMFStoreProvider.INSTANCE.getUIProvider().getAdapter(remoteProject,
			ILocalProject.class);
		if (projectSpace != null) {
			projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, projectSpace.getLocalProjectId()
				.getId());
		}
		ECPProjectManager.INSTANCE.createProject(getRepository(), projectName, projectProperties);
	}

	/**
	 * This return the {@link EMFStoreCheckoutData} used in this wrapper.
	 * 
	 * @return the {@link EMFStoreCheckoutData} used
	 */
	public IRemoteProject getCheckoutData() {
		return remoteProject;
	}

}
