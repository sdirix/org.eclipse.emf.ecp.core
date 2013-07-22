package org.eclipse.emf.ecp.internal.ui.view.renderer;

public class ModelRendererFactoryImpl implements ModelRendererFactory {

	public <C> ModelRenderer<C> getRenderer() {
		// FIXME: shortcut
		ModelRenderer<C> renderer = new ModelRendererImpl<C>();
		return renderer;
	}

}
