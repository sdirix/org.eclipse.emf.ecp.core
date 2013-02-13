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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;

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
	implements IResource {
	public ResourceWrapper(CONTEXT context, IResource delegate) {
		super(context, delegate);
	}

	public final URI getURI() {
		return URI.createPlatformResourceURI(getDelegate().getFullPath().toPortableString(), true);
	}

	@Override
	public String getName() {
		return getDelegate().getName();
	}

	public void fillChildren(InternalChildrenList childrenList) {
		IResource parent = getDelegate();
		if (parent instanceof IFile) {
			// fillFile(childrenList, (IFile) parent);
		} else if (parent instanceof IContainer) {
			fillContainer(childrenList, (IContainer) parent);
		}
	}

	protected void fillFile(InternalChildrenList childrenList, IFile parent) {
		try {
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource emfResource = resourceSet.getResource(getURI(), true);
			childrenList.addChildren(emfResource.getContents());
		} catch (Exception ex) {
			//$FALL-THROUGH$
		}
	}

	protected void fillContainer(InternalChildrenList childrenList, IContainer container) {
		try {
			IResource[] members = container.members();
			for (int i = 0; i < members.length; i++) {
				IResource member = members[i];
				// if (member.getType() == IResource.FILE)
				// {
				// try
				// {
				// URI uri = URI.createPlatformResourceURI(member.getFullPath().toPortableString(), true);
				// ResourceSet resourceSet = new ResourceSetImpl();
				// Resource emfResource = resourceSet.getResource(uri, true);
				// emfResource.getContents().isEmpty(); // Ensure that resource can be loaded
				// childrenList.addChild(emfResource);
				// }
				// catch (Exception ex)
				// {
				// //$FALL-THROUGH$
				// }
				// }
				// else
				// {
				Object child = createChild(getContext(), member);
				if (child != null) {
					childrenList.addChild(child);
				}
				// }
			}
		} catch (CoreException ex) {
			throw new RuntimeException(ex);
		}
	}

	protected abstract Object createChild(CONTEXT context, IResource member);

	/** {@inheritDoc} */
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return getDelegate().getAdapter(adapter);
	}

	/** {@inheritDoc} */
	public boolean contains(ISchedulingRule rule) {
		return getDelegate().contains(rule);
	}

	/** {@inheritDoc} */
	public boolean isConflicting(ISchedulingRule rule) {
		return getDelegate().isConflicting(rule);
	}

	/** {@inheritDoc} */
	public void accept(IResourceProxyVisitor visitor, int memberFlags) throws CoreException {
		getDelegate().accept(visitor, memberFlags);
	}

	/*
	 * This method has been added in 3.8!
	 */
	/** {@inheritDoc} */
	public void accept(IResourceProxyVisitor visitor, int depth, int memberFlags) throws CoreException {
		getDelegate().accept(visitor, depth, memberFlags);
	}

	/** {@inheritDoc} */
	public void accept(IResourceVisitor visitor) throws CoreException {
		getDelegate().accept(visitor);
	}

	/** {@inheritDoc} */
	public void accept(IResourceVisitor visitor, int depth, boolean includePhantoms) throws CoreException {
		getDelegate().accept(visitor, depth, includePhantoms);
	}

	/** {@inheritDoc} */
	public void accept(IResourceVisitor visitor, int depth, int memberFlags) throws CoreException {
		getDelegate().accept(visitor, depth, memberFlags);
	}

	/** {@inheritDoc} */
	public void clearHistory(IProgressMonitor monitor) throws CoreException {
		getDelegate().clearHistory(monitor);
	}

	/** {@inheritDoc} */
	public void copy(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException {
		getDelegate().copy(destination, force, monitor);
	}

	/** {@inheritDoc} */
	public void copy(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException {
		getDelegate().copy(destination, updateFlags, monitor);
	}

	/** {@inheritDoc} */
	public void copy(IProjectDescription description, boolean force, IProgressMonitor monitor) throws CoreException {
		getDelegate().copy(description, force, monitor);
	}

	/** {@inheritDoc} */
	public void copy(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException {
		getDelegate().copy(description, updateFlags, monitor);
	}

	/** {@inheritDoc} */
	public IMarker createMarker(String type) throws CoreException {
		return getDelegate().createMarker(type);
	}

	/** {@inheritDoc} */
	public IResourceProxy createProxy() {
		return getDelegate().createProxy();
	}

	/** {@inheritDoc} */
	public void delete(boolean force, IProgressMonitor monitor) throws CoreException {
		getDelegate().delete(force, monitor);
	}

	/** {@inheritDoc} */
	public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException {
		getDelegate().delete(updateFlags, monitor);
	}

	/** {@inheritDoc} */
	public void deleteMarkers(String type, boolean includeSubtypes, int depth) throws CoreException {
		getDelegate().deleteMarkers(type, includeSubtypes, depth);
	}

	/** {@inheritDoc} */
	public boolean exists() {
		return getDelegate().exists();
	}

	/** {@inheritDoc} */
	public IMarker findMarker(long id) throws CoreException {
		return getDelegate().findMarker(id);
	}

	/** {@inheritDoc} */
	public IMarker[] findMarkers(String type, boolean includeSubtypes, int depth) throws CoreException {
		return getDelegate().findMarkers(type, includeSubtypes, depth);
	}

	/** {@inheritDoc} */
	public int findMaxProblemSeverity(String type, boolean includeSubtypes, int depth) throws CoreException {
		return getDelegate().findMaxProblemSeverity(type, includeSubtypes, depth);
	}

	/** {@inheritDoc} */
	public String getFileExtension() {
		return getDelegate().getFileExtension();
	}

	/** {@inheritDoc} */
	public IPath getFullPath() {
		return getDelegate().getFullPath();
	}

	/** {@inheritDoc} */
	public long getLocalTimeStamp() {
		return getDelegate().getLocalTimeStamp();
	}

	/** {@inheritDoc} */
	public IPath getLocation() {
		return getDelegate().getLocation();
	}

	/** {@inheritDoc} */
	public java.net.URI getLocationURI() {
		return getDelegate().getLocationURI();
	}

	/** {@inheritDoc} */
	public IMarker getMarker(long id) {
		return getDelegate().getMarker(id);
	}

	/** {@inheritDoc} */
	public long getModificationStamp() {
		return getDelegate().getModificationStamp();
	}

	/** {@inheritDoc} */
	public IPathVariableManager getPathVariableManager() {
		return getDelegate().getPathVariableManager();
	}

	/** {@inheritDoc} */
	public IContainer getParent() {
		return getDelegate().getParent();
	}

	/** {@inheritDoc} */
	public Map<QualifiedName, String> getPersistentProperties() throws CoreException {
		return getDelegate().getPersistentProperties();
	}

	/** {@inheritDoc} */
	public String getPersistentProperty(QualifiedName key) throws CoreException {
		return getDelegate().getPersistentProperty(key);
	}

	/** {@inheritDoc} */
	public IProject getProject() {
		return getDelegate().getProject();
	}

	/** {@inheritDoc} */
	public IPath getProjectRelativePath() {
		return getDelegate().getProjectRelativePath();
	}

	/** {@inheritDoc} */
	public IPath getRawLocation() {
		return getDelegate().getRawLocation();
	}

	/** {@inheritDoc} */
	public java.net.URI getRawLocationURI() {
		return getDelegate().getRawLocationURI();
	}

	/** {@inheritDoc} */
	public ResourceAttributes getResourceAttributes() {
		return getDelegate().getResourceAttributes();
	}

	/** {@inheritDoc} */
	public Map<QualifiedName, Object> getSessionProperties() throws CoreException {
		return getDelegate().getSessionProperties();
	}

	/** {@inheritDoc} */
	public Object getSessionProperty(QualifiedName key) throws CoreException {
		return getDelegate().getSessionProperty(key);
	}

	/** {@inheritDoc} */
	public int getType() {
		return getDelegate().getType();
	}

	/** {@inheritDoc} */
	public IWorkspace getWorkspace() {
		return getDelegate().getWorkspace();
	}

	/** {@inheritDoc} */
	public boolean isAccessible() {
		return getDelegate().isAccessible();
	}

	/** {@inheritDoc} */
	public boolean isDerived() {
		return getDelegate().isDerived();
	}

	/** {@inheritDoc} */
	public boolean isDerived(int options) {
		return getDelegate().isDerived(options);
	}

	/** {@inheritDoc} */
	public boolean isHidden() {
		return getDelegate().isHidden();
	}

	/** {@inheritDoc} */
	public boolean isHidden(int options) {
		return getDelegate().isHidden(options);
	}

	/** {@inheritDoc} */
	public boolean isLinked() {
		return getDelegate().isLinked();
	}

	/** {@inheritDoc} */
	public boolean isVirtual() {
		return getDelegate().isVirtual();
	}

	/** {@inheritDoc} */
	public boolean isLinked(int options) {
		return getDelegate().isLinked(options);
	}

	/** {@inheritDoc} */
	@Deprecated
	public boolean isLocal(int depth) {
		return getDelegate().isLocal(depth);
	}

	/** {@inheritDoc} */
	public boolean isPhantom() {
		return getDelegate().isPhantom();
	}

	/** {@inheritDoc} */
	@Deprecated
	public boolean isReadOnly() {
		return getDelegate().isReadOnly();
	}

	/** {@inheritDoc} */
	public boolean isSynchronized(int depth) {
		return getDelegate().isSynchronized(depth);
	}

	/** {@inheritDoc} */
	public boolean isTeamPrivateMember() {
		return getDelegate().isTeamPrivateMember();
	}

	/** {@inheritDoc} */
	public boolean isTeamPrivateMember(int options) {
		return getDelegate().isTeamPrivateMember(options);
	}

	/** {@inheritDoc} */
	public void move(IPath destination, boolean force, IProgressMonitor monitor) throws CoreException {
		getDelegate().move(destination, force, monitor);
	}

	/** {@inheritDoc} */
	public void move(IPath destination, int updateFlags, IProgressMonitor monitor) throws CoreException {
		getDelegate().move(destination, updateFlags, monitor);
	}

	/** {@inheritDoc} */
	public void move(IProjectDescription description, boolean force, boolean keepHistory, IProgressMonitor monitor)
		throws CoreException {
		getDelegate().move(description, force, keepHistory, monitor);
	}

	/** {@inheritDoc} */
	public void move(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException {
		getDelegate().move(description, updateFlags, monitor);
	}

	/** {@inheritDoc} */
	public void refreshLocal(int depth, IProgressMonitor monitor) throws CoreException {
		getDelegate().refreshLocal(depth, monitor);
	}

	/** {@inheritDoc} */
	public void revertModificationStamp(long value) throws CoreException {
		getDelegate().revertModificationStamp(value);
	}

	/** {@inheritDoc} */
	@Deprecated
	public void setDerived(boolean isDerived) throws CoreException {
		getDelegate().setDerived(isDerived);
	}

	/** {@inheritDoc} */
	public void setDerived(boolean isDerived, IProgressMonitor monitor) throws CoreException {
		getDelegate().setDerived(isDerived, monitor);
	}

	/** {@inheritDoc} */
	public void setHidden(boolean isHidden) throws CoreException {
		getDelegate().setHidden(isHidden);
	}

	/** {@inheritDoc} */
	@Deprecated
	public void setLocal(boolean flag, int depth, IProgressMonitor monitor) throws CoreException {
		getDelegate().setLocal(flag, depth, monitor);
	}

	/** {@inheritDoc} */
	public long setLocalTimeStamp(long value) throws CoreException {
		return getDelegate().setLocalTimeStamp(value);
	}

	/** {@inheritDoc} */
	public void setPersistentProperty(QualifiedName key, String value) throws CoreException {
		getDelegate().setPersistentProperty(key, value);
	}

	/** {@inheritDoc} */
	@Deprecated
	public void setReadOnly(boolean readOnly) {
		getDelegate().setReadOnly(readOnly);
	}

	/** {@inheritDoc} */
	public void setResourceAttributes(ResourceAttributes attributes) throws CoreException {
		getDelegate().setResourceAttributes(attributes);
	}

	/** {@inheritDoc} */
	public void setSessionProperty(QualifiedName key, Object value) throws CoreException {
		getDelegate().setSessionProperty(key, value);
	}

	/** {@inheritDoc} */
	public void setTeamPrivateMember(boolean isTeamPrivate) throws CoreException {
		getDelegate().setTeamPrivateMember(isTeamPrivate);
	}

	/** {@inheritDoc} */
	public void touch(IProgressMonitor monitor) throws CoreException {
		getDelegate().touch(monitor);
	}
}
