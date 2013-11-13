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
package org.eclipse.emf.ecp.internal.ui.view.builders;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

public class NodeBuilders {

	public static final NodeBuilders INSTANCE = new NodeBuilders();

	private Map<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, NodeBuilder<? extends VElement>> builders;
	private boolean buildersInitialized;

	private NodeBuilders() {
	}

	@SuppressWarnings({ "serial" })
	private void initBuilders() {

		builders = new LinkedHashMap<Class<? extends org.eclipse.emf.ecp.view.model.VElement>, NodeBuilder<? extends VElement>>() {
			{
				// put(ColumnComposite.class,
				// new CompositeCollectionNodeBuilder<ColumnComposite>());
				// put(Column.class, new CompositeCollectionNodeBuilder<Column>());
				// put(Group.class, new CompositeCollectionNodeBuilder<Group>());
				put(VControl.class, new RenderableNodeBuilder<VControl>());
				put(VView.class, new ViewNodeBuilder());
				// put(VCategory.class, new CategoryNodeBuilder());
				// put(VCategorization.class, new CategorizationNodeBuilder());
				// put(TableControl.class, new ControlNodeBuilder<TableControl>());
				put(VControl.class, new ControlNodeBuilder<VControl>());
				// put(TreeCategory.class, new TreeCategoryNodeBuilder());
			}
		};

		for (final CustomNodeBuilder customBuilder : getCustomNodeBuilders()) {
			for (final Map.Entry<Class<? extends VElement>, NodeBuilder<? extends VElement>> entry : customBuilder
				.getCustomNodeBuilders().entrySet()) {
				builders.put(entry.getKey(), entry.getValue());
			}
		}

		buildersInitialized = true;
	}

	public Set<CustomNodeBuilder> getCustomNodeBuilders() {
		final Set<CustomNodeBuilder> builders = new LinkedHashSet<CustomNodeBuilder>();
		final IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
			.getExtensionPoint(
				"org.eclipse.emf.ecp.ui.view.customNodeBuilders");
		for (final IExtension extension : extensionPoint.getExtensions()) {
			final IConfigurationElement configurationElement = extension
				.getConfigurationElements()[0];
			try {
				final CustomNodeBuilder renderer = (CustomNodeBuilder) configurationElement
					.createExecutableExtension("class");
				builders.add(renderer);
			} catch (final CoreException ex) {
				Activator.log(ex);
			}
		}

		return builders;
	}

	public <R extends VElement> Node<R> build(R renderable,
		ECPControlContext controlContext) {

		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);

		final Node<R> build = build(renderable, controlContext,
			adapterFactoryItemDelegator);
		composedAdapterFactory.dispose();
		return build;
	}

	public <R extends VElement> Node<R> build(R renderable,
		ECPControlContext controlContext,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {

		if (!buildersInitialized) {
			initBuilders();
		}

		Class<?> c = null;
		for (final Class<? extends VElement> cls : builders.keySet()) {
			final Class<?>[] interfaces = renderable.getClass().getInterfaces();
			final int indexOf = Arrays.asList(interfaces).indexOf(cls);
			if (indexOf != -1) {
				c = interfaces[indexOf];
				break;
			}
		}

		if (c != null) {
			@SuppressWarnings("unchecked")
			final NodeBuilder<R> builder = (NodeBuilder<R>) builders.get(c);
			return builder.build(renderable, controlContext,
				adapterFactoryItemDelegator);
		}

		return null;
	}
}
