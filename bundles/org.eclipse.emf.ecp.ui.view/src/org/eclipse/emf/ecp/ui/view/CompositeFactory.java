package org.eclipse.emf.ecp.ui.view;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.ui.view.RendererContext.ValidationListener;
import org.eclipse.swt.widgets.Composite;


public interface CompositeFactory extends ValidationListener {

    // CompositeFactory INSTANCE=CompositeFactoryImpl.INSTANCE;

    Composite getComposite(Composite composite, 
    		org.eclipse.emf.ecp.view.model.Composite modelComposite,
            EObject eObject, 
            ECPControlContext context);

    void dispose();
    // void updateLiveValidation(EObject validationEObject);
}
