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
import org.eclipse.emf.ecp.internal.ui.view.ECPAction;
import org.eclipse.emf.ecp.ui.view.RendererContext.ValidationListener;
import org.eclipse.emf.ecp.view.model.Condition;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.EnableRule;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.LeafCondition;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.ShowRule;

/**
 * 
 * @author emueler
 *
 * @param <CONTROL>
 * 			the type of the actual control
 */
public class Node<T extends Renderable> implements ValidationListener {

	private T model;
	private List<Node<?>> children;
	private WithRenderedObject renderedObject;
    private boolean isVisible;
    private ECPControlContext controlContext;
	private Object labelObject;
	private List<ECPAction> actions;
	
	public Node(T model, ECPControlContext controlContext) {
		this.model = model;
		this.labelObject = model;
		this.controlContext = controlContext;
		this.children = new ArrayList<Node<?>>();
		isVisible = true;
	}
	
	public Node(T model, ECPControlContext context, boolean isVisible) {
		this(model, context);
        this.isVisible = isVisible;
	}
	
	public T getRenderable() {
		return model;
	}
	
	public void addChild(Node node) {
		children.add(node);
	}
	
	public List<Node<?>> getChildren() {
		return children;
	}

	public void checkShow(Notification notification, ECPControlContext controlContext) {
		
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
			for (Node child : getChildren()) {
				child.checkShow(notification, controlContext);
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
	
	public void checkEnable(Notification notification, ECPControlContext context) { 
						
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
					boolean v = ConditionEvaluator.evaluate(context.getModelElement(), condition);
					if (v == ((EnableRule) model.getRule()).isDisable()) {
						enableIsFalse();
					} else {
						enableIsTrue();
					}
				}
			}
			
		} else {
			for (Node child : getChildren()) {
				child.checkEnable(notification, context);				
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
	
	private void checkIsLifted() {
		if (renderedObject == null) {
			throw new IllegalStateException("Node hasn't been lifted!");
		}
	}

	public void enableIsTrue() {
		renderedObject.enableIsTrue();
	}
	
	public void enableIsFalse() {
		renderedObject.enableIsFalse();
	}
	
	public void showIsFalse() {
		renderedObject.showIsFalse();
	}
	
	public void showIsTrue() {
		renderedObject.showIsTrue();
	}
	
	public void layout() {
		renderedObject.layout();
	}
	
	public void cleanup() {
		renderedObject.cleanup();
	}
//	
//	public String getLabel() {
//		return "TODO";
//	}
//	
//	public Image getImage() {
//		throw new 
//	}
	
	@Override
	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
		for (Node child : getChildren()) {
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
		for (Node child : getChildren()) {
			visitor.executeOnNode(child);
		}
	}

	public void lift(WithRenderedObject rendered) {
		renderedObject = rendered;
	}

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

	public ECPControlContext getControlContext() {
		return controlContext;
	}

	public void setControlContext(ECPControlContext controlContext) {
		this.controlContext = controlContext;
	}

	public Object getLabelObject() {
		return labelObject;
	}
	
	public void setLabelObject(Object object) {
		labelObject = object;
	}

    /**
     * Actions of this node.
     * @return the actions
     */
    public List<ECPAction> getActions() {
        return actions;
    }

    /**
     * Sets the available actions on this node.
     * @param actions the actions to set
     */
    public void setActions(List<ECPAction> actions) {
        this.actions = actions;
    }
	
}
