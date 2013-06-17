package org.eclipse.emf.ecp.internal.ui.view.renderer.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.ui.view.RendererContext;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.swt.widgets.Composite;

public interface ModelRenderer {

    // ModelRenderer INSTANCE=ModelRendererImpl.INSTANCE;
    // ModelRenderer INSTANCE=ModelRendererTreeImpl.INSTANCE;
	
    RendererContext render(Composite composite, View view, ECPControlContext context);
}
