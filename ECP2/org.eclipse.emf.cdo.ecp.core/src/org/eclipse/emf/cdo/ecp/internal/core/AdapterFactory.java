/**
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

import org.eclipse.emf.cdo.workspace.CDOWorkspace;

import org.eclipse.emf.ecp.spi.core.InternalProject;

import org.eclipse.core.runtime.IAdapterFactory;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("rawtypes")
public class AdapterFactory implements IAdapterFactory
{
  private static final Class[] CLASSES = { CDOWorkspace.class };

  public AdapterFactory()
  {
  }

  public Class[] getAdapterList()
  {
    return CLASSES;
  }

  public Object getAdapter(Object adaptableObject, Class adapterType)
  {
    if (adapterType == CLASSES[0])
    {
      if (adaptableObject instanceof InternalProject)
      {
        InternalProject project = (InternalProject)adaptableObject;
        if (project.isOpen() && project.getProvider().getName().equals(CDOProvider.NAME))
        {
          CDOProjectData data = CDOProvider.getProjectData(project);
          return data.getWorkspace();
        }
      }
    }

    return null;
  }
}
