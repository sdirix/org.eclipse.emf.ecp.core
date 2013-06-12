package org.eclipse.emf.ecp.ui.view;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.swt.widgets.Composite;

public interface ModelRenderer {

    // ModelRenderer INSTANCE=ModelRendererImpl.INSTANCE;
    // ModelRenderer INSTANCE=ModelRendererTreeImpl.INSTANCE;

    RendererContext render(Composite composite, View view, EObject eObject, ECPControlContext context);
}
