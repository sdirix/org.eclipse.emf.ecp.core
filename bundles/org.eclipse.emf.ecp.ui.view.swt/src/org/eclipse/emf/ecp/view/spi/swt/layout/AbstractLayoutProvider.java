/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.swt.layout;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * Abstract implementation of a {@link LayoutProvider} which contributes helper methods.
 *
 * @author Eugen Neufeld
 * @since 1.3
 *
 */
public abstract class AbstractLayoutProvider implements LayoutProvider {

	/**
	 * Checks whether a setting is set to multiline.
	 *
	 * @param setting the {@link Setting} to check
	 * @return true if multiline, false otherwise
	 */
	protected static boolean isMultiLine(Setting setting) {
		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);
		final IItemPropertyDescriptor descriptor = adapterFactoryItemDelegator.getPropertyDescriptor(
			setting.getEObject(), setting.getEStructuralFeature());
		final boolean multiline = descriptor.isMultiLine(null);

		composedAdapterFactory.dispose();

		return multiline;
	}

}
