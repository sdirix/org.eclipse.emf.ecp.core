package org.eclipse.emf.ecp.internal.ui.view;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.view.model.AndCondition;
import org.eclipse.emf.ecp.view.model.Condition;
import org.eclipse.emf.ecp.view.model.LeafCondition;
import org.eclipse.emf.ecp.view.model.OrCondition;

public final class ConditionEvaluator  {
    
    private EObject article;
    private Condition condition;
    private IConditionEvalResult callback;
    private Set<EContentAdapter> adapters;
    
    public ConditionEvaluator(EObject article, Condition condition) { 
        this.article = article;
        this.condition = condition;
    }
    
    public ConditionEvaluator(EObject article, Condition condition, IConditionEvalResult callback) {
        this.article = article;
        this.condition = condition;
        this.callback = callback;
        this.adapters = new LinkedHashSet<EContentAdapter>();
        boolean valid = evaluate(condition);
        callback.evalFinished(valid);
    }
    
    public boolean evaluate() {
        return evaluate(condition);
    }

    private boolean evaluate(Condition condition) {
        
        if (AndCondition.class.isInstance(condition)) {
            return doEvaluate((AndCondition) condition);
        }
        if (OrCondition.class.isInstance(condition)) {
            return doEvaluate((OrCondition) condition);
        }
        if (LeafCondition.class.isInstance(condition)) {
            return doEvaluate((LeafCondition) condition);
        }
        return false;
    }

    private boolean doEvaluate(AndCondition condition) {
        boolean result = true;
        for (Condition innerCondition : condition.getConditions()) {
            result &= evaluate(innerCondition);
        }
        return result;
    }

    private boolean doEvaluate(OrCondition condition) {
        boolean result = false;
        for (Condition innerCondition : condition.getConditions()) {
            result |= evaluate(innerCondition);
        }
        return result;
    }

    private boolean doEvaluate(LeafCondition condition) {
        EClass attributeClass = condition.getAttribute().getEContainingClass();
        EObject parent = article;
        List<EReference> referencePath = condition.getPathToAttribute();
        for (EReference eReference : referencePath) {
            if (eReference.getEReferenceType().isInstance(parent)) {
                break;
            }
            EObject child = (EObject) parent.eGet(eReference);
            //if the article is not fully filled
            if(child==null){
                break;
            }
            parent = child;
        }
        if (!attributeClass.isInstance(parent))
            return false;
        
        createAdapter(condition);
        
        return parent.eGet(condition.getAttribute()).equals(condition.getExpectedValue());

        // for(EReference containmentEReference:article.eClass().getEAllContainments()){
        // if(containmentEReference.isMany()){
        // List<EObject> objects=(List<EObject>)article.eGet(containmentEReference);
        // for(EObject object:objects){
        // if(attributeClass.isInstance(object)){
        // return
        // object.eGet(condition.getAttribute()).toString().equals(condition.getExpectedValue().toString());
        // }
        // }
        // }
        // else{
        // EObject eObject=(EObject) article.eGet(containmentEReference);
        // if(attributeClass.isInstance(eObject)){
        // return
        // eObject.eGet(condition.getAttribute()).toString().equals(condition.getExpectedValue().toString());
        // }
        // }
        //
        // }
        // for(MpiDataSegmentType dataSegmentType:article.getMpiDataSegment()){
        // if(attributeClass.isInstance(dataSegmentType)){
        // //TODO wrong way
        // return
        // dataSegmentType.eGet(condition.getAttribute()).toString().equals(condition.getExpectedValue().toString());
        // }
        // }
        // return false;

    }
    
    public void dispose() {
        for (EContentAdapter adapter : adapters) {
            article.eAdapters().remove(adapter);
        }
    }

    private void createAdapter(final LeafCondition condition) {
        
        if (callback == null) {
            return;
        }
        
        EContentAdapter eContentAdapter = new EContentAdapter() {
            @Override
            public void notifyChanged(Notification notification) {
                Object notifier = notification.getNotifier();
                Object feature = notification.getFeature();

                if (feature instanceof EAttribute && notifier instanceof EObject) {
                    EAttribute attribute = (EAttribute) feature;
                    if (condition.getAttribute().equals(attribute)) {
                        boolean valid = evaluate(condition);
                        callback.evalFinished(valid);
                    }
                }
            }
        };
        
        article.eAdapters().add(eContentAdapter);
        adapters.add(eContentAdapter);
    }
}
