/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.ecp.internal.core;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.workspace.CDOWorkspace;
import org.eclipse.emf.cdo.workspace.CDOWorkspaceConfiguration;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.core.InternalProject;

/**
 * @author Eike Stepper
 */
public final class CDOProjectData
{
  private final InternalProject project;

  private CDOWorkspace workspace;

  private CDOTransaction transaction;

  private CDOResource rootResource;

  public CDOProjectData(InternalProject project)
  {
    this.project = project;
  }

  public InternalProject getProject()
  {
    return project;
  }

  public synchronized CDOWorkspace getWorkspace()
  {
    if (workspace == null)
    {
      CDOWorkspaceConfiguration config = createWorkspaceConfiguration();
      workspace = config.open();
    }

    return workspace;
  }

  public synchronized CDOWorkspace checkoutWorkspace()
  {
    CDOWorkspaceConfiguration config = createWorkspaceConfiguration();
    return workspace = config.checkout();
  }

  public synchronized CDOTransaction getTransaction()
  {
    if (transaction == null)
    {
      ResourceSet resourceSet = project.getEditingDomain().getResourceSet();
      transaction = getWorkspace().openTransaction(resourceSet);
    }

    return transaction;
  }

  public synchronized CDOResource getRootResource()
  {
    if (rootResource == null)
    {
      rootResource = getTransaction().getRootResource();
    }

    return rootResource;
  }

  public void dispose()
  {
    if (rootResource != null)
    {
      rootResource = null;
    }

    if (transaction != null)
    {
      transaction.close();
      transaction = null;
    }

    if (workspace != null)
    {
      workspace.close();
      workspace = null;
    }
  }

  private CDOWorkspaceConfiguration createWorkspaceConfiguration()
  {
    CDOProvider provider = (CDOProvider)ECPUtil.getResolvedElement(project.getProvider());
    return provider.createWorkspaceConfiguration(project);
  }
}
