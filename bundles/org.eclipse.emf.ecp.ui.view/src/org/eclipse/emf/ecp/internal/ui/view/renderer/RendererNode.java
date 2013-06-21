package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import org.eclipse.emf.ecp.view.model.ShowRule;

public abstract class RendererNode<T> implements ValidationListener {

	private T result;
	private org.eclipse.emf.ecp.view.model.Composite model;
	
	private List<RendererNode<T>> children;
	private ECPControlContext controlContext;
	
	public RendererNode(T result, 
			org.eclipse.emf.ecp.view.model.Composite model, 
			ECPControlContext controlContext) {
		
		this.result = result;
		this.model = model;
		this.controlContext = controlContext;
		this.children = new ArrayList<RendererNode<T>>();
	}
	
	public void checkShow(Notification notification) {
		
		if (isLeafCondition()) {
			Condition condition = model.getRule().getCondition();
			if (!ShowRule.class.isInstance(model.getRule())) {
				return;
			}
			LeafCondition leaf = (LeafCondition) condition;
			EAttribute attr = null;
			
			if (isEAttributeNotification(notification)) { 
				attr = (EAttribute) notification.getFeature();
				if (leaf.getAttribute().getFeatureID() == attr.getFeatureID()) {
					boolean v = ConditionEvaluator.evaluate(controlContext.getModelElement(), condition);
					if (v == ((ShowRule) model.getRule()).isHide()) {
						showIsTrue();
						TreeIterator<EObject> eAllContents = model.eAllContents();
						while (eAllContents.hasNext()) {
							EObject o = eAllContents.next();
							unset(controlContext, o);
						}
						unset(controlContext, model);
					} else {
						showIsFalse();
					}
					
					layout();
				}
			}
			
		} else {
			for (RendererNode child : getChildren()) {
				child.checkShow(notification);
			}
		}
		
//		if (ShowRule.class.isInstance(model.getRule())) {    
//			Condition condition = model.getRule().getCondition();
//			boolean v = ConditionEvaluator.evaluate(controlContext.getModelElement(), condition);
//			if (v == ((ShowRule) model.getRule()).isHide()) {
//				if (result != null && result instanceof Composite) {
//
//					showIsTrue();
//
//					TreeIterator<EObject> eAllContents = model.eAllContents();
//					while (eAllContents.hasNext()) {
//						EObject o = eAllContents.next();
//						unset(controlContext, o);
//					}
//					unset(controlContext, model);
//				}
//			} else {
//				if (result != null) {
//					showIsFalse();
//				} 
//			}
//
//			layout();
//		}
	}
	
    
    private void unset(ECPControlContext context, EObject eObject) {
        if (eObject instanceof Control) {
            Control control = (Control) eObject;
            ECPControlContext editContext = context.createSubContext(eObject);
            EStructuralFeature targetFeature = control.getTargetFeature();
            if (targetFeature.isMany()) {
                Collection<?> collection = (Collection<?>) editContext.getModelElement().eGet(targetFeature);
                collection.clear();
            } else {
                editContext.getModelElement().eSet(targetFeature, null);
            }
        } else if (eObject instanceof Group) {   
            Group group = (Group) eObject;
            EList<org.eclipse.emf.ecp.view.model.Composite> composites = group.getComposites();
            for (org.eclipse.emf.ecp.view.model.Composite composite : composites) {
                unset(context, composite);
            }
        }
    }
	
	public void checkEnable(Notification notification) { 
						
		if (isLeafCondition()) {
			Condition condition = model.getRule().getCondition();
			if (!EnableRule.class.isInstance(model.getRule())) {
				return;
			}
			
			LeafCondition leaf = (LeafCondition) condition;
			EAttribute attr = null;
			
			if (isEAttributeNotification(notification)) { 
				attr = (EAttribute) notification.getFeature();
				if (leaf.getAttribute().getFeatureID() == attr.getFeatureID()) {
					boolean v = ConditionEvaluator.evaluate(controlContext.getModelElement(), condition);
					if (v == ((EnableRule) model.getRule()).isDisable()) {
						enableIsFalse();
					} else {
						enableIsTrue();
					}
				}
			}
			
		} else {
			for (RendererNode child : getChildren()) {
				child.checkEnable(notification);
			}
		}
	}
	
	private boolean isLeafCondition() {
		if (model.getRule() == null) {
			return false;
		}
		
		return  model.getRule().getCondition() instanceof LeafCondition;
	}
	
	private boolean isEAttributeNotification(Notification notification) {
		if (notification.getFeature() instanceof EAttribute) {
			return true;
		}
		
		return false;
	}

	public abstract void enableIsTrue();
	public abstract void enableIsFalse();
	public abstract void showIsFalse();
	public abstract void showIsTrue();
	public abstract void layout();
	public abstract void cleanup();
	
	public T getRenderedResult() {
		return result;
	}
	
	public org.eclipse.emf.ecp.view.model.Composite getModel() {
		return model;
	}
		public void addChild(RendererNode<T> node) {
		children.add(node);
	}
	
	public List<RendererNode<T>> getChildren() {
		return children;
	}

	@Override
	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
		for (RendererNode child : getChildren()) {
			child.validationChanged(affectedObjects);
		}
	}
	
	public boolean isLeaf() {
		return false;
	}

	public void dispose() {
		cleanup();
	}
	
	public void execute(TreeRendererNodeVisitor visitor) {
		visitor.executeOnNode(this);
		for (RendererNode child : getChildren()) {
			visitor.executeOnNode(child);
		}
	}

}
