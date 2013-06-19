package org.eclipse.emf.ecp.ui.view.swt;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractSWTControlRenderer<T extends Control> extends AbstractSWTRenderer<T> {

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
