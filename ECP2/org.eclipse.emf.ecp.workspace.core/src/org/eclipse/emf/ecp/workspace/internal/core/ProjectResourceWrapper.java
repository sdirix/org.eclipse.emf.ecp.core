/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
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
public class ProjectResourceWrapper extends ResourceWrapper<InternalProject> {
	public ProjectResourceWrapper(ECPProject project, IResource resource) {
		super((InternalProject) project, resource);
	}

	@Override
	protected Object createChild(InternalProject project, IResource member) {
		switch (member.getType()) {
		case IResource.ROOT:
			return new IWorkspaceRootWrapper(project, (IWorkspaceRoot) member);

		case IResource.PROJECT:
			return new IProjectWrapper(project, (IProject) member);

		case IResource.FOLDER:
			return new IFolderWrapper(project, (IFolder) member);

		case IResource.FILE:
			return new IFileWrapper(project, (IFile) member);

		default:
			return new ProjectResourceWrapper(project, member);
		}
	}

	/**
	 * @author Eike Stepper
	 */
	public static class IContainerWrapper extends ProjectResourceWrapper implements IContainer {
		public IContainerWrapper(ECPProject project, IContainer container) {
			super(project, container);
		}

		@Override
		public IContainer getDelegate() {
			return (IContainer) super.getDelegate();
		}

		/** {@inheritDoc} */
		public boolean exists(IPath path) {
			return getDelegate().exists(path);
		}

		/** {@inheritDoc} */
		public IResource findMember(String path) {
			return getDelegate().findMember(path);
		}

		/** {@inheritDoc} */
		public IResource findMember(String path, boolean includePhantoms) {
			return getDelegate().findMember(path, includePhantoms);
		}

		/** {@inheritDoc} */
		public IResource findMember(IPath path) {
			return getDelegate().findMember(path);
		}

		/** {@inheritDoc} */
		public IResource findMember(IPath path, boolean includePhantoms) {
			return getDelegate().findMember(path, includePhantoms);
		}

		/** {@inheritDoc} */
		public String getDefaultCharset() throws CoreException {
			return getDelegate().getDefaultCharset();
		}

		/** {@inheritDoc} */
		public String getDefaultCharset(boolean checkImplicit) throws CoreException {
			return getDelegate().getDefaultCharset(checkImplicit);
		}

		/** {@inheritDoc} */
		public IFile getFile(IPath path) {
			return getDelegate().getFile(path);
		}

		/** {@inheritDoc} */
		public IFolder getFolder(IPath path) {
			return getDelegate().getFolder(path);
		}

		/** {@inheritDoc} */
		public IResource[] members() throws CoreException {
			return getDelegate().members();
		}

		/** {@inheritDoc} */
		public IResource[] members(boolean includePhantoms) throws CoreException {
			return getDelegate().members(includePhantoms);
		}

		/** {@inheritDoc} */
		public IResource[] members(int memberFlags) throws CoreException {
			return getDelegate().members(memberFlags);
		}

		/** {@inheritDoc} */
		public IFile[] findDeletedMembersWithHistory(int depth, IProgressMonitor monitor) throws CoreException {
			return getDelegate().findDeletedMembersWithHistory(depth, monitor);
		}

		/** {@inheritDoc} */
		@Deprecated
		public void setDefaultCharset(String charset) throws CoreException {
			getDelegate().setDefaultCharset(charset);
		}

		/** {@inheritDoc} */
		public void setDefaultCharset(String charset, IProgressMonitor monitor) throws CoreException {
			getDelegate().setDefaultCharset(charset, monitor);
		}

