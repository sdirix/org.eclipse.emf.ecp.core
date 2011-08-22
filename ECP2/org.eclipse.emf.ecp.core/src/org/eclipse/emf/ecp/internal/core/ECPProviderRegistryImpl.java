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
package org.eclipse.emf.ecp.internal.core;

import org.eclipse.net4j.util.AdapterUtil;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPProviderRegistry.Listener;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProviderAware;
import org.eclipse.emf.ecp.internal.core.util.ElementRegistry;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser.ExtensionDescriptor;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.runtime.IConfigurationElement;

import java.util.Collections;

/**
 * @author Eike Stepper
 */
public class ECPProviderRegistryImpl extends ElementRegistry<InternalProvider, Listener> implements ECPProviderRegistry
{
  public static final ECPProviderRegistryImpl INSTANCE = new ECPProviderRegistryImpl();

  private final ProviderParser extensionParser = new ProviderParser();

  private ECPProviderRegistryImpl()
  {
  }

  public InternalProvider getProvider(Object adaptable)
  {
    if (adaptable instanceof ECPProviderAware)
    {
      ECPProviderAware providerAware = (ECPProviderAware)adaptable;
      return (InternalProvider)providerAware.getProvider();
    }

    return AdapterUtil.adapt(adaptable, InternalProvider.class);
  }

  public InternalProvider getProvider(String name)
  {
    return getElement(name);
  }

  public InternalProvider[] getProviders()
  {
    return getElements();
  }

  public boolean hasProviders()
  {
    return hasElements();
  }

  public void addProvider(ECPProvider provider)
  {
    changeElements(null, Collections.singleton((InternalProvider)provider));
  }

  public void removeProvider(String name)
  {
    changeElements(Collections.singleton(name), null);
  }

  @Override
  protected InternalProvider[] createElementArray(int size)
  {
    return new InternalProvider[size];
  }

  @Override
  protected Listener[] createListenerArray(int size)
  {
    return new Listener[size];
  }

  @Override
  protected void notifyListener(Listener listener, InternalProvider[] oldElements, InternalProvider[] newElements)
      throws Exception
  {
    listener.providersChanged(oldElements, newElements);
  }

  @Override
  protected void elementsChanged(InternalProvider[] oldElements, InternalProvider[] newElements)
  {
    super.elementsChanged(oldElements, newElements);
  }

  @Override
  protected void doActivate() throws Exception
  {
    super.doActivate();
    extensionParser.activate();
  }

  @Override
  protected void doDeactivate() throws Exception
  {
    extensionParser.deactivate();
    super.doDeactivate();
  }

  /**
   * @author Eike Stepper
   */
  private final class ProviderParser extends ExtensionParser<InternalProvider>
  {
    private static final String EXTENSION_POINT_NAME = "providers";

    public ProviderParser()
    {
      super(ECPProviderRegistryImpl.this, Activator.PLUGIN_ID, EXTENSION_POINT_NAME);
    }

    @Override
    protected InternalProvider createElement(String name, IConfigurationElement configurationElement)
    {
      ProviderDescriptor descriptor = new ProviderDescriptor(name, configurationElement);
      descriptor.setLabel(configurationElement.getDeclaringExtension().getLabel());
      descriptor.setDescription(configurationElement.getAttribute("description"));
      return descriptor;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ProviderDescriptor extends ExtensionDescriptor<InternalProvider> implements InternalProvider
  {
    private Object uiProvider;

    public ProviderDescriptor(String name, IConfigurationElement configurationElement)
    {
      super(ECPProviderRegistryImpl.this, name, configurationElement);
    }

    public ECPProvider getProvider()
    {
      return this;
    }

    public Object getUIProvider()
    {
      return uiProvider;
    }

    public void setUIProvider(Object uiProvider)
    {
      this.uiProvider = uiProvider;
      if (isResolved())
      {
        getResolvedElement().setUIProvider(uiProvider);
      }
    }

    public ECPRepository[] getAllRepositories()
    {
      return getResolvedElement().getAllRepositories();
    }

    public ECPProject[] getOpenProjects()
    {
      return getResolvedElement().getOpenProjects();
    }

    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter)
    {
      return getResolvedElement().getAdapter(adapter);
    }

    public EditingDomain createEditingDomain(InternalProject project)
    {
      return getResolvedElement().createEditingDomain(project);
    }

    public boolean canAddRepositories()
    {
      return getResolvedElement().canAddRepositories();
    }

    public boolean isSlow(Object parent)
    {
      return getResolvedElement().isSlow(parent);
    }

    public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList)
    {
      getResolvedElement().fillChildren(context, parent, childrenList);
    }

    public void handleLifecycle(ECPModelContext context, LifecycleEvent event)
    {
      getResolvedElement().handleLifecycle(context, event);
    }

    @Override
    protected void resolvedElement(InternalProvider provider)
    {
      super.resolvedElement(provider);
      provider.setLabel(getLabel());
      provider.setDescription(getDescription());
      provider.setUIProvider(uiProvider);
    }

    @Override
    protected void doDispose()
    {
      uiProvider = null;
      super.doDispose();
    }
  }
}
