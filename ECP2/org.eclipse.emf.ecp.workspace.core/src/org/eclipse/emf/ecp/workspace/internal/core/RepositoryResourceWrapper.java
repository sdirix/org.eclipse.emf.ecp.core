/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.workspace.internal.core;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.spi.core.InternalRepository;

import org.eclipse.core.resources.IResource;

/**
 * @author Eike Stepper
 */
public class RepositoryResourceWrapper extends ResourceWrapper<InternalRepository> {
	public RepositoryResourceWrapper(ECPRepository repository, IResource resource) {
		super((InternalRepository) repository, resource);
	}

	//
	// public InternalRepository getRepository() {
	// return getContext();
	// }
	//
	// public String getDefaultCheckoutName() {
	// return getName();
	// }
	//
	// public void checkout(String projectName, ECPProperties projectProperties) throws ProjectWithNameExistsException {
	// projectProperties.addProperty(WorkspaceProvider.PROP_ROOT_URI, getURI().toString());
	// ECPProjectManager.INSTANCE.createProject(getRepository(), projectName, projectProperties);
	// }

	@Override
	protected Object createChild(InternalRepository repository, IResource member) {
		if (member.getType() == IResource.FILE) {
			return new FileCheckoutSource(repository, member);
		}
		return new RepositoryResourceWrapper(repository, member);
	}
}
