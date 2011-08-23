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

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.spi.core.InternalProject;

import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentTypeMatcher;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class ProjectResourceWrapper extends ResourceWrapper<InternalProject>
{
  public ProjectResourceWrapper(ECPProject project, IResource resource)
  {
    super((InternalProject)project, resource);
  }

  @Override
  protected Object createChild(InternalProject project, IResource member)
  {
    switch (member.getType())
    {
    case IResource.ROOT:
      return new IWorkspaceRootWrapper(project, (IWorkspaceRoot)member);

    case IResource.PROJECT:
      return new IProjectWrapper(project, (IProject)member);

    case IResource.FOLDER:
      return new IFolderWrapper(project, (IFolder)member);

    case IResource.FILE:
      return new IFileWrapper(project, (IFile)member);

    default:
      return new ProjectResourceWrapper(project, member);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class IContainerWrapper extends ProjectResourceWrapper implements IContainer
  {
    public IContainerWrapper(ECPProject project, IContainer container)
    {
      super(project, container);
    }

    @Override
    public IContainer getDelegate()
    {
      return (IContainer)super.getDelegate();
    }

    public boolean exists(IPath path)
    {
      return getDelegate().exists(path);
    }

    public IResource findMember(String path)
    {
      return getDelegate().findMember(path);
    }

    public IResource findMember(String path, boolean includePhantoms)
    {
      return getDelegate().findMember(path, includePhantoms);
    }

    public IResource findMember(IPath path)
    {
      return getDelegate().findMember(path);
    }

    public IResource findMember(IPath path, boolean includePhantoms)
    {
      return getDelegate().findMember(path, includePhantoms);
    }

    public String getDefaultCharset() throws CoreException
    {
      return getDelegate().getDefaultCharset();
    }

    public String getDefaultCharset(boolean checkImplicit) throws CoreException
    {
      return getDelegate().getDefaultCharset(checkImplicit);
    }

    public IFile getFile(IPath path)
    {
      return getDelegate().getFile(path);
    }

    public IFolder getFolder(IPath path)
    {
      return getDelegate().getFolder(path);
    }

    public IResource[] members() throws CoreException
    {
      return getDelegate().members();
    }

    public IResource[] members(boolean includePhantoms) throws CoreException
    {
      return getDelegate().members(includePhantoms);
    }

    public IResource[] members(int memberFlags) throws CoreException
    {
      return getDelegate().members(memberFlags);
    }

    public IFile[] findDeletedMembersWithHistory(int depth, IProgressMonitor monitor) throws CoreException
    {
      return getDelegate().findDeletedMembersWithHistory(depth, monitor);
    }

    @Deprecated
    public void setDefaultCharset(String charset) throws CoreException
    {
      getDelegate().setDefaultCharset(charset);
    }

    public void setDefaultCharset(String charset, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().setDefaultCharset(charset, monitor);
    }

    public IResourceFilterDescription createFilter(int type, FileInfoMatcherDescription matcherDescription,
        int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      return getDelegate().createFilter(type, matcherDescription, updateFlags, monitor);
    }

    public IResourceFilterDescription[] getFilters() throws CoreException
    {
      return getDelegate().getFilters();
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class IWorkspaceRootWrapper extends IContainerWrapper implements IWorkspaceRoot
  {
    public IWorkspaceRootWrapper(ECPProject project, IWorkspaceRoot workspaceRoot)
    {
      super(project, workspaceRoot);
    }

    @Override
    public IWorkspaceRoot getDelegate()
    {
      return (IWorkspaceRoot)super.getDelegate();
    }

    public void delete(boolean deleteContent, boolean force, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().delete(deleteContent, force, monitor);
    }

    @Deprecated
    public IContainer[] findContainersForLocation(IPath location)
    {
      return getDelegate().findContainersForLocation(location);
    }

    public IContainer[] findContainersForLocationURI(URI location)
    {
      return getDelegate().findContainersForLocationURI(location);
    }

    public IContainer[] findContainersForLocationURI(URI location, int memberFlags)
    {
      return getDelegate().findContainersForLocationURI(location, memberFlags);
    }

    @Deprecated
    public IFile[] findFilesForLocation(IPath location)
    {
      return getDelegate().findFilesForLocation(location);
    }

    public IFile[] findFilesForLocationURI(URI location)
    {
      return getDelegate().findFilesForLocationURI(location);
    }

    public IFile[] findFilesForLocationURI(URI location, int memberFlags)
    {
      return getDelegate().findFilesForLocationURI(location, memberFlags);
    }

    public IContainer getContainerForLocation(IPath location)
    {
      return getDelegate().getContainerForLocation(location);
    }

    public IFile getFileForLocation(IPath location)
    {
      return getDelegate().getFileForLocation(location);
    }

    public IProject getProject(String name)
    {
      return getDelegate().getProject(name);
    }

    public IProject[] getProjects()
    {
      return getDelegate().getProjects();
    }

    public IProject[] getProjects(int memberFlags)
    {
      return getDelegate().getProjects(memberFlags);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class IProjectWrapper extends IContainerWrapper implements IProject
  {
    public IProjectWrapper(ECPProject project, IProject iProject)
    {
      super(project, iProject);
    }

    @Override
    public IProject getDelegate()
    {
      return (IProject)super.getDelegate();
    }

    public void build(int kind, String builderName, Map<String, String> args, IProgressMonitor monitor)
        throws CoreException
    {
      getDelegate().build(kind, builderName, args, monitor);
    }

    public void build(int kind, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().build(kind, monitor);
    }

    public void build(IBuildConfiguration config, int kind, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().build(config, kind, monitor);
    }

    public void close(IProgressMonitor monitor) throws CoreException
    {
      getDelegate().close(monitor);
    }

    public void create(IProjectDescription description, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().create(description, monitor);
    }

    public void create(IProgressMonitor monitor) throws CoreException
    {
      getDelegate().create(monitor);
    }

    public void create(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().create(description, updateFlags, monitor);
    }

    public void delete(boolean deleteContent, boolean force, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().delete(deleteContent, force, monitor);
    }

    public IBuildConfiguration getActiveBuildConfig() throws CoreException
    {
      return getDelegate().getActiveBuildConfig();
    }

    public IBuildConfiguration getBuildConfig(String configName) throws CoreException
    {
      return getDelegate().getBuildConfig(configName);
    }

    public IBuildConfiguration[] getBuildConfigs() throws CoreException
    {
      return getDelegate().getBuildConfigs();
    }

    public IContentTypeMatcher getContentTypeMatcher() throws CoreException
    {
      return getDelegate().getContentTypeMatcher();
    }

    public IProjectDescription getDescription() throws CoreException
    {
      return getDelegate().getDescription();
    }

    public IFile getFile(String name)
    {
      return getDelegate().getFile(name);
    }

    public IFolder getFolder(String name)
    {
      return getDelegate().getFolder(name);
    }

    public IProjectNature getNature(String natureId) throws CoreException
    {
      return getDelegate().getNature(natureId);
    }

    @Deprecated
    public IPath getPluginWorkingLocation(org.eclipse.core.runtime.IPluginDescriptor plugin)
    {
      return getDelegate().getPluginWorkingLocation(plugin);
    }

    public IPath getWorkingLocation(String id)
    {
      return getDelegate().getWorkingLocation(id);
    }

    public IProject[] getReferencedProjects() throws CoreException
    {
      return getDelegate().getReferencedProjects();
    }

    public IProject[] getReferencingProjects()
    {
      return getDelegate().getReferencingProjects();
    }

    public IBuildConfiguration[] getReferencedBuildConfigs(String configName, boolean includeMissing)
        throws CoreException
    {
      return getDelegate().getReferencedBuildConfigs(configName, includeMissing);
    }

    public boolean hasBuildConfig(String configName) throws CoreException
    {
      return getDelegate().hasBuildConfig(configName);
    }

    public boolean hasNature(String natureId) throws CoreException
    {
      return getDelegate().hasNature(natureId);
    }

    public boolean isNatureEnabled(String natureId) throws CoreException
    {
      return getDelegate().isNatureEnabled(natureId);
    }

    public boolean isOpen()
    {
      return getDelegate().isOpen();
    }

    public void loadSnapshot(int options, URI snapshotLocation, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().loadSnapshot(options, snapshotLocation, monitor);
    }

    public void move(IProjectDescription description, boolean force, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().move(description, force, monitor);
    }

    public void open(int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().open(updateFlags, monitor);
    }

    public void open(IProgressMonitor monitor) throws CoreException
    {
      getDelegate().open(monitor);
    }

    public void saveSnapshot(int options, URI snapshotLocation, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().saveSnapshot(options, snapshotLocation, monitor);
    }

    public void setDescription(IProjectDescription description, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().setDescription(description, monitor);
    }

    public void setDescription(IProjectDescription description, int updateFlags, IProgressMonitor monitor)
        throws CoreException
    {
      getDelegate().setDescription(description, updateFlags, monitor);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class IFolderWrapper extends IContainerWrapper implements IFolder
  {
    public IFolderWrapper(ECPProject project, IFolder folder)
    {
      super(project, folder);
    }

    @Override
    public IFolder getDelegate()
    {
      return (IFolder)super.getDelegate();
    }

    public void create(boolean force, boolean local, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().create(force, local, monitor);
    }

    public void create(int updateFlags, boolean local, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().create(updateFlags, local, monitor);
    }

    public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().createLink(localLocation, updateFlags, monitor);
    }

    public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().createLink(location, updateFlags, monitor);
    }

    public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().delete(force, keepHistory, monitor);
    }

    public IFile getFile(String name)
    {
      return getDelegate().getFile(name);
    }

    public IFolder getFolder(String name)
    {
      return getDelegate().getFolder(name);
    }

    public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor)
        throws CoreException
    {
      getDelegate().move(destination, force, keepHistory, monitor);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class IFileWrapper extends ProjectResourceWrapper implements IFile
  {
    public IFileWrapper(ECPProject project, IFile file)
    {
      super(project, file);
    }

    @Override
    public IFile getDelegate()
    {
      return (IFile)super.getDelegate();
    }

    public void appendContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor)
        throws CoreException
    {
      getDelegate().appendContents(source, force, keepHistory, monitor);
    }

    public void appendContents(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().appendContents(source, updateFlags, monitor);
    }

    public void create(InputStream source, boolean force, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().create(source, force, monitor);
    }

    public void create(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().create(source, updateFlags, monitor);
    }

    public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().createLink(localLocation, updateFlags, monitor);
    }

    public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().createLink(location, updateFlags, monitor);
    }

    public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().delete(force, keepHistory, monitor);
    }

    public String getCharset() throws CoreException
    {
      return getDelegate().getCharset();
    }

    public String getCharset(boolean checkImplicit) throws CoreException
    {
      return getDelegate().getCharset(checkImplicit);
    }

    public String getCharsetFor(Reader reader) throws CoreException
    {
      return getDelegate().getCharsetFor(reader);
    }

    public IContentDescription getContentDescription() throws CoreException
    {
      return getDelegate().getContentDescription();
    }

    public InputStream getContents() throws CoreException
    {
      return getDelegate().getContents();
    }

    public InputStream getContents(boolean force) throws CoreException
    {
      return getDelegate().getContents(force);
    }

    @Deprecated
    public int getEncoding() throws CoreException
    {
      return getDelegate().getEncoding();
    }

    public IFileState[] getHistory(IProgressMonitor monitor) throws CoreException
    {
      return getDelegate().getHistory(monitor);
    }

    public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor)
        throws CoreException
    {
      getDelegate().move(destination, force, keepHistory, monitor);
    }

    @Deprecated
    public void setCharset(String newCharset) throws CoreException
    {
      getDelegate().setCharset(newCharset);
    }

    public void setCharset(String newCharset, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().setCharset(newCharset, monitor);
    }

    public void setContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor)
        throws CoreException
    {
      getDelegate().setContents(source, force, keepHistory, monitor);
    }

    public void setContents(IFileState source, boolean force, boolean keepHistory, IProgressMonitor monitor)
        throws CoreException
    {
      getDelegate().setContents(source, force, keepHistory, monitor);
    }

    public void setContents(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().setContents(source, updateFlags, monitor);
    }

    public void setContents(IFileState source, int updateFlags, IProgressMonitor monitor) throws CoreException
    {
      getDelegate().setContents(source, updateFlags, monitor);
    }
  }
}
