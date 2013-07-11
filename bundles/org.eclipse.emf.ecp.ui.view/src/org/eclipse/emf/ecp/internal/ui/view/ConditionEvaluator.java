package org.eclipse.emf.ecp.internal.ui.view;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.view.model.AndCondition;
import org.eclipse.emf.ecp.view.model.Condition;
import org.eclipse.emf.ecp.view.model.LeafCondition;
import org.eclipse.emf.ecp.view.model.OrCondition;

public final class ConditionEvaluator  {
    
    private ConditionEvaluator() { 
     
    }

    public static boolean evaluate(EObject eObject, Condition condition) {
        
        if (AndCondition.class.isInstance(condition)) {
            return doEvaluate(eObject, (AndCondition) condition);
        }
        if (OrCondition.class.isInstance(condition)) {
            return doEvaluate(eObject, (OrCondition) condition);
        }
        if (LeafCondition.class.isInstance(condition)) {
            return doEvaluate(eObject, (LeafCondition) condition);
        }
        return false;
    }

    private static boolean doEvaluate(EObject eObject, AndCondition condition) {
        boolean result = true;
        for (Condition innerCondition : condition.getConditions()) {
            result &= evaluate(eObject, innerCondition);
        }
        return result;
    }

    private static boolean doEvaluate(EObject eObject, OrCondition condition) {
        boolean result = false;
        for (Condition innerCondition : condition.getConditions()) {
            result |= evaluate(eObject, innerCondition);
        }
        return result;
    }

    private static boolean doEvaluate(EObject eObject, LeafCondition condition) {
        EClass attributeClass = condition.getAttribute().getEContainingClass();
        EObject parent = eObject;
        List<EReference> referencePath = condition.getPathToAttribute();
        for (EReference eReference : referencePath) {
            if (eReference.getEReferenceType().isInstance(parent)) {
                break;
            }
            EObject child = (EObject) parent.eGet(eReference);
            if(child==null){
                break;
            }
            parent = child;
        }
        if (!attributeClass.isInstance(parent))
            return false;
                
        return condition.getExpectedValue().equals(parent.eGet(condition.getAttribute()));
    }
    
}
