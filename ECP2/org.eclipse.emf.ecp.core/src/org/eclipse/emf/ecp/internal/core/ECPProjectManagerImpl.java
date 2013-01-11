/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 * Eugen Neufeld - JavaDoc and changes
 *******************************************************************************/
package org.eclipse.emf.ecp.internal.core;

import org.eclipse.net4j.util.AdapterUtil;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.exception.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProjectAware;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.ECPObserverBus;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectObjectsChangedObserver;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedUIObserver;
import org.eclipse.emf.ecp.core.util.observer.IECPRepositoriesChangedObserver;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.spi.core.InternalRepository;

import org.eclipse.core.runtime.IStatus;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class manages the available {@link ECPProject ECPProjects}.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public final class ECPProjectManagerImpl extends PropertiesStore<InternalProject, IECPProjectsChangedUIObserver>
	implements ECPProjectManager, IECPRepositoriesChangedObserver {

	/**
	 * The singleton that is returned by the {@link ECPProjectManager#INSTANCE}.
	 */
	public static final ECPProjectManagerImpl INSTANCE = new ECPProjectManagerImpl();
	/**
	 * This variable defines whether the projects where already initialized.
	 */
	private boolean initializedProjects = false;

	private ECPProjectManagerImpl() {
	}

	/** {@inheritDoc} */
	public ECPProject createProject(ECPProvider provider, String name) throws ProjectWithNameExistsException {
		return this.createProject(provider, name, ECPUtil.createProperties());
	}

	/** {@inheritDoc} */
	public ECPProject createProject(ECPProvider provider, String name, ECPProperties properties)
		throws ProjectWithNameExistsException {
		if (projectExists(name)) {
			throw new ProjectWithNameExistsException("A project with name " + name + " already exists");
		}
		InternalProject project = new ECPProjectImpl((InternalProvider) provider, name, properties);
		return createProject(project);
	}

	/** {@inheritDoc} */
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

	private ECPProject createProject(InternalProject project) {
		project.getProvider().handleLifecycle(project, LifecycleEvent.CREATE);
		changeElements(null, Collections.singleton(project));

		return project;
	}

	/** {@inheritDoc} */
	public ECPProject cloneProject(ECPProject project) {
		InternalProject internalProject = (InternalProject) project;
		InternalProject newProject = internalProject.clone();
		return createProject(newProject);
	}

	/** {@inheritDoc} */
	public InternalProject getProject(Object adaptable) {
		if (adaptable instanceof ECPProjectAware) {
			ECPProjectAware projectAware = (ECPProjectAware) adaptable;
			return (InternalProject) projectAware.getProject();
		}
		InternalProject result = getInternalProject(adaptable);
		if (result != null) {
			return result;
		}
		return AdapterUtil.adapt(adaptable, InternalProject.class);
	}

	// TODO integrate in getProject
	private InternalProject getInternalProject(Object object) {
		if (InternalProject.class.isInstance(object)) {
			return (InternalProject) object;
		}

		for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders()) {
			InternalProvider internalProvider = (InternalProvider) ECPUtil.getResolvedElement(provider);
			ECPModelContext modelContext = internalProvider.getModelContext(object);
			if (modelContext != null && InternalProject.class.isInstance(modelContext)) {
				return (InternalProject) modelContext;
			}
		}

		return null;
	}

	/** {@inheritDoc} */
	public InternalProject getProject(String name) {
		return getElement(name);
	}

	/** {@inheritDoc} */
	public InternalProject[] getProjects() {
		InternalProject[] projects = getElements();
		if (!initializedProjects) {

			for (InternalProject project : projects) {

				if (!project.getProvider().modelExists(project)) {
					project.close();
					Activator.log(IStatus.ERROR, "Project Data was deleted since last start. Project is now closed.");

					continue;
				}
				project.notifyProvider(LifecycleEvent.INIT);
			}
			initializedProjects = true;
		}

		return projects;
	}

	@Override
	public void storeElement(InternalProject project) {
		super.storeElement(project);
	}

	/**
	 * This is called by projects to notify observers about project changes.
	 * 
	 * @param project the project that called this method
	 * @param opened whether the project is open
	 * @param store whether to store the change
	 */
	public void changeProject(ECPProject project, boolean opened, boolean store) {
		if (store) {
			storeElement((InternalProject) project);
		}

		try {
			ECPObserverBus.getInstance().notify(IECPProjectsChangedUIObserver.class).projectChanged(project, opened);
		} catch (Exception ex) {
			Activator.log(ex);
		}
	}

	/**
	 * This is called by projects to notify observers about object changes.
	 * First the {@link IECPProjectObjectsChangedObserver IECPProjectObjectsChangedObservers} are notified then the
	 * {@link IECPProjectsChangedUIObserver IECPProjectsChangedUIObservers}.
	 * 
	 * @param project the project that called this method
	 * @param objects the objects that changed
	 * @param structural whether the changes where structural
	 */
	public void notifyObjectsChanged(ECPProject project, Object[] objects, boolean structural) {

		try {
			Object[] affected = ECPObserverBus.getInstance().notify(IECPProjectObjectsChangedObserver.class)
				.objectsChanged(project, objects);
			Set<Object> toUpdate = new HashSet<Object>(Arrays.asList(objects));
			if (affected != null) {
				for (Object o : affected) {
					toUpdate.add(o);
				}
			}
			ECPObserverBus.getInstance().notify(IECPProjectsChangedUIObserver.class)
				.objectsChanged(project, toUpdate.toArray(), structural);
		} catch (Exception ex) {
			Activator.log(ex);
		}
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
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
		ECPObserverBus.getInstance().register(this);
	}

	@Override
	protected void doDeactivate() throws Exception {
		ECPObserverBus.getInstance().unregister(this);
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
	protected void notifyObservers(IECPProjectsChangedUIObserver observer, InternalProject[] oldElements,
		InternalProject[] newElements) throws Exception {
		observer.projectsChanged(oldElements, newElements);
	}

	@Override
	protected boolean isRemoveDisposedElements() {
		return false;
	}

}
