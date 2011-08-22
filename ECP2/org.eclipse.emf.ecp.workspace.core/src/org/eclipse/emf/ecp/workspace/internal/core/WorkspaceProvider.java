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
package org.eclipse.emf.ecp.workspace.internal.core;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.spi.core.DefaultProvider;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class WorkspaceProvider extends DefaultProvider implements IResourceChangeListener
{
  public static final String NAME = "org.eclipse.emf.ecp.workspace.provider";

  public static final String PROP_ROOT_URI = "rootURI";

  private static final IWorkspace WORKSPACE = ResourcesPlugin.getWorkspace();

  private static final IWorkspaceRoot WORKSPACE_ROOT = WORKSPACE.getRoot();

  static WorkspaceProvider INSTANCE;

  public WorkspaceProvider()
  {
    super(NAME);
    INSTANCE = this;
    WORKSPACE.addResourceChangeListener(this);
  }

  @Override
  protected void doDispose()
  {
    try
    {
      WORKSPACE.removeResourceChangeListener(this);
      super.doDispose();
    }
    finally
    {
      INSTANCE = null;
    }
  }

  @Override
  public boolean canAddRepositories()
  {
    return false;
  }

  @Override
  public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList)
  {
    if (parent instanceof ECPRepository)
    {
      ECPRepository repository = (ECPRepository)parent;
      RepositoryResourceWrapper wrapper = new RepositoryResourceWrapper(repository, WORKSPACE_ROOT);
      wrapper.fillChildren(childrenList);
    }
    else if (parent instanceof ECPProject)
    {
      ECPProject project = (ECPProject)parent;
      String rootURI = project.getProperties().getValue(PROP_ROOT_URI);
      if (rootURI == null)
      {
        ProjectResourceWrapper wrapper = new ProjectResourceWrapper(project, WORKSPACE_ROOT);
        wrapper.fillChildren(childrenList);
      }
      else
      {
        ResourceSet resourceSet = project.getEditingDomain().getResourceSet();

        URI uri = URI.createURI(rootURI);
        if (uri.hasFragment())
        {
          EObject eObject = resourceSet.getEObject(uri, true);
          super.fillChildren(context, eObject, childrenList);
        }
        else
        {
          Resource resource = resourceSet.getResource(uri, true);
          childrenList.addChildren(resource.getContents());

          // String path = uri.toPlatformString(true);
          // IResource member = WORKSPACE_ROOT.findMember(path);
          // ProjectResourceWrapper wrapper = new ProjectResourceWrapper(project, member);
          // wrapper.fillChildren(childrenList);
        }
      }
    }
    else if (parent instanceof ResourceWrapper)
    {
      ResourceWrapper<?> wrapper = (ResourceWrapper<?>)parent;
      wrapper.fillChildren(childrenList);
    }
    else
    {
      super.fillChildren(context, parent, childrenList);
    }
  }

  public void resourceChanged(IResourceChangeEvent event)
  {
    IResourceDelta delta = event.getDelta();
    if (delta != null)
    {
      InternalRepository repository = getAllRepositories()[0];
      Object[] objects = getChangedObjects(delta, repository);
      repository.notifyObjectsChanged(objects);
    }
  }

  private Object[] getChangedObjects(IResourceDelta delta, InternalRepository repository)
  {
    Set<Object> objects = new HashSet<Object>();
    getChangedObjects(delta, repository, objects);
    return objects.toArray(new Object[objects.size()]);
  }

  private void getChangedObjects(IResourceDelta delta, InternalRepository repository, Set<Object> objects)
  {
    switch (delta.getKind())
    {
    case IResourceDelta.ADDED:
    case IResourceDelta.REMOVED:
      IResource resource = delta.getResource();
      if (resource.getType() == IResource.PROJECT)
      {
        objects.add(repository);
      }
      else
      {
        objects.add(new RepositoryResourceWrapper(repository, resource.getParent()));
      }

      return;
    }

    for (IResourceDelta child : delta.getAffectedChildren())
    {
      getChangedObjects(child, repository, objects);
    }
  }
}
