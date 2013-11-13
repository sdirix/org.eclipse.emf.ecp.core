/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.internal;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Composite;

public final class SWTRenderers implements SWTRenderer<VElement> {

	public static final SWTRenderers INSTANCE = new SWTRenderers();

	@SuppressWarnings("rawtypes")
	private final Map<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, SWTRenderer> renderers;

	public SWTRenderers() {

		renderers = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, SWTRenderer>();
		// renderers.put(ColumnComposite.class, SWTColumnCompositeRenderer.INSTANCE);
		// renderers.put(Column.class, SWTColumnRenderer.INSTANCE);
		// put(Group.class, SWTGroupRenderer.INSTANCE);
		// put(TableControl.class, SWTTableControlRenderer.INSTANCE);
		renderers.put(VControl.class, SWTControlRenderer.INSTANCE);
		// renderers.put(VCategorization.class, SWTCategorizationRenderer.INSTANCE);
		// renderers.put(VCategory.class, SWTCategoryRenderer.INSTANCE);
		renderers.put(VView.class, SWTViewRenderer.INSTANCE);

		for (final CustomSWTRenderer customRenderer : getCustomRenderers()) {
			for (final Map.Entry<Class<? extends VElement>, SWTRenderer<?>> renderEntry : customRenderer
				.getCustomRenderers().entrySet()) {
				renderers.put(renderEntry.getKey(), renderEntry.getValue());
			}
		}

	}

	// public SWTLifted render(
	// Composite parent,
	// final org.eclipse.emf.ecp.view.model.Renderable renderable,
	// ECPControlContext controlContext,
	// AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException,
	// NoPropertyDescriptorFoundExeption {
	// currentParent = parent;
	// return render(renderable, controlContext, adapterFactoryItemDelegator);
	// }
	//
	// public SWTLifted render(
	// final org.eclipse.emf.ecp.view.model.Renderable renderable,
	// ECPControlContext controlContext,
	// AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException,
	// NoPropertyDescriptorFoundExeption {
	//
	// if (!renderersInitialized) {
	// initRenderers();
	// }
	//
	// Class c = null;
	// for (Class cls : renderers.keySet()) {
	// Class<?>[] interfaces = renderable.getClass().getInterfaces();
	// int indexOf = Arrays.asList(interfaces).indexOf(cls);
	// if (indexOf != -1) {
	// c = interfaces[indexOf];
	// break;
	// }
	//
	// }
	//
	// if (c != null) {
	// @SuppressWarnings("rawtypes")
	// SWTRenderer swtRenderer = renderers.get(c);
	// swtRenderer.initialize(new Object[] {currentParent});
	// @SuppressWarnings("unchecked")
	// SWTLifted node = (SWTLifted) swtRenderer.render(
	// renderable, controlContext, adapterFactoryItemDelegator);
	// return node;
	// }
	//
	// throw new NoRendererFoundException("No renderer found for renderable " + renderable);
	// }

	public Set<CustomSWTRenderer> getCustomRenderers() {
		final Set<CustomSWTRenderer> renderers = new LinkedHashSet<CustomSWTRenderer>();
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
			"org.eclipse.emf.ecp.ui.view.swt.customSWTRenderers");
		for (final IExtension extension : extensionPoint.getExtensions()) {
			final IConfigurationElement configurationElement = extension.getConfigurationElements()[0];
			try {
				final CustomSWTRenderer renderer = (CustomSWTRenderer) configurationElement
					.createExecutableExtension("class");
				renderers.add(renderer);
			} catch (final CoreException ex) {
				Activator.log(ex);
			}
		}

		return renderers;
	}

	public List<RenderingResultRow<org.eclipse.swt.widgets.Control>> render(Node<VElement> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,

		Object... initData) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		Class c = null;
		for (final Class cls : renderers.keySet()) {
			final Class<?>[] interfaces = node.getRenderable().getClass().getInterfaces();
			final int indexOf = Arrays.asList(interfaces).indexOf(cls);
			if (indexOf != -1) {
				c = interfaces[indexOf];
				break;
			}

		}

		if (c != null) {
			@SuppressWarnings("rawtypes")
			final SWTRenderer swtRenderer = renderers.get(c);
			return swtRenderer.render(node, adapterFactoryItemDelegator, initData);
		}

		throw new NoRendererFoundException("No renderer found for renderable " + node.getRenderable());
	}

	public List<RenderingResultRow<org.eclipse.swt.widgets.Control>> render(Composite parent, Node node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator)
		throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		return render(node, adapterFactoryItemDelegator, parent);
	}
}
