package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.swt.widgets.Composite;

public interface ModelRenderer {

	ModelRendererFactory INSTANCE = new ModelRendererFactoryImpl();
	
    RendererContext render(View view, ECPControlContext context);
}
