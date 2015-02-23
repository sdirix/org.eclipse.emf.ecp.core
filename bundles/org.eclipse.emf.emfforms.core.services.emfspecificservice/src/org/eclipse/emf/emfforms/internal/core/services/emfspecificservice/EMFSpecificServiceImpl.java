/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfforms.internal.core.services.emfspecificservice;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.emfforms.spi.core.services.emfspecificservice.EMFSpecificService;

/**
 * Implementation of {@link EMFSpecificService}.
 *
 * @author Lucas Koehler
 *
 */
public class EMFSpecificServiceImpl implements EMFSpecificService {

	private ComposedAdapterFactory composedAdapterFactory; // TODO: discuss: need dispose?

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfforms.spi.core.services.emfspecificservice.EMFSpecificService#getComposedAdapterFactory()
	 */
	@Override
	public ComposedAdapterFactory getComposedAdapterFactory() {
		if (composedAdapterFactory == null) {
			composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
				new ReflectiveItemProviderAdapterFactory(),
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		}
		return composedAdapterFactory;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.emfforms.spi.core.services.emfspecificservice.EMFSpecificService#getAdapterFactoryItemDelegator()
	 */
	@Override
	public AdapterFactoryItemDelegator getAdapterFactoryItemDelegator() {
		return new AdapterFactoryItemDelegator(getComposedAdapterFactory());
	}
}
