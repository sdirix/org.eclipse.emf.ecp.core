package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.ConditionEvaluator;
import org.eclipse.emf.ecp.ui.view.RendererContext.ValidationListener;
import org.eclipse.emf.ecp.view.model.Condition;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.EnableRule;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.LeafCondition;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.ShowRule;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author emueller
 *
 * @param <CONTROL>
 * 			the type of the actual control
 */
public class WithRenderedObjectAdapter implements WithRenderedObject{

    @Override
    public void enable(boolean shouldEnable) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void show(boolean shouldShow) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void layout() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
        // TODO Auto-generated method stub
        
    }
	


}
