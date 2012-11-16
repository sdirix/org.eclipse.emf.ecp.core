/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.internal.core;

import org.eclipse.net4j.util.AdapterUtil;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.exception.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProjectAware;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.ECPObserverBus;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedObserver;
import org.eclipse.emf.ecp.core.util.observer.IECPRepositoriesChangedObserver;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.spi.core.InternalRepository;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.Collections;
import java.util.Set;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public class ECPProjectManagerImpl extends PropertiesStore<InternalProject, IECPProjectsChangedObserver> implements
	ECPProjectManager, IECPRepositoriesChangedObserver {

	public static final ECPProjectManagerImpl INSTANCE = new ECPProjectManagerImpl();
	private boolean initializedProjects = false;

	private ECPProjectManagerImpl() {
	}

	public ECPProject createProject(ECPProvider provider, String name) throws ProjectWithNameExistsException {
		return this.createProject(provider, name, ECPUtil.createProperties());
	}

	public ECPProject createProject(ECPProvider provider, String name, ECPProperties properties)
		throws ProjectWithNameExistsException {
		if (projectExists(name)) {
			throw new ProjectWithNameExistsException("A project with name " + name + " already exists");
		}
		InternalProject project = new ECPProjectImpl((InternalProvider) provider, name, properties);
		return createProject(project);
	}

	public ECPProject createProject(ECPRepository repository, String name, ECPProperties properties)
		throws ProjectWithNameExistsException {
		if (projectExists(name)) {
			throw new ProjectWithNameExistsException("A project with name " + name + " already exists");
		}
		InternalProject project = new ECPProjectImpl(repository, name, properties);
		return createProject(project);
	}

	private boolean projectExists(String name) {
		return getProject(name) != null;
	}

	/**
	 * @param project
	 * @return
	 */
	private ECPProject createProject(InternalProject project) {
		project.getProvider().handleLifecycle(project, LifecycleEvent.CREATE);
		changeElements(null, Collections.singleton(project));

		return project;
	}

	public ECPProject cloneProject(ECPProject project) {
		InternalProject internalProject = (InternalProject) project;
		InternalProject newProject = internalProject.clone();
		return createProject(newProject);
	}

	public InternalProject getProject(Object adaptable) {
		if (adaptable instanceof ECPProjectAware) {
			ECPProjectAware projectAware = (ECPProjectAware) adaptable;
			return (InternalProject) projectAware.getProject();
		}

		return AdapterUtil.adapt(adaptable, InternalProject.class);
	}

	public InternalProject getProject(String name) {
		return getElement(name);
	}

	public InternalProject[] getProjects() {
		InternalProject[] projects = getElements();
		if (!initializedProjects) {

			for (InternalProject project : projects) {

				if (!project.getProvider().modelExists(project)) {
					project.close();
					Activator.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						"Project Data was deleted since last start. Project is now closed."));

					continue;
				}
				project.getProvider().handleLifecycle(project, LifecycleEvent.INIT);
			}
			initializedProjects = true;
		}

		return projects;
	}

	@Override
	public void storeElement(InternalProject project) {
		super.storeElement(project);
	}

	public void changeProject(ECPProject project, boolean opened, boolean store) {
		if (store) {
			storeElement((InternalProject) project);
		}

		try {
			ECPObserverBus.getInstance().notify(IECPProjectsChangedObserver.class).projectChanged(project, opened);
		} catch (Exception ex) {
			Activator.log(ex);
		}
	}

	public void notifyObjectsChanged(ECPProject project, Object[] objects, boolean structural) {

		try {
			ECPObserverBus.getInstance().notify(IECPProjectsChangedObserver.class)
				.objectsChanged(project, objects, structural);
		} catch (Exception ex) {
			Activator.log(ex);
		}
	}

	public void repositoriesChanged(ECPRepository[] oldRepositories, ECPRepository[] newRepositories) throws Exception {
		Set<ECPRepository> addedRepositories = ECPUtil.getAddedElements(oldRepositories, newRepositories);
		InternalProject[] projects = getProjects();

		for (ECPRepository repository : addedRepositories) {
			for (InternalProject project : projects) {
				if (!project.isOpen() && project.getRepository().getName().equals(repository.getName())) {
					project.undispose((InternalRepository) repository);
				}
			}
		}
	}

	public void objectsChanged(ECPRepository repository, Object[] objects) throws Exception {
		// Do nothing
	}

	@Override
	protected void elementsChanged(InternalProject[] oldElements, InternalProject[] newElements) {
		super.elementsChanged(oldElements, newElements);
	}

	@Override
	protected void doActivate() throws Exception {
		super.doActivate();
		ECPRepositoryManager.INSTANCE.addObserver(this);
	}

	@Override
	protected void doDeactivate() throws Exception {
		ECPRepositoryManager.INSTANCE.removeObserver(this);
		super.doDeactivate();
	}

	@Override
	protected InternalProject loadElement(ObjectInput in) throws IOException {
		return new ECPProjectImpl(in);
	}

	@Override
	protected InternalProject[] createElementArray(int size) {
		return new InternalProject[size];
	}

	@Override
	protected void notifyObservers(IECPProjectsChangedObserver observer, InternalProject[] oldElements,
		InternalProject[] newElements) throws Exception {
		observer.projectsChanged(oldElements, newElements);
	}

	@Override
	protected boolean isRemoveDisposedElements() {
		return false;
	}

}
