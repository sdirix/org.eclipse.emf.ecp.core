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
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import java.util.Map;

/**
 * @author Eike Stepper
 */
public abstract class ResourceWrapper<CONTEXT extends ECPModelContext> extends ModelWrapper<CONTEXT, IResource>
    implements IResource
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

  public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter)
  {
    return getDelegate().getAdapter(adapter);
  }

  public boolean contains(ISchedulingRule rule)
  {
    return getDelegate().contains(rule);
  }

  public boolean isConflicting(ISchedulingRule rule)
  {
    return getDelegate().isConflicting(rule);
  }

  public void accept(IResourceProxyVisitor visitor, int memberFlags) throws CoreException
  {
    getDelegate().accept(visitor, memberFlags);
  }

  public void accept(IResourceVisitor visitor) throws CoreException
  {
    getDelegate().accept(visitor);
  }

  public void accept(IResourceVisitor visitor, int depth, boolean includePhantoms) throws CoreException
  {
    getDelegate().accept(visitor, depth, includePhantoms);
  }

  public void accept(IResourceVisitor visitor, int depth, int memberFlags) throws CoreException
  {
    getDelegate().accept(visitor, depth, memberFlags);
  }

  public void clearHistory(IProgressMonitor monitor) throws CoreException
  {
    getDelegate().clearHistory(monitor);
  }

  public void copy(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().copy(destination, force, monitor);
  }

  public void copy(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().copy(destination, updateFlags, monitor);
  }

  public void copy(IProjectDescription description, boolean force, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().copy(description, force, monitor);
  }

  public void copy(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().copy(description, updateFlags, monitor);
  }

  public IMarker createMarker(String type) throws CoreException
  {
    return getDelegate().createMarker(type);
  }

  public IResourceProxy createProxy()
  {
    return getDelegate().createProxy();
  }

  public void delete(boolean force, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().delete(force, monitor);
  }

  public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().delete(updateFlags, monitor);
  }

  public void deleteMarkers(String type, boolean includeSubtypes, int depth) throws CoreException
  {
    getDelegate().deleteMarkers(type, includeSubtypes, depth);
  }

  public boolean exists()
  {
    return getDelegate().exists();
  }

  public IMarker findMarker(long id) throws CoreException
  {
    return getDelegate().findMarker(id);
  }

  public IMarker[] findMarkers(String type, boolean includeSubtypes, int depth) throws CoreException
  {
    return getDelegate().findMarkers(type, includeSubtypes, depth);
  }

  public int findMaxProblemSeverity(String type, boolean includeSubtypes, int depth) throws CoreException
  {
    return getDelegate().findMaxProblemSeverity(type, includeSubtypes, depth);
  }

  public String getFileExtension()
  {
    return getDelegate().getFileExtension();
  }

  public IPath getFullPath()
  {
    return getDelegate().getFullPath();
  }

  public long getLocalTimeStamp()
  {
    return getDelegate().getLocalTimeStamp();
  }

  public IPath getLocation()
  {
    return getDelegate().getLocation();
  }

  public java.net.URI getLocationURI()
  {
    return getDelegate().getLocationURI();
  }

  public IMarker getMarker(long id)
  {
    return getDelegate().getMarker(id);
  }

  public long getModificationStamp()
  {
    return getDelegate().getModificationStamp();
  }

  public IPathVariableManager getPathVariableManager()
  {
    return getDelegate().getPathVariableManager();
  }

  public IContainer getParent()
  {
    return getDelegate().getParent();
  }

  public Map<QualifiedName, String> getPersistentProperties() throws CoreException
  {
    return getDelegate().getPersistentProperties();
  }

  public String getPersistentProperty(QualifiedName key) throws CoreException
  {
    return getDelegate().getPersistentProperty(key);
  }

  public IProject getProject()
  {
    return getDelegate().getProject();
  }

  public IPath getProjectRelativePath()
  {
    return getDelegate().getProjectRelativePath();
  }

  public IPath getRawLocation()
  {
    return getDelegate().getRawLocation();
  }

  public java.net.URI getRawLocationURI()
  {
    return getDelegate().getRawLocationURI();
  }

  public ResourceAttributes getResourceAttributes()
  {
    return getDelegate().getResourceAttributes();
  }

  public Map<QualifiedName, Object> getSessionProperties() throws CoreException
  {
    return getDelegate().getSessionProperties();
  }

  public Object getSessionProperty(QualifiedName key) throws CoreException
  {
    return getDelegate().getSessionProperty(key);
  }

  public int getType()
  {
    return getDelegate().getType();
  }

  public IWorkspace getWorkspace()
  {
    return getDelegate().getWorkspace();
  }

  public boolean isAccessible()
  {
    return getDelegate().isAccessible();
  }

  public boolean isDerived()
  {
    return getDelegate().isDerived();
  }

  public boolean isDerived(int options)
  {
    return getDelegate().isDerived(options);
  }

  public boolean isHidden()
  {
    return getDelegate().isHidden();
  }

  public boolean isHidden(int options)
  {
    return getDelegate().isHidden(options);
  }

  public boolean isLinked()
  {
    return getDelegate().isLinked();
  }

  public boolean isVirtual()
  {
    return getDelegate().isVirtual();
  }

  public boolean isLinked(int options)
  {
    return getDelegate().isLinked(options);
  }

  @Deprecated
  public boolean isLocal(int depth)
  {
    return getDelegate().isLocal(depth);
  }

  public boolean isPhantom()
  {
    return getDelegate().isPhantom();
  }

  @Deprecated
  public boolean isReadOnly()
  {
    return getDelegate().isReadOnly();
  }

  public boolean isSynchronized(int depth)
  {
    return getDelegate().isSynchronized(depth);
  }

  public boolean isTeamPrivateMember()
  {
    return getDelegate().isTeamPrivateMember();
  }

  public boolean isTeamPrivateMember(int options)
  {
    return getDelegate().isTeamPrivateMember(options);
  }

  public void move(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().move(destination, force, monitor);
  }

  public void move(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().move(destination, updateFlags, monitor);
  }

  public void move(IProjectDescription description, boolean force, boolean keepHistory, IProgressMonitor monitor)
      throws CoreException
  {
    getDelegate().move(description, force, keepHistory, monitor);
  }

  public void move(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().move(description, updateFlags, monitor);
  }

  public void refreshLocal(int depth, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().refreshLocal(depth, monitor);
  }

  public void revertModificationStamp(long value) throws CoreException
  {
    getDelegate().revertModificationStamp(value);
  }

  @Deprecated
  public void setDerived(boolean isDerived) throws CoreException
  {
    getDelegate().setDerived(isDerived);
  }

  public void setDerived(boolean isDerived, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().setDerived(isDerived, monitor);
  }

  public void setHidden(boolean isHidden) throws CoreException
  {
    getDelegate().setHidden(isHidden);
  }

  @Deprecated
  public void setLocal(boolean flag, int depth, IProgressMonitor monitor) throws CoreException
  {
    getDelegate().setLocal(flag, depth, monitor);
  }

  public long setLocalTimeStamp(long value) throws CoreException
  {
    return getDelegate().setLocalTimeStamp(value);
  }

  public void setPersistentProperty(QualifiedName key, String value) throws CoreException
  {
    getDelegate().setPersistentProperty(key, value);
  }

  @Deprecated
  public void setReadOnly(boolean readOnly)
  {
    getDelegate().setReadOnly(readOnly);
  }

  public void setResourceAttributes(ResourceAttributes attributes) throws CoreException
  {
    getDelegate().setResourceAttributes(attributes);
  }

  public void setSessionProperty(QualifiedName key, Object value) throws CoreException
  {
    getDelegate().setSessionProperty(key, value);
  }

  public void setTeamPrivateMember(boolean isTeamPrivate) throws CoreException
  {
    getDelegate().setTeamPrivateMember(isTeamPrivate);
  }

  public void touch(IProgressMonitor monitor) throws CoreException
  {
    getDelegate().touch(monitor);
  }
}
