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
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.ECPRepositoryManager.Listener;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPRepositoryAware;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser.ExtensionDescriptor;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.spi.core.InternalRepository;

import org.eclipse.core.runtime.IConfigurationElement;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ECPRepositoryManagerImpl extends PropertiesStore<InternalRepository, Listener> implements
    ECPRepositoryManager, ECPProviderRegistry.Listener
{
  public static final ECPRepositoryManagerImpl INSTANCE = new ECPRepositoryManagerImpl();

  private final RepositoryParser extensionParser = new RepositoryParser();

  private ECPRepositoryManagerImpl()
  {
  }

  public InternalRepository getRepository(Object adaptable)
  {
    if (adaptable instanceof ECPRepositoryAware)
    {
      ECPRepositoryAware repositoryAware = (ECPRepositoryAware)adaptable;
      return (InternalRepository)repositoryAware.getRepository();
    }

    return AdapterUtil.adapt(adaptable, InternalRepository.class);
  }

  public InternalRepository getRepository(String name)
  {
    return getElement(name);
  }

  public InternalRepository[] getRepositories()
  {
    return getElements();
  }

  public boolean hasRepositories()
  {
    return hasElements();
  }

  public ECPRepository addRepository(ECPProvider provider, String name, String label, String description,
      ECPProperties properties)
  {
    InternalRepository repository = new ECPRepositoryImpl(provider, name, properties);
    repository.setLabel(label);
    repository.setDescription(description);

    ((InternalProvider)provider).handleLifecycle(repository, LifecycleEvent.CREATE);
    changeElements(null, Collections.singleton(repository));
    return repository;
  }

  public void notifyObjectsChanged(ECPRepository repository, Object[] objects)
  {
    Listener[] listeners = getRegistryListeners();
    if (listeners != null)
    {
      for (Listener listener : listeners)
      {
        try
        {
          listener.objectsChanged(repository, objects);
        }
        catch (Exception ex)
        {
          Activator.log(ex);
        }
      }
    }
  }

  public void providersChanged(ECPProvider[] oldProviders, ECPProvider[] newProviders) throws Exception
  {
    Set<ECPProvider> addedProviders = ECPUtil.getAddedElements(oldProviders, newProviders);
    if (!addedProviders.isEmpty())
    {
      load();
    }
  }

  @Override
  protected InternalRepository loadElement(ObjectInput in) throws IOException
  {
    return new ECPRepositoryImpl(in);
  }

  @Override
  protected InternalRepository[] createElementArray(int size)
  {
    return new InternalRepository[size];
  }

  @Override
  protected Listener[] createListenerArray(int size)
  {
    return new Listener[size];
  }

  @Override
  protected void notifyListener(Listener listener, InternalRepository[] oldElements, InternalRepository[] newElements)
      throws Exception
  {
    listener.repositoriesChanged(oldElements, newElements);
  }

  @Override
  protected void doActivate() throws Exception
  {
    super.doActivate();
    extensionParser.activate();
    ECPProviderRegistry.INSTANCE.addListener(this);
  }

  @Override
  protected void doDeactivate() throws Exception
  {
    ECPProviderRegistry.INSTANCE.removeListener(this);
    extensionParser.deactivate();
    super.doDeactivate();
  }

  /**
   * @author Eike Stepper
   */
  private final class RepositoryParser extends ExtensionParser<InternalRepository>
  {
    private static final String EXTENSION_POINT_NAME = "repositories";

    public RepositoryParser()
    {
      super(ECPRepositoryManagerImpl.this, Activator.PLUGIN_ID, EXTENSION_POINT_NAME);
    }

    @Override
    protected InternalRepository createElement(String name, IConfigurationElement configurationElement)
    {
      RepositoryDescriptor descriptor = new RepositoryDescriptor(name, configurationElement);
      descriptor.setLabel(configurationElement.getDeclaringExtension().getLabel());
      descriptor.setDescription(configurationElement.getAttribute("description"));
      return descriptor;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class RepositoryDescriptor extends ExtensionDescriptor<InternalRepository> implements
      InternalRepository
  {
    private ECPProperties properties = ECPUtil.createProperties();

    public RepositoryDescriptor(String name, IConfigurationElement configurationElement)
    {
      super(ECPRepositoryManagerImpl.this, name, TYPE, configurationElement);
      for (IConfigurationElement property : configurationElement.getChildren("property"))
      {
        String key = property.getAttribute("key");
        String value = property.getAttribute("value");
        properties.addProperty(key, value);
      }
    }

    public boolean isStorable()
    {
      return false;
    }

    public void write(ObjectOutput out) throws IOException
    {
      throw new UnsupportedOperationException();
    }

    public ECPRepository getRepository()
    {
      return this;
    }

    public InternalProvider getProvider()
    {
      String providerName = getConfigurationElement().getAttribute("provider");
      return (InternalProvider)ECPProviderRegistry.INSTANCE.getProvider(providerName);
    }

    public ECPProperties getProperties()
    {
      return properties;
    }

    public Object getProviderSpecificData()
    {
      return getResolvedElement().getProviderSpecificData();
    }

    public void setProviderSpecificData(Object data)
    {
      getResolvedElement().setProviderSpecificData(data);
    }

    public ECPProject[] getOpenProjects()
    {
      return getResolvedElement().getOpenProjects();
    }

    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapterType)
    {
      return getResolvedElement().getAdapter(adapterType);
    }

    public ECPModelContext getContext()
    {
      return this;
    }

    public boolean canDelete()
    {
      return false;
    }

    public void delete()
    {
      throw new UnsupportedOperationException();
    }

    public void notifyObjectsChanged(Object[] objects)
    {
      getResolvedElement().notifyObjectsChanged(objects);
    }

    public String getDefaultCheckoutName()
    {
      return getResolvedElement().getDefaultCheckoutName();
    }

    public ECPProject checkout(String projectName, ECPProperties projectProperties)
    {
      return getResolvedElement().checkout(projectName, projectProperties);
    }

    @Override
    protected InternalRepository resolve() throws Exception
    {
      return new ECPRepositoryImpl(getProvider(), getName(), getProperties());
    }
  }
}
