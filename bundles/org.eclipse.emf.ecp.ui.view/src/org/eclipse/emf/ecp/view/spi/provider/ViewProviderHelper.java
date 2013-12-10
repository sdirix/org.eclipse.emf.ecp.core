/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Muenchen - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.view.spi.provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.osgi.framework.Bundle;

public class ViewProviderHelper {

	private static Set<IViewProvider> viewProviders = new HashSet<IViewProvider>();

	public static Set<IViewProvider> getViewProviders() {
		if (viewProviders == null || viewProviders.isEmpty()) {
			readViewProviders();
		}
		return viewProviders;
	}

	private static void readViewProviders() {
		final IConfigurationElement[] controls = Platform.getExtensionRegistry()
			.getConfigurationElementsFor(
				"org.eclipse.emf.ecp.ui.view.viewModelProviders");
		for (final IConfigurationElement e : controls) {
			try {
				final String clazz = e.getAttribute("class");
				final Class<? extends IViewProvider> resolvedClass = loadClass(e
					.getContributor().getName(), clazz);
				final Constructor<? extends IViewProvider> controlConstructor = resolvedClass
					.getConstructor();
				final IViewProvider viewProvider = controlConstructor.newInstance();
				viewProviders.add(viewProvider);
			} catch (final ClassNotFoundException ex) {
				Activator.log(ex);
			} catch (final NoSuchMethodException ex) {
				Activator.log(ex);
			} catch (final SecurityException ex) {
				Activator.log(ex);
			} catch (final InstantiationException ex) {
				Activator.log(ex);
			} catch (final IllegalAccessException ex) {
				Activator.log(ex);
			} catch (final IllegalArgumentException ex) {
				Activator.log(ex);
			} catch (final InvocationTargetException ex) {
				Activator.log(ex);
			}

		}

	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz)
		throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			// TODO externalize strings
			throw new ClassNotFoundException(clazz
				+ " cannot be loaded because bundle " + bundleName //$NON-NLS-1$
				+ " cannot be resolved"); //$NON-NLS-1$
		}
		return (Class<T>) bundle.loadClass(clazz);

	}

	/**
	 * @return a view model for the given {@link EObject}
	 */
	public static VView getView(EObject eObject) {
		int highestPrio = IViewProvider.NOT_APPLICABLE;
		IViewProvider selectedProvider = null;
		for (final IViewProvider viewProvider : ViewProviderHelper.getViewProviders()) {
			final int prio = viewProvider.canRender(eObject);
			if (prio > highestPrio) {
				highestPrio = prio;
				selectedProvider = viewProvider;
			}
		}
		if (selectedProvider != null) {
			return selectedProvider.generate(eObject);
		}
		return null;

	}
}
