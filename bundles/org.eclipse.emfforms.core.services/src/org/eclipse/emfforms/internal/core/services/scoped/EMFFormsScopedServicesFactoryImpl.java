/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.core.services.scoped;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicePolicy;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceProvider;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServiceScope;
import org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicesFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * The implementation of the EMFFormsScopedServicesFactory.
 *
 * @author Eugen Neufeld
 *
 */
@Component
public class EMFFormsScopedServicesFactoryImpl implements EMFFormsScopedServicesFactory {

	private final Map<Class<?>, EMFFormsScopedServiceProvider<?>> localImmediateMap = new LinkedHashMap<Class<?>, EMFFormsScopedServiceProvider<?>>();
	private final Map<Class<?>, EMFFormsScopedServiceProvider<?>> localLazyMap = new LinkedHashMap<Class<?>, EMFFormsScopedServiceProvider<?>>();
	private final Map<Class<?>, EMFFormsScopedServiceProvider<?>> globalImmediateMap = new LinkedHashMap<Class<?>, EMFFormsScopedServiceProvider<?>>();
	private final Map<Class<?>, EMFFormsScopedServiceProvider<?>> globalLazyMap = new LinkedHashMap<Class<?>, EMFFormsScopedServiceProvider<?>>();

	/**
	 * Called by OSGi whenever a new provider is available.
	 *
	 * @param provider The newly available provider
	 */
	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	protected void addEMFFormsScopedServiceProvider(EMFFormsScopedServiceProvider<?> provider) {
		final Map<Class<?>, EMFFormsScopedServiceProvider<?>> serviceProviderMap = getServiceProviderMap(provider);
		double currentPrio = Double.NEGATIVE_INFINITY;

		if (serviceProviderMap.containsKey(provider.getType())) {
			currentPrio = serviceProviderMap.get(provider.getType()).getPriority();
		}

		if (currentPrio < provider.getPriority()) {
			serviceProviderMap.put(provider.getType(), provider);
		}
	}

	/**
	 * Called by OSGi whenever a provider is removed.
	 *
	 * @param provider The removed provider
	 */
	protected void removeEMFFormsScopedServiceProvider(EMFFormsScopedServiceProvider<?> provider) {
		getServiceProviderMap(provider).remove(provider.getType());
	}

	private Map<Class<?>, EMFFormsScopedServiceProvider<?>> getServiceProviderMap(
		EMFFormsScopedServiceProvider<?> provider) {
		if (provider.getPolicy() == EMFFormsScopedServicePolicy.IMMEDIATE
			&& provider.getScope() == EMFFormsScopedServiceScope.GLOBAL) {
			return globalImmediateMap;
		} else if (provider.getPolicy() == EMFFormsScopedServicePolicy.LAZY
			&& provider.getScope() == EMFFormsScopedServiceScope.GLOBAL) {
			return globalLazyMap;
		} else if (provider.getPolicy() == EMFFormsScopedServicePolicy.IMMEDIATE
			&& provider.getScope() == EMFFormsScopedServiceScope.LOCAL) {
			return localImmediateMap;
		} else if (provider.getPolicy() == EMFFormsScopedServicePolicy.LAZY
			&& provider.getScope() == EMFFormsScopedServiceScope.LOCAL) {
			return localLazyMap;
		}
		throw new IllegalStateException(
			"This should never be reached as we check for all possible cases before.s Only when there is one more combination of policy and scope that we missed this can happen!"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicesFactory#getLocalImmediateService(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> getLocalImmediateService(Class<T> type) {
		final EMFFormsScopedServiceProvider<?> serviceProvider = localImmediateMap.get(type);
		if (serviceProvider != null) {
			return (Optional<T>) Optional.of(serviceProvider.provideService());
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicesFactory#getLocalLazyService(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> getLocalLazyService(Class<T> type) {
		final EMFFormsScopedServiceProvider<?> serviceProvider = localLazyMap.get(type);
		if (serviceProvider != null) {
			return (Optional<T>) Optional.of(serviceProvider.provideService());
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicesFactory#getGlobalImmediateService(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> getGlobalImmediateService(Class<T> type) {
		final EMFFormsScopedServiceProvider<?> serviceProvider = globalImmediateMap.get(type);
		if (serviceProvider != null) {
			return (Optional<T>) Optional.of(serviceProvider.provideService());
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emfforms.spi.core.services.scoped.EMFFormsScopedServicesFactory#getGlobalLazyService(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> getGlobalLazyService(Class<T> type) {
		final EMFFormsScopedServiceProvider<?> serviceProvider = globalLazyMap.get(type);
		if (serviceProvider != null) {
			return (Optional<T>) Optional.of(serviceProvider.provideService());
		}
		return Optional.empty();
	}

}
