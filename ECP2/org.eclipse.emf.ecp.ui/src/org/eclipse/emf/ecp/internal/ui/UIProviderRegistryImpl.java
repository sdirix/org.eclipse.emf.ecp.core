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
package org.eclipse.emf.ecp.internal.ui;

import org.eclipse.net4j.util.AdapterUtil;

import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPProviderAware;
import org.eclipse.emf.ecp.internal.core.util.ElementRegistry;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser.ExtensionDescriptor;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.ui.DefaultUIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProvider;
import org.eclipse.emf.ecp.spi.ui.UIProviderRegistry;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * @author Eike Stepper
 */
public final class UIProviderRegistryImpl extends ElementRegistry<UIProvider, Object> implements UIProviderRegistry
{
  public static final UIProviderRegistryImpl INSTANCE = new UIProviderRegistryImpl();

  private final UIProviderParser extensionParser = new UIProviderParser();

  private UIProviderRegistryImpl()
  {
  }

  public UIProvider getUIProvider(Object adaptable)
  {
    if (adaptable instanceof ECPProviderAware)
    {
      return getUIProvider(((ECPProviderAware)adaptable).getProvider());
    }

    return AdapterUtil.adapt(adaptable, UIProvider.class);
  }

  public synchronized UIProvider getUIProvider(ECPProvider provider)
  {
    UIProvider uiProvider = (UIProvider)((InternalProvider)provider).getUIProvider();
    if (uiProvider != null)
    {
      return uiProvider;
    }

    for (UIProvider ui : getUIProviders())
    {
      if (ui.getProvider() == provider)
      {
        uiProvider = ui;
        break;
      }
    }

    if (uiProvider == null)
    {
      uiProvider = new DefaultUIProvider(provider.getName() + ".default");
    }

    ((InternalProvider)provider).setUIProvider(uiProvider);
    return uiProvider;
  }

  public UIProvider getUIProvider(String name)
  {
    return getElement(name);
  }

  public UIProvider[] getUIProviders()
  {
    return getElements();
  }

  public boolean hasUIProviders()
  {
    return hasElements();
  }

  public String getText(ECPModelContext context, Object adaptable)
  {
    UIProvider uiProvider = getUIProvider(adaptable);
    if (uiProvider == null)
    {
      return null;
    }

    return uiProvider.getText(adaptable);
  }

  public Image getImage(ECPModelContext context, Object adaptable)
  {
    UIProvider uiProvider = getUIProvider(adaptable);
    if (uiProvider == null)
    {
      return null;
    }

    return uiProvider.getImage(adaptable);
  }

  @Override
  protected UIProvider[] createElementArray(int size)
  {
    return new UIProvider[size];
  }

  @Override
  protected Object[] createListenerArray(int size)
  {
    return new Object[size];
  }

  @Override
  protected void notifyListener(Object listener, UIProvider[] oldElements, UIProvider[] newElements) throws Exception
  {
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
  private final class UIProviderParser extends ExtensionParser<UIProvider>
  {
    private static final String EXTENSION_POINT_NAME = "uiProviders";

    public UIProviderParser()
    {
      super(UIProviderRegistryImpl.this, Activator.PLUGIN_ID, EXTENSION_POINT_NAME);
    }

    @Override
    protected UIProvider createElement(String name, IConfigurationElement configurationElement)
    {
      UIProviderDescriptor descriptor = new UIProviderDescriptor(name, configurationElement);
      descriptor.setLabel(configurationElement.getDeclaringExtension().getLabel());
      descriptor.setDescription(configurationElement.getAttribute("description"));
      return descriptor;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class UIProviderDescriptor extends ExtensionDescriptor<UIProvider> implements UIProvider
  {
    public UIProviderDescriptor(String name, IConfigurationElement configurationElement)
    {
      super(UIProviderRegistryImpl.this, name, configurationElement);
    }

    public InternalProvider getProvider()
    {
      String providerName = getConfigurationElement().getAttribute("provider");
      return (InternalProvider)ECPProviderRegistry.INSTANCE.getProvider(providerName);
    }

    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter)
    {
      return getResolvedElement().getAdapter(adapter);
    }

    public String getText(Object element)
    {
      return getResolvedElement().getText(element);
    }

    public Image getImage(Object element)
    {
      return getResolvedElement().getImage(element);
    }

    public void fillContextMenu(IMenuManager manager, ECPModelContext context, Object[] elements)
    {
      getResolvedElement().fillContextMenu(manager, context, elements);
    }

    public Control createAddRepositoryUI(Composite parent, ECPProperties repositoryProperties, Text repositoryNameText,
        Text repositoryLabelText, Text repositoryDescriptionText)
    {
      return getResolvedElement().createAddRepositoryUI(parent, repositoryProperties, repositoryNameText,
          repositoryLabelText, repositoryDescriptionText);
    }

    public Control createCheckoutUI(Composite parent, ECPCheckoutSource checkoutSource, ECPProperties projectProperties)
    {
      return getResolvedElement().createCheckoutUI(parent, checkoutSource, projectProperties);
    }
  }
}
