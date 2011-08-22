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
package org.eclipse.emf.ecp.ui.model;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;

/**
 * @author Eike Stepper
 */
public class ModelContentProvider extends ECPContentProvider<ECPProjectManager> implements ECPProjectManager.Listener
{
  public ModelContentProvider()
  {
  }

  public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception
  {
    refreshViewer();
  }

  public void projectChanged(ECPProject project, boolean opened) throws Exception
  {
    refreshViewer(project);
  }

  @Override
  protected void connectInput(ECPProjectManager input)
  {
    ECPProjectManager.INSTANCE.addListener(this);
  }

  @Override
  protected void disconnectInput(ECPProjectManager input)
  {
    ECPProjectManager.INSTANCE.removeListener(this);
  }

  @Override
  protected void fillChildren(Object parent, InternalChildrenList childrenList)
  {
    if (parent == ECPProjectManager.INSTANCE)
    {
      childrenList.addChildren(ECPProjectManager.INSTANCE.getProjects());
    }
    else
    {
      super.fillChildren(parent, childrenList);
    }
  }
}
