package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.view.model.Control;

public abstract class AbstractSWTControlRenderer<C extends Control> extends AbstractSWTRenderer<C> {

	protected ECPControlContext createSubcontext(Control modelControl,
			ECPControlContext controlContext) {
		EObject parent = controlContext.getModelElement();
        
        for (EReference eReference : modelControl.getPathToFeature()) {
            EObject child = (EObject) parent.eGet(eReference);
            if (child == null) {
                child = EcoreUtil.create(eReference.getEReferenceType());
                parent.eSet(eReference, child);
            }
            parent = child;
        }
        ECPControlContext subContext = controlContext.createSubContext(parent);
		return subContext;
	}
	
	protected abstract SWTControl getControl();
	
	
}
