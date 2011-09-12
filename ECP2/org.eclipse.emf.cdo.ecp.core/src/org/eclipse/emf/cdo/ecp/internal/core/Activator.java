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

import org.eclipse.emf.cdo.server.CDOServerBrowser;
import org.eclipse.emf.cdo.spi.server.InternalRepository;
import org.eclipse.emf.cdo.spi.workspace.InternalCDOWorkspace;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.spi.core.InternalProject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

import org.osgi.framework.BundleContext;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class Activator extends Plugin
{
  public static final String PLUGIN_ID = "org.eclipse.emf.cdo.ecp.core";

  private static Activator instance;

  private CDOServerBrowser serverBrowser;

  @Override
  public void start(BundleContext bundleContext) throws Exception
  {
    super.start(bundleContext);
    instance = this;

    serverBrowser = new CDOServerBrowser(null)
    {
      @Override
      protected Set<String> getRepositoryNames()
      {
        Set<String> names = new HashSet<String>();
        for (ECPProject project : ECPProjectManager.INSTANCE.getProjects())
        {
          if (project.getProvider().getName().equals(CDOProvider.NAME))
          {
            CDOProjectData projectData = CDOProvider.getProjectData((InternalProject)project);
            InternalCDOWorkspace workspace = (InternalCDOWorkspace)projectData.getWorkspace();
            if (workspace != null)
            {
              InternalRepository localRepository = workspace.getLocalRepository();
              names.add(localRepository.getName());
            }
          }
        }

        return names;
      }

      @Override
      protected InternalRepository getRepository(String name)
      {
        for (ECPProject project : ECPProjectManager.INSTANCE.getProjects())
        {
          if (project.getProvider().getName().equals(CDOProvider.NAME))
          {
            CDOProjectData projectData = CDOProvider.getProjectData((InternalProject)project);
            InternalCDOWorkspace workspace = (InternalCDOWorkspace)projectData.getWorkspace();
            if (workspace != null)
            {
              InternalRepository localRepository = workspace.getLocalRepository();
              if (localRepository.getName().equals(name))
              {
                return localRepository;
              }
            }
          }
        }

        return null;
      }
    };

    serverBrowser.setPort(7778);
    serverBrowser.activate();
  }

  @Override
  public void stop(BundleContext bundleContext) throws Exception
  {
    serverBrowser.deactivate();
    serverBrowser = null;

    if (CDOProvider.INSTANCE != null)
    {
      CDOProvider.INSTANCE.dispose();
    }

    instance = null;
    super.stop(bundleContext);
  }

  public static Activator getInstance()
  {
    return instance;
  }

  public static void log(String message)
  {
    instance.getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
  }

  public static void log(IStatus status)
  {
    instance.getLog().log(status);
  }

  public static String log(Throwable t)
  {
    IStatus status = getStatus(t);
    log(status);
    return status.getMessage();
  }

  public static IStatus getStatus(Throwable t)
  {
    if (t instanceof CoreException)
    {
      CoreException coreException = (CoreException)t;
      return coreException.getStatus();
    }

    String msg = t.getLocalizedMessage();
    if (msg == null || msg.length() == 0)
    {
      msg = t.getClass().getName();
    }

    return new Status(IStatus.ERROR, PLUGIN_ID, msg, t);
  }
}
