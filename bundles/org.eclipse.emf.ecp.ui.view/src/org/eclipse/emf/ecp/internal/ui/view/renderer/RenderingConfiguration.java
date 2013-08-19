/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * @author Eugen Neufeld
 * 
 */
public final class RenderingConfiguration {

	private static final String CLASS_ATTRIBUTE = "class";//$NON-NLS-1$

	private static RenderingConfiguration INSTANCE = new RenderingConfiguration();
	private final Map<Class<?>, RenderingResultRowFactory<?>> rowFactories = new LinkedHashMap<Class<?>, RenderingResultRowFactory<?>>();
	private final Map<Class<?>, LayoutHelper<?>> layoutHelpers = new LinkedHashMap<Class<?>, LayoutHelper<?>>();

	public static RenderingConfiguration getCurrent() {
		return INSTANCE;
	}

	/**
	 * 
	 */
	private RenderingConfiguration() {
		INSTANCE = this;
		readExtensionPoint();
	}

	/**
	 * 
	 */
	private void readExtensionPoint() {
		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry == null) {
			return;
		}
		final IConfigurationElement[] controls = extensionRegistry.getConfigurationElementsFor(
			"org.eclipse.emf.ecp.ui.view.renderingConfiguration");
		for (final IConfigurationElement e : controls) {
			for (final IConfigurationElement e2 : e.getChildren()) {
				try {
					if ("renderingResultRowFactory".equalsIgnoreCase(e2.getName())) {
						final RenderingResultRowFactory<?> rrrf = (RenderingResultRowFactory<?>) e2
							.createExecutableExtension(CLASS_ATTRIBUTE);
						final Class<?> clazz = (Class<?>) ((ParameterizedType) rrrf.getClass().getGenericInterfaces()[0])
							.getActualTypeArguments()[0];
						rowFactories.put(clazz, rrrf);
					}
					else if ("renderingLayoutHelper".equalsIgnoreCase(e2.getName())) {
						final LayoutHelper<?> layoutHelper = (LayoutHelper<?>) e2
							.createExecutableExtension(CLASS_ATTRIBUTE);
						final Class<?> clazz = (Class<?>) ((ParameterizedType) layoutHelper.getClass()
							.getGenericInterfaces()[0]).getActualTypeArguments()[0];
						layoutHelpers.put(clazz, layoutHelper);
					}
				} catch (final CoreException ex) {
					// TODO Auto-generated catch block
					// Do NOT catch all Exceptions ("catch (Exception e)")
					// Log AND handle Exceptions if possible
					//
					// You can just uncomment one of the lines below to log an exception:
					// logException will show the logged excpetion to the user
					// ModelUtil.logException(ex);
					// ModelUtil.logException("YOUR MESSAGE HERE", ex);
					// logWarning will only add the message to the error log
					// ModelUtil.logWarning("YOUR MESSAGE HERE", ex);
					// ModelUtil.logWarning("YOUR MESSAGE HERE");
					//
					// If handling is not possible declare and rethrow Exception
				}
			}
		}

	}

	public <CONTROL> RenderingResultRowFactory<CONTROL> getRenderingRowFactory(Class<CONTROL> controlClazz) {
		return (RenderingResultRowFactory<CONTROL>) rowFactories.get(controlClazz);
	}

	public <LAYOUT> LayoutHelper<LAYOUT> getLayoutHelper(Class<LAYOUT> layoutClazz) {
		return (LayoutHelper<LAYOUT>) layoutHelpers.get(layoutClazz);
	}
}
