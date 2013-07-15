package org.eclipse.emf.ecp.internal.ui.view.renderer;


public interface ModelRendererFactory {

	public <C> ModelRenderer<C> getRenderer(Object[] initData);

}
