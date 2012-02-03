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
package org.eclipse.emf.ecp.core;

import org.eclipse.emf.ecp.core.util.ECPProperties;

/**
 * @author Eike Stepper
 */
public interface ECPProjectManager
{
  public static final ECPProjectManager INSTANCE = org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl.INSTANCE;

  public ECPProject createProject(ECPProvider provider, String name, ECPProperties properties);

  public ECPProject getProject(Object adaptable);

  public ECPProject getProject(String name);

  public ECPProject[] getProjects();

  public boolean hasProjects();

  public void addListener(Listener listener);

  public void removeListener(Listener listener);

  /**
   * @author Eike Stepper
   */
  public interface Listener
  {
    public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception;

    public void projectChanged(ECPProject project, boolean opened) throws Exception;

    public void objectsChanged(ECPProject project, Object[] objects) throws Exception;
  }
}
