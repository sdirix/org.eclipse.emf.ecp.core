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
 * Eugen Neufeld - JavaDoc
 *******************************************************************************/

package org.eclipse.emf.ecp.internal.core;

import org.eclipse.net4j.util.AdapterUtil;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPRepositoryAware;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.ECPObserverBus;
import org.eclipse.emf.ecp.core.util.observer.IECPProvidersChangedObserver;
import org.eclipse.emf.ecp.core.util.observer.IECPRepositoriesChangedObserver;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser.ExtensionDescriptor;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.spi.core.InternalRepository;

import org.eclipse.core.runtime.IConfigurationElement;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Set;

/**
 * This class manages the repositories.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public final class ECPRepositoryManagerImpl extends
	PropertiesStore<InternalRepository, IECPRepositoriesChangedObserver> implements ECPRepositoryManager,
	IECPProvidersChangedObserver {
	/**
	 * This Singleton is used by the {@link ECPRepositoryManager#INSTANCE}.
	 */
	public static final ECPRepositoryManagerImpl INSTANCE = new ECPRepositoryManagerImpl();

	private final RepositoryParser extensionParser = new RepositoryParser();

	private ECPRepositoryManagerImpl() {
	}

	/** {@inheritDoc} **/
	public InternalRepository getRepository(Object adaptable) {
		if (adaptable instanceof ECPRepositoryAware) {
			ECPRepositoryAware repositoryAware = (ECPRepositoryAware) adaptable;
			return (InternalRepository) repositoryAware.getRepository();
		}

		return AdapterUtil.adapt(adaptable, InternalRepository.class);
	}

	/** {@inheritDoc} **/
	public InternalRepository getRepository(String name) {
		return getElement(name);
	}

	/** {@inheritDoc} **/
	public InternalRepository[] getRepositories() {
		return getElements();
	}

	/** {@inheritDoc} **/
	public boolean hasRepositories() {
		return hasElements();
	}

	/** {@inheritDoc} **/
	public ECPRepository addRepository(ECPProvider provider, String name, String label, String description,
		ECPProperties properties) {
		InternalRepository repository = new ECPRepositoryImpl(provider, name, properties);
		repository.setLabel(label);
		repository.setDescription(description);

		((InternalProvider) provider).handleLifecycle(repository, LifecycleEvent.CREATE);
		changeElements(null, Collections.singleton(repository));
		return repository;
	}

	/**
	 * This is called by the {@link ECPRepository} to notificate observers about chnages it its objects.
	 * 
	 * @param repository the repository where the changes occured
	 * @param objects the changed objects
	 */
	public void notifyObjectsChanged(ECPRepository repository, Object[] objects) {

		try {
			ECPObserverBus.getInstance().notify(IECPRepositoriesChangedObserver.class)
				.objectsChanged(repository, objects);
		} catch (Exception ex) {
			Activator.log(ex);
		}
	}

	/** {@inheritDoc} **/
	public void providersChanged(ECPProvider[] oldProviders, ECPProvider[] newProviders) throws Exception {
		Set<ECPProvider> addedProviders = ECPUtil.getAddedElements(oldProviders, newProviders);
		if (!addedProviders.isEmpty()) {
			load();
		}
	}

	@Override
	protected InternalRepository loadElement(ObjectInput in) throws IOException {
		return new ECPRepositoryImpl(in);
	}

	@Override
	protected InternalRepository[] createElementArray(int size) {
		return new InternalRepository[size];
	}

	@Override
	protected void notifyObservers(IECPRepositoriesChangedObserver observer, InternalRepository[] oldRepositories,
		InternalRepository[] newRepositories) throws Exception {
		observer.repositoriesChanged(oldRepositories, newRepositories);
	}

	@Override
	protected void doActivate() throws Exception {
		super.doActivate();
		extensionParser.activate();
		ECPProviderRegistry.INSTANCE.addObserver(this);
	}

	@Override
	protected void doDeactivate() throws Exception {
		ECPProviderRegistry.INSTANCE.removeObserver(this);
		extensionParser.deactivate();
		super.doDeactivate();
	}

	/**
	 * @author Eike Stepper
	 */
	private final class RepositoryParser extends ExtensionParser<InternalRepository> {
		private static final String EXTENSION_POINT_NAME = "repositories";

		public RepositoryParser() {
			super(ECPRepositoryManagerImpl.this, Activator.PLUGIN_ID, EXTENSION_POINT_NAME);
		}

		@Override
		protected InternalRepository createElement(String name, IConfigurationElement configurationElement) {
			RepositoryDescriptor descriptor = new RepositoryDescriptor(name, configurationElement);
			descriptor.setLabel(configurationElement.getDeclaringExtension().getLabel());
			descriptor.setDescription(configurationElement.getAttribute("description"));
			return descriptor;
		}
	}

	/**
	 * @author Eike Stepper
	 */
	private final class RepositoryDescriptor extends ExtensionDescriptor<InternalRepository> implements
		InternalRepository {
		private ECPProperties properties = ECPUtil.createProperties();

		public RepositoryDescriptor(String name, IConfigurationElement configurationElement) {
			super(ECPRepositoryManagerImpl.this, name, TYPE, configurationElement);
			for (IConfigurationElement property : configurationElement.getChildren("property")) {
				String key = property.getAttribute("key");
				String value = property.getAttribute("value");
				properties.addProperty(key, value);
			}
		}

		public boolean isStorable() {
			return false;
		}

		public void write(ObjectOutput out) throws IOException {
			throw new UnsupportedOperationException();
		}

		public InternalProvider getProvider() {
			String providerName = getConfigurationElement().getAttribute("provider");
			return (InternalProvider) ECPProviderRegistry.INSTANCE.getProvider(providerName);
		}

		public ECPProperties getProperties() {
			return properties;
		}

		public Object getProviderSpecificData() {
			return getResolvedElement().getProviderSpecificData();
		}

		public void setProviderSpecificData(Object data) {
			getResolvedElement().setProviderSpecificData(data);
		}

		public ECPProject[] getOpenProjects() {
			return getResolvedElement().getOpenProjects();
		}

		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapterType) {
			return getResolvedElement().getAdapter(adapterType);
		}

		public ECPModelContext getContext() {
			return this;
		}

		public boolean canDelete() {
			return false;
		}

		public void delete() {
			throw new UnsupportedOperationException();
		}

		public void notifyObjectsChanged(Object[] objects) {
			getResolvedElement().notifyObjectsChanged(objects);
		}

		@Override
		protected InternalRepository resolve() throws Exception {
			return new ECPRepositoryImpl(getProvider(), getName(), getProperties());
		}
	}
}
