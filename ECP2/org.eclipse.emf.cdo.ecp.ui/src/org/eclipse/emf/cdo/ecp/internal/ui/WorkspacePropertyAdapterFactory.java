/**
 * Copyright (c) 2004 - 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.ecp.internal.ui;

import org.eclipse.emf.cdo.ecp.internal.core.CDOProjectData;
import org.eclipse.emf.cdo.ecp.internal.core.CDOProvider;
import org.eclipse.emf.cdo.workspace.CDOWorkspace;
import org.eclipse.emf.cdo.workspace.CDOWorkspaceUtil;

import org.eclipse.net4j.util.ui.AbstractPropertyAdapterFactory;
import org.eclipse.net4j.util.ui.DefaultPropertySource;

import org.eclipse.emf.ecp.spi.core.InternalProject;

import org.eclipse.ui.views.properties.IPropertySource;

/**
 * @author Eike Stepper
 */
public class WorkspacePropertyAdapterFactory extends AbstractPropertyAdapterFactory
{
  public WorkspacePropertyAdapterFactory()
  {
  }

  @Override
  protected IPropertySource createPropertySource(Object object)
  {
    if (object instanceof InternalProject)
    {
      InternalProject project = (InternalProject)object;
      if (project.getProvider().getName().equals(CDOProvider.NAME))
      {
        CDOProjectData data = CDOProvider.getProjectData(project);
        CDOWorkspace workspace = data.getWorkspace();
        return new DefaultPropertySource<CDOWorkspace>(workspace, CDOWorkspaceUtil.PROPERTIES);
      }
    }

    return null;
  }
}
