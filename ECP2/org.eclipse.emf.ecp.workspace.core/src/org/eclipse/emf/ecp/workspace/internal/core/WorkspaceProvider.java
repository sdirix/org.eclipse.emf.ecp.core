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

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.spi.core.DefaultProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class WorkspaceProvider extends DefaultProvider implements IResourceChangeListener {
	public static final String NAME = "org.eclipse.emf.ecp.workspace.provider";

	public static final String PROP_ROOT_URI = "rootURI";

	private static final IWorkspace WORKSPACE = ResourcesPlugin.getWorkspace();

	private static final IWorkspaceRoot WORKSPACE_ROOT = WORKSPACE.getRoot();

	static WorkspaceProvider INSTANCE;

	public WorkspaceProvider() {
		super(NAME);
		INSTANCE = this;
		WORKSPACE.addResourceChangeListener(this);
	}

	@Override
	protected void doDispose() {
		try {
			WORKSPACE.removeResourceChangeListener(this);
			super.doDispose();
		} finally {
			INSTANCE = null;
		}
	}

	@Override
	public boolean canAddRepositories() {
		return false;
	}

	@Override
	public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList) {
		if (parent instanceof ECPRepository) {
			ECPRepository repository = (ECPRepository) parent;
			RepositoryResourceWrapper wrapper = new RepositoryResourceWrapper(repository, WORKSPACE_ROOT);
			wrapper.fillChildren(childrenList);
		} else if (parent instanceof ECPProject) {
			ECPProject project = (ECPProject) parent;
			String rootURI = project.getProperties().getValue(PROP_ROOT_URI);
			if (rootURI == null) {
				ProjectResourceWrapper wrapper = new ProjectResourceWrapper(project, WORKSPACE_ROOT);
				wrapper.fillChildren(childrenList);
			} else {
				ResourceSet resourceSet = project.getEditingDomain().getResourceSet();

				URI uri = URI.createURI(rootURI);
				if (uri.hasFragment()) {
					EObject eObject = resourceSet.getEObject(uri, true);
					super.fillChildren(context, eObject, childrenList);
				} else {
					Resource resource = resourceSet.getResource(uri, true);
					childrenList.addChildren(resource.getContents());

					// String path = uri.toPlatformString(true);
					// IResource member = WORKSPACE_ROOT.findMember(path);
					// ProjectResourceWrapper wrapper = new ProjectResourceWrapper(project, member);
					// wrapper.fillChildren(childrenList);
				}
			}
		} else if (parent instanceof ResourceWrapper) {
			ResourceWrapper<?> wrapper = (ResourceWrapper<?>) parent;
			wrapper.fillChildren(childrenList);
		} else {
			super.fillChildren(context, parent, childrenList);
		}
	}

	public EList<EObject> getElements(InternalProject project) {
		// TODO: implement WorkspaceProvider.addRootElement(project, rootElement)
		throw new UnsupportedOperationException();
	}

	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		if (delta != null) {
			InternalRepository repository = getAllRepositories()[0];
			Object[] objects = getChangedObjects(delta, repository);
			repository.notifyObjectsChanged(objects);

			for (InternalProject project : getOpenProjects()) {
				objects = getChangedObjects(delta, project);
				project.notifyObjectsChanged(objects, true);
			}
		}
	}

	private Object[] getChangedObjects(IResourceDelta delta, InternalRepository repository) {
		Set<Object> objects = new HashSet<Object>();
		collectChangedObjects(delta, repository, objects);
		return objects.toArray(new Object[objects.size()]);
	}

	private void collectChangedObjects(IResourceDelta delta, InternalRepository repository, Set<Object> objects) {
		switch (delta.getKind()) {
		case IResourceDelta.ADDED:
		case IResourceDelta.REMOVED:
			IResource resource = delta.getResource();
			if (resource.getType() == IResource.PROJECT) {
				objects.add(repository);
			} else {
				objects.add(new RepositoryResourceWrapper(repository, resource.getParent()));
			}

			return;
		}

		for (IResourceDelta child : delta.getAffectedChildren()) {
			collectChangedObjects(child, repository, objects);
		}
	}

	private Object[] getChangedObjects(IResourceDelta delta, InternalProject project) {
		IPath rootPath = getRootResource(project).getFullPath();
		IPath deltaPath = delta.getResource().getFullPath();
		if (rootPath.equals(deltaPath)) {
			return new Object[] { project };
		}

		if (rootPath.isPrefixOf(deltaPath)) {
			Set<Object> objects = new HashSet<Object>();
			collectChangedObjects(delta, project, objects);
			return objects.toArray(new Object[objects.size()]);
		}

		return null;
	}

	private void collectChangedObjects(IResourceDelta delta, InternalProject project, Set<Object> objects) {
		switch (delta.getKind()) {
		case IResourceDelta.ADDED:
		case IResourceDelta.REMOVED:
			IResource resource = delta.getResource();
			objects.add(new ProjectResourceWrapper(project, resource.getParent()));
			return;
		}

		for (IResourceDelta child : delta.getAffectedChildren()) {
			collectChangedObjects(child, project, objects);
		}
	}

	public static Object getElement(ECPProject project, URI uri) {
		if (uri == null) {
			return WORKSPACE_ROOT;
		}

		if (uri.hasFragment()) {
			ResourceSet resourceSet = project.getEditingDomain().getResourceSet();
			return resourceSet.getEObject(uri, true);
		}

		String path = uri.toPlatformString(true);
		return WORKSPACE_ROOT.findMember(path);
	}

	public static IResource getResource(ECPProject project, URI uri) {
		if (uri != null) {
			uri = uri.trimFragment();
		}

		return (IResource) getElement(project, uri);
	}

	public static Object getRootElement(ECPProject project) {
		URI uri = getRootURI(project);
		return getElement(project, uri);
	}

	public static IResource getRootResource(ECPProject project) {
		URI uri = getRootURI(project);
		return getResource(project, uri);
	}

	public static URI getRootURI(ECPProject project) {
		String rootURI = project.getProperties().getValue(PROP_ROOT_URI);
		return rootURI == null ? null : URI.createURI(rootURI);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.spi.core.InternalProvider#delete(org.eclipse.emf.ecp.spi.core.InternalProject,
	 * java.util.Collection)
	 */
	public void delete(InternalProject project, Collection<EObject> eObjects) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.spi.core.InternalProvider#cloneProject(org.eclipse.emf.ecp.spi.core.InternalProject,
	 * org.eclipse.emf.ecp.spi.core.InternalProject)
	 */
	public void cloneProject(final InternalProject projectToClone, InternalProject targetProject) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.spi.core.InternalProvider#contains(org.eclipse.emf.ecp.spi.core.InternalProject,
	 * org.eclipse.emf.ecore.EObject)
	 */
	public boolean contains(InternalProject project, EObject eObject) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.spi.core.InternalProvider#getRoot(org.eclipse.emf.ecp.spi.core.InternalProject)
	 */
	// FIXME
	public Notifier getRoot(InternalProject project) {
		return null;
	}

}
