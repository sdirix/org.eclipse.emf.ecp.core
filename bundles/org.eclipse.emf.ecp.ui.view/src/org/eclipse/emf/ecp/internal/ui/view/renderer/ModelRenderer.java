package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.Renderable;

public interface ModelRenderer<R extends Renderable, C> extends ControlRenderer<R, C> {

	ModelRendererFactory INSTANCE = new ModelRendererFactoryImpl();
	
    RendererContext<C> render(Node<R> node) 
    		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption;
    
}
