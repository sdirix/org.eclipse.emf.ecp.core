package org.eclipse.emf.ecp.internal.ui.view.renderer;

public class ModelRendererFactoryImpl implements ModelRendererFactory {

	public <C> ModelRenderer<C> getRenderer(Object[] initData) {
		// FIXME: shortcut
		ModelRenderer<C> renderer = new ModelRendererImpl<C>(initData);
		renderer.initialize(initData);
		return renderer;
	}

}
