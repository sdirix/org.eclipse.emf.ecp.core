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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.ECPContainer;
import org.eclipse.emf.ecp.core.util.ECPProviderAware;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.core.util.observer.ECPObserver;
import org.eclipse.emf.ecp.core.util.observer.ECPProvidersChangedObserver;
import org.eclipse.emf.ecp.internal.core.util.ElementRegistry;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser;
import org.eclipse.emf.ecp.internal.core.util.ExtensionParser.ExtensionDescriptor;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.spi.core.util.AdapterProvider;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.net4j.util.AdapterUtil;

/**
 * This class manages {@link ECPProvider}.
 * 
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public final class ECPProviderRegistryImpl extends ElementRegistry<InternalProvider, ECPObserver> implements
	ECPProviderRegistry {
	/**
	 * The Singleton to access the implementation of the Default ECPProviderRegistry.
	 * 
	 * @deprecated use {@link ECPUtil} instead
	 */
	@Deprecated
	public static ECPProviderRegistryImpl INSTANCE;

	private final ProviderParser extensionParser = new ProviderParser();

	/**
	 * Should not be called directly, use service instead.
	 */
	public ECPProviderRegistryImpl() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Manager must not be initialized twice"); //$NON-NLS-1$
		}
		INSTANCE = this;
	}

	/** {@inheritDoc} **/
	public InternalProvider getProvider(Object adaptable) {
		if (adaptable instanceof ECPProviderAware) {
			final ECPProviderAware providerAware = (ECPProviderAware) adaptable;
			return (InternalProvider) providerAware.getProvider();
		}
		return AdapterUtil.adapt(adaptable, InternalProvider.class);
	}

	/** {@inheritDoc} **/
	public InternalProvider getProvider(String name) {
		return getElement(name);
	}

	/** {@inheritDoc} **/
	public Collection<ECPProvider> getProviders() {
		return (Collection) getElements();
	}

	/** {@inheritDoc} **/
	public void addProvider(ECPProvider provider) {
		changeElements(null, Collections.singleton((InternalProvider) provider));
	}

	/** {@inheritDoc} **/
	public void removeProvider(String name) {
		changeElements(Collections.singleton(name), null);
	}

	// @Override
	// protected InternalProvider[] createElementArray(int size) {
	// return new InternalProvider[size];
	// }

	/** {@inheritDoc} **/
	@Override
	protected void notifyObservers(Collection<InternalProvider> oldProviders, Collection<InternalProvider> newProviders)
		throws Exception {
		ECPUtil.getECPObserverBus().notify(ECPProvidersChangedObserver.class)
			.providersChanged((Collection) oldProviders, (Collection) newProviders);
	}

	@Override
	protected void elementsChanged(Collection<InternalProvider> oldElements, Collection<InternalProvider> newElements) {
		super.elementsChanged(oldElements, newElements);
	}

	@Override
	protected void doActivate() throws Exception {
		super.doActivate();
		extensionParser.activate();
	}

	@Override
	protected void doDeactivate() throws Exception {
		extensionParser.deactivate();
		super.doDeactivate();
	}

	/**
	 * @author Eike Stepper
	 */
	private final class ProviderParser extends ExtensionParser<InternalProvider> {
		private static final String EXTENSION_POINT_NAME = "providers"; //$NON-NLS-1$

		public ProviderParser() {
			super(ECPProviderRegistryImpl.this, Activator.PLUGIN_ID, EXTENSION_POINT_NAME);
		}

		@Override
		protected InternalProvider createElement(String name, IConfigurationElement configurationElement) {
			final ProviderDescriptor descriptor = new ProviderDescriptor(name, configurationElement);
			descriptor.setLabel(configurationElement.getDeclaringExtension().getLabel());
			descriptor.setDescription(configurationElement.getAttribute("description"));
			return descriptor;
		}
	}

	/**
	 * @author Eike Stepper
	 */
	private final class ProviderDescriptor extends ExtensionDescriptor<InternalProvider> implements InternalProvider {
		private AdapterProvider uiProvider;

		public ProviderDescriptor(String name, IConfigurationElement configurationElement) {
			super(ECPProviderRegistryImpl.this, name, TYPE, configurationElement);
		}

		/** {@inheritDoc} */
		public ECPProvider getProvider() {
			return this;
		}

		/** {@inheritDoc} */
		public AdapterProvider getUIProvider() {
			return uiProvider;
		}

		/** {@inheritDoc} */
		public void setUIProvider(AdapterProvider uiProvider) {
			this.uiProvider = uiProvider;
			if (isResolved()) {
				getResolvedElement().setUIProvider(uiProvider);
			}
		}

		/** {@inheritDoc} */
		public <T> T getAdapter(Object adaptable, Class<T> adapterType) {
			return getResolvedElement().getAdapter(adaptable, adapterType);
		}

		/** {@inheritDoc} */
		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapterType) {
			return getResolvedElement().getAdapter(adapterType);
		}

		/** {@inheritDoc} */
		public Set<InternalProject> getOpenProjects() {
			return getResolvedElement().getOpenProjects();
		}

		/** {@inheritDoc} */
		public EditingDomain createEditingDomain(InternalProject project) {
			return getResolvedElement().createEditingDomain(project);
		}

		/** {@inheritDoc} */
		public boolean hasCreateRepositorySupport() {
			return getResolvedElement().hasCreateRepositorySupport();
		}

		/** {@inheritDoc} */
		public boolean isSlow(Object parent) {
			return getResolvedElement().isSlow(parent);
		}

		/** {@inheritDoc} */
		public ECPContainer getModelContext(Object element) {
			return getResolvedElement().getModelContext(element);
		}

		/** {@inheritDoc} */
		public void fillChildren(ECPContainer context, Object parent, InternalChildrenList childrenList) {
			getResolvedElement().fillChildren(context, parent, childrenList);
		}

		/** {@inheritDoc} */
		public void handleLifecycle(ECPContainer context, LifecycleEvent event) {
			getResolvedElement().handleLifecycle(context, event);
		}

		/** {@inheritDoc} */
		@Override
		protected void resolvedElement(InternalProvider provider) {
			super.resolvedElement(provider);
			provider.setLabel(getLabel());
			provider.setDescription(getDescription());
			provider.setUIProvider(uiProvider);
		}

		/** {@inheritDoc} */
		@Override
		protected void doDispose() {
			uiProvider = null;
			super.doDispose();
		}

		/** {@inheritDoc} */
		public boolean hasCreateProjectWithoutRepositorySupport() {
			return getResolvedElement().hasCreateProjectWithoutRepositorySupport();
		}

		/** {@inheritDoc} */
		public EList<? extends Object> getElements(InternalProject project) {
			return getResolvedElement().getElements(project);
		}

		/** {@inheritDoc} */
		public Iterator<EObject> getLinkElements(InternalProject project, EObject modelElement, EReference eReference) {
			return getResolvedElement().getLinkElements(project, modelElement, eReference);
		}

		/** {@inheritDoc} */
		public Set<EPackage> getUnsupportedEPackages(Collection<EPackage> ePackages, InternalRepository repository) {
			return getResolvedElement().getUnsupportedEPackages(ePackages, repository);
		}

		/** {@inheritDoc} */
		public void doSave(InternalProject project) {
			getResolvedElement().doSave(project);
		}

		/** {@inheritDoc} */
		public boolean isDirty(InternalProject project) {
			return getResolvedElement().isDirty(project);
		}

		/** {@inheritDoc} */
		public void delete(InternalProject project, Collection<Object> objects) {
			getResolvedElement().delete(project, objects);
		}

		/** {@inheritDoc} */
		public void cloneProject(InternalProject projectToClone, InternalProject targetProject) {
			getResolvedElement().cloneProject(projectToClone, targetProject);
		}

		/** {@inheritDoc} */
		public boolean modelExists(InternalProject project) {
			return getResolvedElement().modelExists(project);
		}

		/** {@inheritDoc} */
		public Notifier getRoot(InternalProject project) {
			return getResolvedElement().getRoot(project);
		}

		/** {@inheritDoc} */
		public boolean contains(InternalProject project, Object object) {
			return getResolvedElement().contains(project, object);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.spi.core.InternalProvider#isThreadSafe()
		 */
		public boolean isThreadSafe() {
			return getResolvedElement().isThreadSafe();
		}
	}
}
