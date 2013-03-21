/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 * Eugen Neufeld - JavaDoc and API cleanup
 * Jonas Helming - API cleanup
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.core;

import org.eclipse.emf.ecp.core.util.observer.IECPProvidersChangedObserver;

/**
 * Registry class to manage the registered providers.
 * 
 * @author Eike Stepper
 * @author Jonas
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ECPProviderRegistry {
	/**
	 * Instance of the ECPProviderRegistry.
	 */
	ECPProviderRegistry INSTANCE = org.eclipse.emf.ecp.internal.core.ECPProviderRegistryImpl.INSTANCE;

	/**
	 * This method returns the {@link ECPProvider} based on the adaptable. The adaptable must be of type
	 * {@link org.eclipse.emf.ecp.core.util.ECPProviderAware ECPProviderAware}.
	 * 
	 * @param adaptable the adaptable to adapt
	 * @return the adapted {@link ECPProvider} or null
	 */
	ECPProvider getProvider(Object adaptable);

	/**
	 * This method returns the ECPProvider based on the name.
	 * 
	 * @param name the name of the {@link ECPProvider} to search for
	 * @return the {@link ECPProvider}
	 */
	// FIXME is needed?
	ECPProvider getProvider(String name);

	/**
	 * This method returns all known providers.
	 * 
	 * @return array of {@link ECPProvider ECPProviders}
	 */
	ECPProvider[] getProviders();

	/**
	 * Method to programmatically add an {@link ECPProvider} to list of available provider.
	 * 
	 * @param provider the {@link ECPProvider} to add
	 */
	// TODO is needed?
	void addProvider(ECPProvider provider);

	/**
	 * Delete a provider programmatically from the list of available providers by its name.
	 * 
	 * @param name the name of the provider to delete
	 */
	// TODO is needed?
	void removeProvider(String name);

	/**
	 * Add an {@link IECPProvidersChangedObserver} to be notified.
	 * 
	 * @param changeObserver the observer to add
	 */
	// TODO is needed?
	void addObserver(IECPProvidersChangedObserver changeObserver);

	/**
	 * Remove an {@link IECPProvidersChangedObserver} from the list of the providers to be notified.
	 * 
	 * @param changeObserver the observer to remove
	 */
	// TODO is needed?
	void removeObserver(IECPProvidersChangedObserver changeObserver);
}
