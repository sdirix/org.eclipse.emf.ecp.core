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
 ******************************************************************************/
package org.eclipse.emf.ecp.workspace.internal.core;

import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.exception.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.InternalRepository;

import org.eclipse.core.resources.IResource;

/**
 * @author Eugen Neufeld
 * 
 */
public class FileCheckoutSource extends ResourceWrapper<InternalRepository> implements ECPCheckoutSource {

	/**
	 * @param context
	 * @param delegate
	 */
	public FileCheckoutSource(InternalRepository context, IResource delegate) {
		super(context, delegate);
	}

	/** {@inheritDoc} */
	public InternalRepository getRepository() {
		return getContext();
	}

	/** {@inheritDoc} */
	public String getDefaultCheckoutName() {
		return getName();
	}

	/** {@inheritDoc} */
	public void checkout(String projectName, ECPProperties projectProperties) throws ProjectWithNameExistsException {
		projectProperties.addProperty(WorkspaceProvider.PROP_ROOT_URI, getURI().toString());
		ECPProjectManager.INSTANCE.createProject(getRepository(), projectName, projectProperties);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.workspace.internal.core.ResourceWrapper#createChild(org.eclipse.emf.ecp.core.util.ECPModelContext
	 * , org.eclipse.core.resources.IResource)
	 */
	@Override
	protected Object createChild(InternalRepository context, IResource member) {
		return new FileCheckoutSource(context, member);
	}

}
