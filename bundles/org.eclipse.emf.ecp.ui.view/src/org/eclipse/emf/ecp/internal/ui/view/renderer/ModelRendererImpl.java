package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

public class ModelRendererImpl<C> implements ModelRenderer<C> {

	public ModelRendererImpl() {
	}

	public <R extends Renderable> RendererContext<C> render(Node<R> node, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		ControlRenderer<R, C> renderer = getControlRenderer();

		if (renderer == null) {
			throw new IllegalStateException("Renderer not initialized!");
		}

		ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);

		final RendererContext<C> rendererContext = new RendererContext<C>(node, node.getControlContext());

		C control = renderer.render(node, adapterFactoryItemDelegator, initData);
		rendererContext.setRenderedResult(control);
		composedAdapterFactory.dispose();

		return rendererContext;
	}

	private <R extends Renderable> ControlRenderer<R, C> getControlRenderer() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
			"org.eclipse.emf.ecp.ui.view.renderer");
		for (IExtension extension : extensionPoint.getExtensions()) {
			IConfigurationElement configurationElement = extension.getConfigurationElements()[0];
			try {
				@SuppressWarnings("unchecked")
				ControlRenderer<R, C> renderer = (ControlRenderer<R, C>) configurationElement
					.createExecutableExtension("class");
				return renderer;
			} catch (CoreException ex) {
				Activator.log(ex);
			}
		}

		// TODO: provide default renderer?
		return null;
	}

}
