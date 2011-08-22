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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.ecp.spi.core.util.ModelWrapper;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * @author Eike Stepper
 */
public abstract class ResourceWrapper<CONTEXT extends ECPModelContext> extends ModelWrapper<CONTEXT, IResource>
{
  public ResourceWrapper(CONTEXT context, IResource delegate)
  {
    super(context, delegate);
  }

  public final URI getURI()
  {
    return URI.createPlatformResourceURI(getDelegate().getFullPath().toPortableString(), true);
  }

  @Override
  public String getName()
  {
    return getDelegate().getName();
  }

  public void fillChildren(InternalChildrenList childrenList)
  {
    IResource parent = getDelegate();
    if (parent instanceof IFile)
    {
      fillFile(childrenList, (IFile)parent);
    }
    else if (parent instanceof IContainer)
    {
      fillContainer(childrenList, (IContainer)parent);
    }
  }

  protected void fillFile(InternalChildrenList childrenList, IFile parent)
  {
    try
    {
      ResourceSet resourceSet = new ResourceSetImpl();
      Resource emfResource = resourceSet.getResource(getURI(), true);
      childrenList.addChildren(emfResource.getContents());
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }
  }

  protected void fillContainer(InternalChildrenList childrenList, IContainer container)
  {
    try
    {
      IResource[] members = container.members();
      for (int i = 0; i < members.length; i++)
      {
        IResource member = members[i];
        if (member.getType() == IResource.FILE)
        {
          try
          {
            URI uri = URI.createPlatformResourceURI(member.getFullPath().toPortableString(), true);
            ResourceSet resourceSet = new ResourceSetImpl();
            Resource emfResource = resourceSet.getResource(uri, true);
            emfResource.getContents().isEmpty(); // Ensure that resource can be loaded
            childrenList.addChild(emfResource);
          }
          catch (Exception ex)
          {
            //$FALL-THROUGH$
          }
        }
        else
        {
          Object child = createChild(getContext(), member);
          if (child != null)
          {
            childrenList.addChild(child);
          }
        }
      }
    }
    catch (CoreException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  protected abstract Object createChild(CONTEXT context, IResource member);
}