		/** {@inheritDoc} */
		public IResourceFilterDescription createFilter(int type, FileInfoMatcherDescription matcherDescription,
			int updateFlags, IProgressMonitor monitor) throws CoreException {
			return getDelegate().createFilter(type, matcherDescription, updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public IResourceFilterDescription[] getFilters() throws CoreException {
			return getDelegate().getFilters();
		}
	}

	/**
	 * @author Eike Stepper
	 */
	public static class IWorkspaceRootWrapper extends IContainerWrapper implements IWorkspaceRoot {
		public IWorkspaceRootWrapper(ECPProject project, IWorkspaceRoot workspaceRoot) {
			super(project, workspaceRoot);
		}

		@Override
		public IWorkspaceRoot getDelegate() {
			return (IWorkspaceRoot) super.getDelegate();
		}

		/** {@inheritDoc} */
		public void delete(boolean deleteContent, boolean force, IProgressMonitor monitor) throws CoreException {
			getDelegate().delete(deleteContent, force, monitor);
		}

		/** {@inheritDoc} */
		@Deprecated
		public IContainer[] findContainersForLocation(IPath location) {
			return getDelegate().findContainersForLocation(location);
		}

		/** {@inheritDoc} */
		public IContainer[] findContainersForLocationURI(URI location) {
			return getDelegate().findContainersForLocationURI(location);
		}

		/** {@inheritDoc} */
		public IContainer[] findContainersForLocationURI(URI location, int memberFlags) {
			return getDelegate().findContainersForLocationURI(location, memberFlags);
		}

		/** {@inheritDoc} */
		@Deprecated
		public IFile[] findFilesForLocation(IPath location) {
			return getDelegate().findFilesForLocation(location);
		}

		/** {@inheritDoc} */
		public IFile[] findFilesForLocationURI(URI location) {
			return getDelegate().findFilesForLocationURI(location);
		}

		/** {@inheritDoc} */
		public IFile[] findFilesForLocationURI(URI location, int memberFlags) {
			return getDelegate().findFilesForLocationURI(location, memberFlags);
		}

		/** {@inheritDoc} */
		public IContainer getContainerForLocation(IPath location) {
			return getDelegate().getContainerForLocation(location);
		}

		/** {@inheritDoc} */
		public IFile getFileForLocation(IPath location) {
			return getDelegate().getFileForLocation(location);
		}

		/** {@inheritDoc} */
		public IProject getProject(String name) {
			return getDelegate().getProject(name);
		}

		/** {@inheritDoc} */
		public IProject[] getProjects() {
			return getDelegate().getProjects();
		}

		/** {@inheritDoc} */
		public IProject[] getProjects(int memberFlags) {
			return getDelegate().getProjects(memberFlags);
		}
	}

	/**
	 * @author Eike Stepper
	 */
	public static class IProjectWrapper extends IContainerWrapper implements IProject {
		public IProjectWrapper(ECPProject project, IProject iProject) {
			super(project, iProject);
		}

		@Override
		public IProject getDelegate() {
			return (IProject) super.getDelegate();
		}

		/** {@inheritDoc} */
		public void build(int kind, String builderName, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
			getDelegate().build(kind, builderName, args, monitor);
		}

		/** {@inheritDoc} */
		public void build(int kind, IProgressMonitor monitor) throws CoreException {
			getDelegate().build(kind, monitor);
		}

		/** {@inheritDoc} */
		public void build(IBuildConfiguration config, int kind, IProgressMonitor monitor) throws CoreException {
			getDelegate().build(config, kind, monitor);
		}

		/** {@inheritDoc} */
		public void close(IProgressMonitor monitor) throws CoreException {
			getDelegate().close(monitor);
		}

		/** {@inheritDoc} */
		public void create(IProjectDescription description, IProgressMonitor monitor) throws CoreException {
			getDelegate().create(description, monitor);
		}

		/** {@inheritDoc} */
		public void create(IProgressMonitor monitor) throws CoreException {
			getDelegate().create(monitor);
		}

		/** {@inheritDoc} */
		public void create(IProjectDescription description, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
			getDelegate().create(description, updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public void delete(boolean deleteContent, boolean force, IProgressMonitor monitor) throws CoreException {
			getDelegate().delete(deleteContent, force, monitor);
		}

		/** {@inheritDoc} */
		public IBuildConfiguration getActiveBuildConfig() throws CoreException {
			return getDelegate().getActiveBuildConfig();
		}

		/** {@inheritDoc} */
		public IBuildConfiguration getBuildConfig(String configName) throws CoreException {
			return getDelegate().getBuildConfig(configName);
		}

		/** {@inheritDoc} */
		public IBuildConfiguration[] getBuildConfigs() throws CoreException {
			return getDelegate().getBuildConfigs();
		}

		/** {@inheritDoc} */
		public IContentTypeMatcher getContentTypeMatcher() throws CoreException {
			return getDelegate().getContentTypeMatcher();
		}

		/** {@inheritDoc} */
		public IProjectDescription getDescription() throws CoreException {
			return getDelegate().getDescription();
		}

		/** {@inheritDoc} */
		public IFile getFile(String name) {
			return getDelegate().getFile(name);
		}

		/** {@inheritDoc} */
		public IFolder getFolder(String name) {
			return getDelegate().getFolder(name);
		}

		/** {@inheritDoc} */
		public IProjectNature getNature(String natureId) throws CoreException {
			return getDelegate().getNature(natureId);
		}

		/** {@inheritDoc} */
		@Deprecated
		public IPath getPluginWorkingLocation(org.eclipse.core.runtime.IPluginDescriptor plugin) {
			return getDelegate().getPluginWorkingLocation(plugin);
		}

		/** {@inheritDoc} */
		public IPath getWorkingLocation(String id) {
			return getDelegate().getWorkingLocation(id);
		}

		/** {@inheritDoc} */
		public IProject[] getReferencedProjects() throws CoreException {
			return getDelegate().getReferencedProjects();
		}

		/** {@inheritDoc} */
		public IProject[] getReferencingProjects() {
			return getDelegate().getReferencingProjects();
		}

		/** {@inheritDoc} */
		public IBuildConfiguration[] getReferencedBuildConfigs(String configName, boolean includeMissing)
			throws CoreException {
			return getDelegate().getReferencedBuildConfigs(configName, includeMissing);
		}

		/** {@inheritDoc} */
		public boolean hasBuildConfig(String configName) throws CoreException {
			return getDelegate().hasBuildConfig(configName);
		}

		/** {@inheritDoc} */
		public boolean hasNature(String natureId) throws CoreException {
			return getDelegate().hasNature(natureId);
		}

		/** {@inheritDoc} */
		public boolean isNatureEnabled(String natureId) throws CoreException {
			return getDelegate().isNatureEnabled(natureId);
		}

		/** {@inheritDoc} */
		public boolean isOpen() {
			return getDelegate().isOpen();
		}

		/** {@inheritDoc} */
		public void loadSnapshot(int options, URI snapshotLocation, IProgressMonitor monitor) throws CoreException {
			getDelegate().loadSnapshot(options, snapshotLocation, monitor);
		}

		/** {@inheritDoc} */
		public void move(IProjectDescription description, boolean force, IProgressMonitor monitor) throws CoreException {
			getDelegate().move(description, force, monitor);
		}

		/** {@inheritDoc} */
		public void open(int updateFlags, IProgressMonitor monitor) throws CoreException {
			getDelegate().open(updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public void open(IProgressMonitor monitor) throws CoreException {
			getDelegate().open(monitor);
		}

		/** {@inheritDoc} */
		public void saveSnapshot(int options, URI snapshotLocation, IProgressMonitor monitor) throws CoreException {
			getDelegate().saveSnapshot(options, snapshotLocation, monitor);
		}

		/** {@inheritDoc} */
		public void setDescription(IProjectDescription description, IProgressMonitor monitor) throws CoreException {
			getDelegate().setDescription(description, monitor);
		}

		/** {@inheritDoc} */
		public void setDescription(IProjectDescription description, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
			getDelegate().setDescription(description, updateFlags, monitor);
		}
	}

	/**
	 * @author Eike Stepper
	 */
	public static class IFolderWrapper extends IContainerWrapper implements IFolder {
		public IFolderWrapper(ECPProject project, IFolder folder) {
			super(project, folder);
		}

		@Override
		public IFolder getDelegate() {
			return (IFolder) super.getDelegate();
		}

		/** {@inheritDoc} */
		public void create(boolean force, boolean local, IProgressMonitor monitor) throws CoreException {
			getDelegate().create(force, local, monitor);
		}

		/** {@inheritDoc} */
		public void create(int updateFlags, boolean local, IProgressMonitor monitor) throws CoreException {
			getDelegate().create(updateFlags, local, monitor);
		}

		/** {@inheritDoc} */
		public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor) throws CoreException {
			getDelegate().createLink(localLocation, updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException {
			getDelegate().createLink(location, updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException {
			getDelegate().delete(force, keepHistory, monitor);
		}

		/** {@inheritDoc} */
		public IFile getFile(String name) {
			return getDelegate().getFile(name);
		}

		/** {@inheritDoc} */
		public IFolder getFolder(String name) {
			return getDelegate().getFolder(name);
		}

		/** {@inheritDoc} */
		public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {
			getDelegate().move(destination, force, keepHistory, monitor);
		}
	}

	/**
	 * @author Eike Stepper
	 */
	public static class IFileWrapper extends ProjectResourceWrapper implements IFile {
		public IFileWrapper(ECPProject project, IFile file) {
			super(project, file);
		}

		@Override
		public IFile getDelegate() {
			return (IFile) super.getDelegate();
		}

		/** {@inheritDoc} */
		public void appendContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {
			getDelegate().appendContents(source, force, keepHistory, monitor);
		}

		/** {@inheritDoc} */
		public void appendContents(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException {
			getDelegate().appendContents(source, updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public void create(InputStream source, boolean force, IProgressMonitor monitor) throws CoreException {
			getDelegate().create(source, force, monitor);
		}

		/** {@inheritDoc} */
		public void create(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException {
			getDelegate().create(source, updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public void createLink(IPath localLocation, int updateFlags, IProgressMonitor monitor) throws CoreException {
			getDelegate().createLink(localLocation, updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public void createLink(URI location, int updateFlags, IProgressMonitor monitor) throws CoreException {
			getDelegate().createLink(location, updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public void delete(boolean force, boolean keepHistory, IProgressMonitor monitor) throws CoreException {
			getDelegate().delete(force, keepHistory, monitor);
		}

		/** {@inheritDoc} */
		public String getCharset() throws CoreException {
			return getDelegate().getCharset();
		}

		/** {@inheritDoc} */
		public String getCharset(boolean checkImplicit) throws CoreException {
			return getDelegate().getCharset(checkImplicit);
		}

		/** {@inheritDoc} */
		public String getCharsetFor(Reader reader) throws CoreException {
			return getDelegate().getCharsetFor(reader);
		}

		/** {@inheritDoc} */
		public IContentDescription getContentDescription() throws CoreException {
			return getDelegate().getContentDescription();
		}

		/** {@inheritDoc} */
		public InputStream getContents() throws CoreException {
			return getDelegate().getContents();
		}

		/** {@inheritDoc} */
		public InputStream getContents(boolean force) throws CoreException {
			return getDelegate().getContents(force);
		}

		/** {@inheritDoc} */
		@Deprecated
		public int getEncoding() throws CoreException {
			return getDelegate().getEncoding();
		}

		/** {@inheritDoc} */
		public IFileState[] getHistory(IProgressMonitor monitor) throws CoreException {
			return getDelegate().getHistory(monitor);
		}

		/** {@inheritDoc} */
		public void move(IPath destination, boolean force, boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {
			getDelegate().move(destination, force, keepHistory, monitor);
		}

		/** {@inheritDoc} */
		@Deprecated
		public void setCharset(String newCharset) throws CoreException {
			getDelegate().setCharset(newCharset);
		}

		/** {@inheritDoc} */
		public void setCharset(String newCharset, IProgressMonitor monitor) throws CoreException {
			getDelegate().setCharset(newCharset, monitor);
		}

		/** {@inheritDoc} */
		public void setContents(InputStream source, boolean force, boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {
			getDelegate().setContents(source, force, keepHistory, monitor);
		}

		/** {@inheritDoc} */
		public void setContents(IFileState source, boolean force, boolean keepHistory, IProgressMonitor monitor)
			throws CoreException {
			getDelegate().setContents(source, force, keepHistory, monitor);
		}

		/** {@inheritDoc} */
		public void setContents(InputStream source, int updateFlags, IProgressMonitor monitor) throws CoreException {
			getDelegate().setContents(source, updateFlags, monitor);
		}

		/** {@inheritDoc} */
		public void setContents(IFileState source, int updateFlags, IProgressMonitor monitor) throws CoreException {
			getDelegate().setContents(source, updateFlags, monitor);
		}
	}
}
