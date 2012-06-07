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
package org.eclipse.emf.ecp.internal.core;

import org.eclipse.net4j.util.AdapterUtil;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProjectManager.Listener;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.ECPProjectAware;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.spi.core.InternalRepository;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.Collections;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ECPProjectManagerImpl extends PropertiesStore<InternalProject, Listener> implements ECPProjectManager,
    ECPRepositoryManager.Listener
{
  public static final ECPProjectManagerImpl INSTANCE = new ECPProjectManagerImpl();

  private ECPProjectManagerImpl()
  {
  }

  public ECPProject createProject(ECPProvider provider, String name, ECPProperties properties)
  {
    InternalProject project = new ECPProjectImpl((InternalProvider)provider, name, properties);
    return createProject(project);
  }

  public ECPProject createProject(ECPRepository repository, String name, ECPProperties properties)
  {
    InternalProject project = new ECPProjectImpl(repository, name, properties);
    return createProject(project);

  }

  /**
   * @param project
   * @return
   */
  private ECPProject createProject(InternalProject project)
  {
    project.getProvider().handleLifecycle(project, LifecycleEvent.CREATE);
    changeElements(null, Collections.singleton(project));
    return project;
  }

  public InternalProject getProject(Object adaptable)
  {
    if (adaptable instanceof ECPProjectAware)
    {
      ECPProjectAware projectAware = (ECPProjectAware)adaptable;
      return (InternalProject)projectAware.getProject();
    }

    return AdapterUtil.adapt(adaptable, InternalProject.class);
  }

  public InternalProject getProject(String name)
  {
    return getElement(name);
  }

  public InternalProject[] getProjects()
  {
    return getElements();
  }

  public boolean hasProjects()
  {
    return hasElements();
  }

  public void changeProject(ECPProject project, boolean opened, boolean store)
  {
    if (store)
    {
      storeElement((InternalProject)project);
    }

    Listener[] listeners = getRegistryListeners();
    if (listeners != null)
    {
      for (int i = 0; i < listeners.length; i++)
      {
        try
        {
          Listener listener = listeners[i];
          listener.projectChanged(project, opened);
        }
        catch (Exception ex)
        {
          Activator.log(ex);
        }
      }
    }
  }

  public void notifyObjectsChanged(ECPProject project, Object[] objects)
  {
    Listener[] listeners = getRegistryListeners();
    if (listeners != null)
    {
      for (Listener listener : listeners)
      {
        try
        {
          listener.objectsChanged(project, objects);
        }
        catch (Exception ex)
        {
          Activator.log(ex);
        }
      }
    }
  }

  public void repositoriesChanged(ECPRepository[] oldRepositories, ECPRepository[] newRepositories) throws Exception
  {
    Set<ECPRepository> addedRepositories = ECPUtil.getAddedElements(oldRepositories, newRepositories);
    InternalProject[] projects = getProjects();

    for (ECPRepository repository : addedRepositories)
    {
      for (InternalProject project : projects)
      {
        if (!project.isOpen() && project.getRepository().getName().equals(repository.getName()))
        {
          project.undispose((InternalRepository)repository);
        }
      }
    }
  }

  public void objectsChanged(ECPRepository repository, Object[] objects) throws Exception
  {
    // Do nothing
  }

  @Override
  protected void elementsChanged(InternalProject[] oldElements, InternalProject[] newElements)
  {
    super.elementsChanged(oldElements, newElements);
  }

  @Override
  protected void doActivate() throws Exception
  {
    super.doActivate();
    ECPRepositoryManager.INSTANCE.addListener(this);
  }

  @Override
  protected void doDeactivate() throws Exception
  {
    ECPRepositoryManager.INSTANCE.removeListener(this);
    super.doDeactivate();
  }

  @Override
  protected InternalProject loadElement(ObjectInput in) throws IOException
  {
    return new ECPProjectImpl(in);
  }

  @Override
  protected InternalProject[] createElementArray(int size)
  {
    return new InternalProject[size];
  }

  @Override
  protected Listener[] createListenerArray(int size)
  {
    return new Listener[size];
  }

  @Override
  protected void notifyListener(Listener listener, InternalProject[] oldElements, InternalProject[] newElements)
      throws Exception
  {
    listener.projectsChanged(oldElements, newElements);
  }

  @Override
  protected boolean isRemoveDisposedElements()
  {
    return false;
  }
}
