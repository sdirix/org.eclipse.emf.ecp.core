package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.Renderable;

public interface ModelRenderer<C> {

	ModelRendererFactory INSTANCE = new ModelRendererFactoryImpl();
	
    <R extends Renderable> RendererContext<C> render(Node<R> node,Object...initData) 
    		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption;
    
}
