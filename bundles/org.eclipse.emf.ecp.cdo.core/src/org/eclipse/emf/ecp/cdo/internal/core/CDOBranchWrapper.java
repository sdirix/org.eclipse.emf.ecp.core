/*******************************************************************************
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.ecp.cdo.internal.core;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.core.InternalRepository;

/**
 * @author Eike Stepper
 */
public class CDOBranchWrapper implements ECPCheckoutSource {
	private final InternalRepository repository;

	private final String branchPath;

	public CDOBranchWrapper(InternalRepository repository, String branchPath) {
		this.repository = repository;
		this.branchPath = branchPath;
	}

	/** {@inheritDoc} */
	public ECPProvider getProvider() {
		return repository.getProvider();
	}

	/** {@inheritDoc} */
	public final InternalRepository getRepository() {
		return repository;
	}

	public final String getBranchPath() {
		return branchPath;
	}

	public String getName() {
		int pos = branchPath.lastIndexOf('/');
		if (pos == -1) {
			return branchPath;
		}

		return branchPath.substring(pos + 1);
	}

	/** {@inheritDoc} */
	public String getDefaultCheckoutName() {
		return repository.getName() + "." + getName();
	}

	/** {@inheritDoc} */
	public void checkout(String projectName, ECPProperties projectProperties) throws ECPProjectWithNameExistsException {
		projectProperties.addProperty("branchPath", branchPath);
		ECPUtil.getECPProjectManager().createProject(getRepository(), projectName, projectProperties);
	}

	@Override
	public String toString() {
		return getName();
	}
}
