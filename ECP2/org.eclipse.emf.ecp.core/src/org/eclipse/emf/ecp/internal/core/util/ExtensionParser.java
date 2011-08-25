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
package org.eclipse.emf.ecp.internal.core.util;

import org.eclipse.net4j.util.lifecycle.Lifecycle;

import org.eclipse.emf.ecp.core.util.ECPDisposable;
import org.eclipse.emf.ecp.core.util.ECPRegistryElement;
import org.eclipse.emf.ecp.spi.core.util.InternalRegistryElement;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;

import org.osgi.framework.Bundle;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public abstract class ExtensionParser<ELEMENT extends ECPRegistryElement> extends Lifecycle implements
    IRegistryChangeListener
{
  private final ElementRegistry<ELEMENT, ?> elementRegistry;

  private final String namespace;

  private final String name;

  public ExtensionParser(ElementRegistry<ELEMENT, ?> elementRegistry, String namespace, String name)
  {
    this.elementRegistry = elementRegistry;
    this.namespace = namespace;
    this.name = name;
  }

  public final ElementRegistry<ELEMENT, ?> getElementRegistry()
  {
    return elementRegistry;
  }

  public final String getNamespace()
  {
    return namespace;
  }

  public final String getName()
  {
    return name;
  }

  public void registryChanged(IRegistryChangeEvent event)
  {
    Set<String> remove = new HashSet<String>();
    Set<ELEMENT> add = new HashSet<ELEMENT>();
    for (IExtensionDelta delta : event.getExtensionDeltas(namespace, name))
    {
      IExtension extension = delta.getExtension();
      int kind = delta.getKind();
      switch (kind)
      {
      case IExtensionDelta.ADDED:
        addExtension(extension, add);
        break;

      case IExtensionDelta.REMOVED:
        removeExtension(extension, remove);
        break;
      }
    }

    Set<ELEMENT> removedElements = elementRegistry.doChangeElements(remove, add);
    if (removedElements != null)
    {
      for (ELEMENT removedElement : removedElements)
      {
        if (removedElement instanceof ECPDisposable)
        {
          ECPDisposable disposable = (ECPDisposable)removedElement;
          disposable.dispose();
        }
      }
    }
  }

  @Override
  protected void doActivate() throws Exception
  {
    super.doActivate();

    String extensionPointID = namespace + "." + name;
    Set<ELEMENT> add = new HashSet<ELEMENT>();

    IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(extensionPointID);
    for (IExtension extension : extensionPoint.getExtensions())
    {
      addExtension(extension, add);
    }

    elementRegistry.doChangeElements(null, add);
    Platform.getExtensionRegistry().addRegistryChangeListener(this, namespace);
  }

  @Override
  protected void doDeactivate() throws Exception
  {
    Platform.getExtensionRegistry().removeRegistryChangeListener(this);
    super.doDeactivate();
  }

  protected void addExtension(IExtension extension, Set<ELEMENT> result)
  {
    String name = extension.getUniqueIdentifier();
    IConfigurationElement configurationElement = extension.getConfigurationElements()[0];

    ELEMENT element = createElement(name, configurationElement);
    ((InternalRegistryElement)element).setLabel(extension.getLabel());
    ((InternalRegistryElement)element).setDescription(configurationElement.getAttribute("description"));
    result.add(element);
  }

  protected void removeExtension(IExtension extension, Set<String> result)
  {
    String name = extension.getUniqueIdentifier();
    result.add(name);
  }

  protected abstract ELEMENT createElement(String name, IConfigurationElement configurationElement);

  /**
   * @author Eike Stepper
   */
  public static class ExtensionDescriptor<ELEMENT extends ECPRegistryElement> extends ElementDescriptor<ELEMENT>
  {
    private final String type;

    private final IConfigurationElement configurationElement;

    public ExtensionDescriptor(ElementRegistry<ELEMENT, ?> registry, String name, String type,
        IConfigurationElement configurationElement)
    {
      super(registry, name);
      this.type = type;
      this.configurationElement = configurationElement;

      try
      {
        String bundleName = configurationElement.getContributor().getName();
        Bundle bundle = Platform.getBundle(bundleName);
        String location = bundle.getLocation();

        if (location.startsWith("initial@"))
        {
          location = location.substring("initial@".length());
        }

        String prefix = "reference:file:";
        if (location.startsWith(prefix))
        {
          location = location.substring(prefix.length());

          // TODO Trace properly
          System.out.println(getClass().getSimpleName() + ": " + bundleName + " [" + bundle.getBundleId()
              + "] --> file:" + new File(location).getCanonicalPath());
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }

    public String getType()
    {
      return type;
    }

    public final IConfigurationElement getConfigurationElement()
    {
      return configurationElement;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ELEMENT resolve() throws Exception
    {
      return (ELEMENT)configurationElement.createExecutableExtension(getClassAttributeName());
    }

    protected String getClassAttributeName()
    {
      return "class";
    }
  }
}
