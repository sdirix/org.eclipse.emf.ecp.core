package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

public class ModelRendererImpl<T, U extends Renderable> implements ModelRenderer<T, U> {
	
	private ControlRenderer<T, U> renderer;
	
	public ModelRendererImpl(Object[] initData) {
		renderer = getControlRenderer(initData);
	}

	@Override
	public RendererContext<T> render(U renderable, ECPControlContext context) throws NoRendererFoundException {
		
		if (renderer == null) {
			throw new IllegalStateException("Renderer not initialized!");
		}
		
		ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
    			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
		
		final RendererContext<T> rendererContext = new RendererContext<T>(renderable, context.getModelElement());

		RendererNode<T> node = render(renderable, context, adapterFactoryItemDelegator);
		
		composedAdapterFactory.dispose();
		
		rendererContext.setNode(node);
	
		return rendererContext;
	}

	private ControlRenderer<T, U> getControlRenderer(Object[] initData) {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
				"org.eclipse.emf.ecp.ui.view.renderer");
		for (IExtension extension : extensionPoint.getExtensions()) {
			IConfigurationElement configurationElement = extension.getConfigurationElements()[0];
			try {
				@SuppressWarnings("unchecked") ControlRenderer<T, U> renderer = 
						(ControlRenderer<T, U>) configurationElement.createExecutableExtension("class");
				renderer.initialize(initData);
				return renderer;
			} catch (CoreException ex) {
				Activator.log(ex);
			}
		}
		
		// TODO: provide default renderer?
		return null;
	}

	@Override
	public RendererNode<T> render(U model,
			ECPControlContext controlContext,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException {
		return renderer.render(model, controlContext, adapterFactoryItemDelegator);
	}

	@Override
	public void initialize(Object[] initData) {
		// TODO Auto-generated method stub
		
	}

}
