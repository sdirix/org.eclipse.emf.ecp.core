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
package org.eclipse.emf.ecp.internal.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import org.osgi.framework.BundleContext;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public final class Activator extends AbstractUIPlugin
{
  public static final String PLUGIN_ID = "org.eclipse.emf.ecp.ui"; //$NON-NLS-1$

  private static Activator instance;

  public Activator()
  {
  }

  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    instance = this;
    UIProviderRegistryImpl.INSTANCE.activate();
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    UIProviderRegistryImpl.INSTANCE.deactivate();
    ResourceManager.dispose();
    SWTResourceManager.dispose();
    instance = null;
    super.stop(context);
  }

  public static Activator getInstance()
  {
    return instance;
  }

  public static void log(String message)
  {
    instance.getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
  }

  public static void log(String message, Throwable t)
  {
    if (t instanceof CoreException)
    {
      CoreException coreException = (CoreException)t;
      instance.getLog().log(coreException.getStatus());
    }
    else
    {
      instance.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message, t));
    }
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

  public static ImageDescriptor getImageDescriptor(String path)
  {
    return ResourceManager.getPluginImageDescriptor(PLUGIN_ID, path);
  }

  public static Image getImage(String path)
  {
    return ResourceManager.getPluginImage(PLUGIN_ID, path);
  }
}
