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
package org.eclipse.emfforms.spi.core.services.scoped;

import org.eclipse.emfforms.common.Optional;

/**
 * This Factory provides access methods to retrieve a service of a specific scope.
 *
 * @author Eugen Neufeld
 * @since 1.8
 */
public interface EMFFormsScopedServicesFactory {

	/**
	 * Return a local service which should be activated immediately. If no service of the requested type is available
	 * null will be returned.
	 *
	 * @param <T> The type parameter of the service
	 * @param type The Type of the requested service
	 * @return An optional instance of the requested service registered for this scope.
	 */
	<T> Optional<T> getLocalImmediateService(Class<T> type);

	/**
	 * Return a local service which should be activated on request. If no service of the requested type is available
	 * null will be returned.
	 *
	 * @param <T> The type parameter of the service
	 * @param type The Type of the requested service
	 * @return An optional instance of the requested service registered for this scope.
	 */
	<T> Optional<T> getLocalLazyService(Class<T> type);

	/**
	 * Return a global service which should be activated immediately. If no service of the requested type is available
	 * null will be returned.
	 *
	 * @param <T> The type parameter of the service
	 * @param type The Type of the requested service
	 * @return An optional instance of the requested service registered for this scope.
	 */
	<T> Optional<T> getGlobalImmediateService(Class<T> type);

	/**
	 * Return a global service which should be activated on request. If no service of the requested type is available
	 * null will be returned.
	 *
	 * @param <T> The type parameter of the service
	 * @param type The Type of the requested service
	 * @return An optional instance of the requested service registered for this scope.
	 */
	<T> Optional<T> getGlobalLazyService(Class<T> type);
}
