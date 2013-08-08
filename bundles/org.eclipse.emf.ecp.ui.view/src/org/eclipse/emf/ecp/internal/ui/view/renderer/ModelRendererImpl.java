/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

public class ModelRendererImpl<C> implements ModelRenderer<C> {

	public ModelRendererImpl() {
	}

	public <R extends Renderable> RendererContext<C> render(Node<R> node,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final ControlRenderer<R, C> renderer = getControlRenderer();

		if (renderer == null) {
			throw new IllegalStateException("Renderer not initialized!");
		}

		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);

		final RendererContext<C> rendererContext = new RendererContext<C>(node, node.getControlContext());

		final List<RenderingResultRow<C>> rowResults = renderer.render(node, adapterFactoryItemDelegator,
			initData);
		rendererContext.setRenderedResult(rowResults.get(0).getMainControl());
		composedAdapterFactory.dispose();

		return rendererContext;
	}

	private <R extends Renderable> ControlRenderer<R, C> getControlRenderer() {
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
			"org.eclipse.emf.ecp.ui.view.renderer");
		for (final IExtension extension : extensionPoint.getExtensions()) {
			final IConfigurationElement configurationElement = extension.getConfigurationElements()[0];
			try {
				@SuppressWarnings("unchecked")
				final ControlRenderer<R, C> renderer = (ControlRenderer<R, C>) configurationElement
					.createExecutableExtension("class");
				return renderer;
			} catch (final CoreException ex) {
				Activator.log(ex);
			}
		}

		// TODO: provide default renderer?
		return null;
	}

}
