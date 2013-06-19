package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

public class ModelRendererImpl implements ModelRenderer {
	
	private ControlRenderer renderer;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	
	public ModelRendererImpl(Object[] initData) {
		renderer = getControlRenderer(initData);
	}

	@Override
	public RendererContext render(View view, ECPControlContext context) {
		
	 	composedAdapterFactory = new ComposedAdapterFactory(
    			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
    	adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
		
		final RendererContext rendererContext = new RendererContext(view, context.getModelElement());
		Category category = (Category) view.getCategorizations().get(0);
		
		if (renderer == null) {
			throw new NullPointerException("Renderer not initialized!");
		}
		
		RendererNode render = renderer.render(category.getComposite(), context, adapterFactoryItemDelegator);
		render.checkShow();
		render.checkEnable();
		rendererContext.setNode(render);
	
		return rendererContext;
	}

	private ControlRenderer getControlRenderer(Object[] initData) {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
				"org.eclipse.emf.ecp.ui.view.renderer");
		for (IExtension extension : extensionPoint.getExtensions()) {
			IConfigurationElement configurationElement = extension.getConfigurationElements()[0];
			try {
				ControlRenderer renderer = (ControlRenderer) configurationElement.createExecutableExtension("class");
				renderer.initialize(initData);
				return renderer;
			} catch (CoreException ex) {
				Activator.log(ex);
			}
		}
		// TODO: provide default renderer?
		return null;
	}
}
