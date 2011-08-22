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
package org.eclipse.emf.ecp.spi.core;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.core.util.Disposable;
import org.eclipse.emf.ecp.internal.core.util.Element;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class DefaultProvider extends Element implements InternalProvider
{
  private final Disposable disposable = new Disposable(this)
  {
    @Override
    protected void doDispose()
    {
      uiProvider = null;

      for (ECPRepository repository : ECPRepositoryManager.INSTANCE.getRepositories())
      {
        if (repository.getProvider().getName().equals(getName()))
        {
          handleLifecycle(repository, LifecycleEvent.DISPOSE);
        }
      }

      for (ECPProject project : ECPProjectManager.INSTANCE.getProjects())
      {
        if (project.getProvider().getName().equals(getName()))
        {
          handleLifecycle(project, LifecycleEvent.DISPOSE);
        }
      }

      DefaultProvider.this.doDispose();
    }
  };

  private String label;

  private String description;

  private Object uiProvider;

  protected DefaultProvider(String name)
  {
    super(name);
    label = name;
    description = "";
  }

  public final InternalProvider getProvider()
  {
    return this;
  }

  public final String getLabel()
  {
    return label;
  }

  public final void setLabel(String label)
  {
    this.label = label;
  }

  public final String getDescription()
  {
    return description;
  }

  public final void setDescription(String description)
  {
    this.description = description;
  }

  public final Object getUIProvider()
  {
    return uiProvider;
  }

  public final void setUIProvider(Object uiProvider)
  {
    this.uiProvider = uiProvider;
  }

  public final InternalRepository[] getAllRepositories()
  {
    List<InternalRepository> result = new ArrayList<InternalRepository>();
    for (ECPRepository repository : ECPRepositoryManager.INSTANCE.getRepositories())
    {
      if (!ECPUtil.isDisposed(repository))
      {
        ECPProvider provider = repository.getProvider();
        if (provider.equals(this))
        {
          result.add((InternalRepository)repository);
        }
      }
    }

    // TODO Consider to cache the result
    return result.toArray(new InternalRepository[result.size()]);
  }

  public final InternalProject[] getOpenProjects()
  {
    List<InternalProject> result = new ArrayList<InternalProject>();
    for (ECPProject project : ECPProjectManager.INSTANCE.getProjects())
    {
      if (project.isOpen())
      {
        ECPRepository repository = project.getRepository();
        if (!ECPUtil.isDisposed(repository))
        {
          if (repository.getProvider().equals(this))
          {
            result.add((InternalProject)project);
          }
        }
      }
    }

    // TODO Consider to cache the result
    return result.toArray(new InternalProject[result.size()]);
  }

  public final boolean isDisposed()
  {
    return disposable.isDisposed();
  }

  public final void dispose()
  {
    disposable.dispose();
  }

  public final void addDisposeListener(DisposeListener listener)
  {
    disposable.addDisposeListener(listener);
  }

  public final void removeDisposeListener(DisposeListener listener)
  {
    disposable.removeDisposeListener(listener);
  }

  protected void doDispose()
  {
  }

  public EditingDomain createEditingDomain(InternalProject project)
  {
    CommandStack commandStack = createCommandStack(project);
    return new AdapterFactoryEditingDomain(InternalProvider.EMF_ADAPTER_FACTORY, commandStack);
  }

  protected CommandStack createCommandStack(InternalProject project)
  {
    return new BasicCommandStack();
  }

  public boolean canAddRepositories()
  {
    return true;
  }

  public boolean isSlow(Object parent)
  {
    return false;
  }

  public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList)
  {
    if (parent == ECPProjectManager.INSTANCE)
    {
      childrenList.addChildren(ECPProjectManager.INSTANCE.getProjects());
    }
    else if (parent == ECPRepositoryManager.INSTANCE)
    {
      childrenList.addChildren(ECPRepositoryManager.INSTANCE.getRepositories());
    }
    else
    {
      // Get the adapter from the factory.
      ITreeItemContentProvider treeItemContentProvider = (ITreeItemContentProvider)InternalProvider.EMF_ADAPTER_FACTORY
          .adapt(parent, ITreeItemContentProvider.class);

      // Either delegate the call or return nothing.
      if (treeItemContentProvider != null)
      {
        Collection<?> children = treeItemContentProvider.getChildren(parent);
        childrenList.addChildren(children);
      }
    }
  }

  public void handleLifecycle(ECPModelContext context, LifecycleEvent event)
  {
    // TODO Trace properly
    String providerClass = getClass().getSimpleName();
    String contextClass = context.getClass().getSimpleName();
    System.out.println(providerClass + " received " + event + " for " + contextClass + " " + context);
  }
}
