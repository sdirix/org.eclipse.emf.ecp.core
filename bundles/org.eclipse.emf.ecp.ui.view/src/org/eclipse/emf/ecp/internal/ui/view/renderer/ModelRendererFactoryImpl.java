package org.eclipse.emf.ecp.internal.ui.view.renderer;

public class ModelRendererFactoryImpl implements ModelRendererFactory {

	@Override
	public ModelRenderer getRenderer(Object[] initData) {
		// FIXME: shortcut
		ModelRenderer renderer = new ModelRendererImpl(initData);
		return renderer;
	}

}
