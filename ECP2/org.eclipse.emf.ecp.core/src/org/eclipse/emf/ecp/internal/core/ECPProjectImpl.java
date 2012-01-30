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

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.ECPDisposable;
import org.eclipse.emf.ecp.core.util.ECPDisposable.DisposeListener;
import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.internal.core.util.PropertiesElement;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;

/**
 * @author Eike Stepper
 */
public final class ECPProjectImpl extends PropertiesElement implements InternalProject, DisposeListener
{
  private InternalRepository repository;

  private Object providerSpecificData;

  private EditingDomain editingDomain;

  private boolean open;

  public ECPProjectImpl(ECPRepository repository, String name, ECPProperties properties)
  {
    super(name, properties);

    if (repository == null)
    {
      throw new IllegalArgumentException("Repository is null");
    }

    setRepository((InternalRepository)repository);
    open = true;
  }

  public ECPProjectImpl(ObjectInput in) throws IOException
  {
    super(in);

    String repositoryName = in.readUTF();
    InternalRepository repository = (InternalRepository)ECPRepositoryManager.INSTANCE.getRepository(repositoryName);
    if (repository == null)
    {
      repository = new Disposed(repositoryName);
    }

    setRepository(repository);
    open = in.readBoolean();
  }

  public String getType()
  {
    return TYPE;
  }

  public void disposed(ECPDisposable disposable)
  {
    if (disposable == repository)
    {
      dispose();
    }
  }

  public boolean isStorable()
  {
    return true;
  }

  @Override
  public void write(ObjectOutput out) throws IOException
  {
    super.write(out);
    out.writeUTF(repository.getName());
    out.writeBoolean(open);
  }

  public InternalProject getProject()
  {
    return this;
  }

  public InternalRepository getRepository()
  {
    return repository;
  }

  private void setRepository(InternalRepository repository)
  {
    if (this.repository != null)
    {
      this.repository.removeDisposeListener(this);
    }

    this.repository = repository;

    if (this.repository != null)
    {
      this.repository.addDisposeListener(this);
    }
  }

  public InternalProvider getProvider()
  {
    return getRepository().getProvider();
  }

  public Object getProviderSpecificData()
  {
    return providerSpecificData;
  }

  public void setProviderSpecificData(Object providerSpecificData)
  {
    this.providerSpecificData = providerSpecificData;
  }

  public void notifyObjectsChanged(Object[] objects)
  {
    if (objects != null && objects.length != 0)
    {
      ECPProjectManagerImpl.INSTANCE.notifyObjectsChanged(this, objects);
    }
  }

  public synchronized EditingDomain getEditingDomain()
  {
    if (editingDomain == null)
    {
      editingDomain = getProvider().createEditingDomain(this);
    }

    return editingDomain;
  }

  /**
   * Returns an object which is an instance of the given class associated with this object. Returns <code>null</code> if
   * no such object can be found.
   * <p>
   * This implementation of the method declared by <code>IAdaptable</code> passes the request along to the platform's
   * adapter manager; roughly <code>Platform.getAdapterManager().getAdapter(this, adapter)</code>. Subclasses may
   * override this method (however, if they do so, they should invoke the method on their superclass to ensure that the
   * Platform's adapter manager is consulted).
   * </p>
   * 
   * @param adapterType
   *          the class to adapt to
   * @return the adapted object or <code>null</code>
   * @see IAdaptable#getAdapter(Class)
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Object getAdapter(Class adapterType)
  {
    InternalProvider provider = getProvider();
    if (provider != null && !provider.isDisposed())
    {
      Object result = provider.getAdapter(this, adapterType);
      if (result != null)
      {
        return result;
      }
    }

    return Platform.getAdapterManager().getAdapter(this, adapterType);
  }

  public ECPModelContext getContext()
  {
    return this;
  }

  public boolean canDelete()
  {
    return true;
  }

  public void delete()
  {
    try
    {
      getProvider().handleLifecycle(this, LifecycleEvent.REMOVE);
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }

    ECPProjectManagerImpl.INSTANCE.changeElements(Collections.singleton(getName()), null);
  }

  public synchronized boolean isOpen()
  {
    return !isDisposed() && open;
  }

  public synchronized void open()
  {
    if (!isDisposed())
    {
      setOpen(true);
    }
  }

  public synchronized void close()
  {
    if (!isDisposed())
    {
      setOpen(false);
    }
  }

  private void setOpen(boolean open)
  {
    boolean modified = false;
    synchronized (this)
    {
      if (open != this.open)
      {
        this.open = open;
        modified = true;

        notifyProvider(open ? LifecycleEvent.INIT : LifecycleEvent.DISPOSE);

        if (!open)
        {
          providerSpecificData = null;
          editingDomain = null;
        }
      }
    }

    if (modified)
    {
      ECPProjectManagerImpl.INSTANCE.changeProject(this, open, true);
    }
  }

  private void notifyProvider(LifecycleEvent event)
  {
    InternalProvider provider = getProvider();
    provider.handleLifecycle(this, event);
  }

  public void undispose(InternalRepository repository)
  {
    setRepository(repository);
    notifyProvider(LifecycleEvent.INIT);

    if (open)
    {
      ECPProjectManagerImpl.INSTANCE.changeProject(this, true, false);
    }
  }

  private void dispose()
  {
    notifyProvider(LifecycleEvent.DISPOSE);
    setRepository(new Disposed(repository.getName()));

    providerSpecificData = null;
    editingDomain = null;

    ECPProjectManagerImpl.INSTANCE.changeProject(this, false, false);
  }

  private boolean isDisposed()
  {
    return repository.isDisposed();
  }

  /**
   * @author Eike Stepper
   */
  private static final class Disposed implements InternalRepository
  {
    private final String name;

    public Disposed(String name)
    {
      this.name = name;
    }

    public String getType()
    {
      return TYPE;
    }

    public String getName()
    {
      return name;
    }

    public boolean isDisposed()
    {
      return true;
    }

    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter)
    {
      return null;
    }

    public String getLabel()
    {
      return null;
    }

    public String getDescription()
    {
      return null;
    }

    public ECPProject[] getOpenProjects()
    {
      return null;
    }

    public int compareTo(ECPElement o)
    {
      return 0;
    }

    public ECPModelContext getContext()
    {
      return null;
    }

    public String getDefaultCheckoutName()
    {
      return null;
    }

    public ECPProject checkout(String projectName, ECPProperties projectProperties)
    {
      return null;
    }

    public ECPRepository getRepository()
    {
      return null;
    }

    public ECPProperties getProperties()
    {
      return null;
    }

    public boolean canDelete()
    {
      return false;
    }

    public void delete()
    {
    }

    public boolean isStorable()
    {
      return false;
    }

    public void write(ObjectOutput out) throws IOException
    {
    }

    public void setLabel(String label)
    {
    }

    public void setDescription(String description)
    {
    }

    public void dispose()
    {
    }

    public void addDisposeListener(DisposeListener listener)
    {
    }

    public void removeDisposeListener(DisposeListener listener)
    {
    }

    public InternalProvider getProvider()
    {
      return null;
    }

    public Object getProviderSpecificData()
    {
      return null;
    }

    public void setProviderSpecificData(Object data)
    {
    }

    public void notifyObjectsChanged(Object[] objects)
    {
    }
  }
}
