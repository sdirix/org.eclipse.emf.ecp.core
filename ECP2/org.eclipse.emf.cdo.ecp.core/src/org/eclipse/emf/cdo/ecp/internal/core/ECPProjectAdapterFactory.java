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

import org.eclipse.emf.cdo.workspace.CDOWorkspace;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.spi.core.InternalProject;

import org.eclipse.core.runtime.IAdapterFactory;

/**
 * @author Eike Stepper
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ECPProjectAdapterFactory implements IAdapterFactory
{
  private static final Class[] CLASSES = { CDOWorkspace.class };

  public ECPProjectAdapterFactory()
  {
  }

  public Class[] getAdapterList()
  {
    return CLASSES;
  }

  public Object getAdapter(Object adaptable, Class adapterType)
  {
    return adapt(adaptable, adapterType);
  }

  public static <T> T adapt(Object adaptable, Class<T> adapterType)
  {
    if (adapterType == CLASSES[0])
    {
      if (adaptable instanceof ECPProject)
      {
        ECPProject project = (ECPProject)adaptable;
        if (project.isOpen() && project.getProvider().getName().equals(CDOProvider.NAME))
        {
          CDOProjectData data = CDOProvider.getProjectData((InternalProject)project);
          return (T)data.getWorkspace();
        }
      }
    }

    return null;
  }
}
