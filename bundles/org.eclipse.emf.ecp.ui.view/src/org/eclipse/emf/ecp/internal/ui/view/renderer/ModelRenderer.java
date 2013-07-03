package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.Renderable;

public interface ModelRenderer<T, U extends Renderable> extends ControlRenderer<T, U> {

	ModelRendererFactory INSTANCE = new ModelRendererFactoryImpl();
	
    RendererContext<T> render(U renderable, ECPControlContext context) throws NoRendererFoundException;
    
}
