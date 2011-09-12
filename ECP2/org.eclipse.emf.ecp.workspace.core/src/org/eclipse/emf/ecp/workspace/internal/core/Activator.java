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
package org.eclipse.emf.ecp.workspace.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

import org.osgi.framework.BundleContext;

/**
 * @author Eike Stepper
 */
public class Activator extends Plugin
{
  public static final String PLUGIN_ID = "org.eclipse.emf.ecp.workspace.core";

  private static Activator instance;

  @Override
  public void start(BundleContext bundleContext) throws Exception
  {
    super.start(bundleContext);
    instance = this;
  }

  @Override
  public void stop(BundleContext bundleContext) throws Exception
  {
    if (WorkspaceProvider.INSTANCE != null)
    {
      WorkspaceProvider.INSTANCE.dispose();
    }

    instance = null;
    super.stop(bundleContext);
  }

  public static final Activator getInstance()
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
