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
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.ui.platform;

import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.ecp.ui.workbench"; //$NON-NLS-1$

	// The shared instance
	private static Activator instance;
	
	/**
	 * The constructor.
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		instance = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
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
	    // return ResourceManager.getPluginImageDescriptor(PLUGIN_ID, path);
	    ImageDescriptor id = getDefault().getImageRegistry().getDescriptor(path);
	    if (id == null)
	    {
	      id = loadImageDescriptor(path);
	    }
	    return id;
	  }

	  public static Image getImage(String path)
	  {
	    // return ResourceManager.getPluginImage(PLUGIN_ID, path);
	    Image image = getDefault().getImageRegistry().get(path);
	    if (image == null)
	    {
	      image = loadImage(path);
	    }
	    return image;
	  }

	  /**
	   * @param path
	   * @return
	   */
	  private static Image loadImage(String path)
	  {
	    ImageDescriptor id = loadImageDescriptor(path);
	    if (id == null)
	    {
	      return null;
	    }
	    getDefault().getImageRegistry().put(path, id);
	    return getDefault().getImageRegistry().get(path);
	  }

	  /**
	   * @param path
	   * @return
	   */
	  private static ImageDescriptor loadImageDescriptor(String path)
	  {
	    URL url = FileLocator.find(Platform.getBundle(PLUGIN_ID), new Path(path), null);
	    if (url == null)
	    {
	      return null;
	    }
	    ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
	    getDefault().getImageRegistry().put(path, imageDescriptor);
	    return imageDescriptor;
	  }
}
