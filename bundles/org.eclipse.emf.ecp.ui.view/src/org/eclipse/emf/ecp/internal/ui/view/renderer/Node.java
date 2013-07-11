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
import org.eclipse.emf.ecore.EReference;
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
 * @author emueller
 *
 * @param <CONTROL>
 * 			the type of the actual control
 */
// FIXME: 
public class Node<T extends Renderable> implements ValidationListener {

	private T viewModelElement;
	private List<Node<?>> children;
	protected RenderingResultDelegator renderingResultDelegator;
	private ECPControlContext controlContext;
	private boolean isVisible;
	private List<ECPAction> actions;
	private Object labelObject;
	private int severity;
	private ValidationOccurredListener callback;
    private List<SelectedChildNodeListener> selectedChildNodeListeners;
	
	public Node(T model, ECPControlContext controlContext) {
		this.viewModelElement = model;
		this.labelObject = model;
		this.controlContext = controlContext;
		this.children = new ArrayList<Node<?>>();
		this.selectedChildNodeListeners = new ArrayList<SelectedChildNodeListener>();
		isVisible = true;
	}
	
	public Node(T model, ECPControlContext context, boolean isVisible) {
		this(model, context);
        this.isVisible = isVisible;
	}
	
	public T getRenderable() {
		return viewModelElement;
	}
	
	public void addChild(Node<T> node) {
		children.add(node);
	}
	
	public List<Node<?>> getChildren() {
		return children;
	}

	// FIXME: change type of notification to attribute
	public void checkShow(Notification notification, ECPControlContext controlContext) {
		
		if (isLeafCondition()) {
			Condition condition = viewModelElement.getRule().getCondition();
			if (!ShowRule.class.isInstance(viewModelElement.getRule())) {
				return;
			}
			LeafCondition leaf = (LeafCondition) condition;
			EAttribute attr = null;
			
			if (isEAttributeNotification(notification)) { 
				attr = (EAttribute) notification.getFeature();
				if (leaf.getAttribute().getFeatureID() == attr.getFeatureID()) {
					boolean v = ConditionEvaluator.evaluate(controlContext.getModelElement(), condition);
					if (v == ((ShowRule) viewModelElement.getRule()).isHide()) {
					    show(false);
						TreeIterator<EObject> eAllContents = viewModelElement.eAllContents();
						while (eAllContents.hasNext()) {
							EObject o = eAllContents.next();
							unset(controlContext, o);
						}
						unset(controlContext, viewModelElement);
					} else {
					    show(true);
					}
					
					layout();
				}
			}
			
		} else {
			for (Node<?> child : getChildren()) {
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
            EObject parent=context.getModelElement();
            for(EReference eReference:control.getPathToFeature()){
                parent=(EObject) parent.eGet(eReference);
            }
            ECPControlContext editContext = context.createSubContext(parent);
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
	
    // FIXME: change type of notification to attribute
	public void checkEnable(Notification notification, ECPControlContext context) { 
						
		if (isLeafCondition()) {
		    // FIXME: MKVMR
			Condition condition = viewModelElement.getRule().getCondition();
			if (!EnableRule.class.isInstance(viewModelElement.getRule())) {
				return;
			}
			
			LeafCondition leaf = (LeafCondition) condition;
			EAttribute attr = null;
			
			if (isEAttributeNotification(notification)) { 
				attr = (EAttribute) notification.getFeature();
				if (leaf.getAttribute().getFeatureID() == attr.getFeatureID()) {
					boolean v = ConditionEvaluator.evaluate(context.getModelElement(), condition);
					if (v == ((EnableRule) viewModelElement.getRule()).isDisable()) {
						enable(false);
					} else {
						enable(true);
					}
				}
			}
			
		} else {
			for (Node<?> child : getChildren()) {
				child.checkEnable(notification, context);				
			}
		}
	}
	
	private boolean isLeafCondition() {
		if (viewModelElement.getRule() == null) {
			return false;
		}
		
		return  viewModelElement.getRule().getCondition() instanceof LeafCondition;
	}
	
	private boolean isEAttributeNotification(Notification notification) {
		if (notification.getFeature() instanceof EAttribute) {
			return true;
		}
		
		return false;
	}
	
	// TODO: how should we behave in case there is no renderable?
	private void checkIsLifted() {
		if (renderingResultDelegator == null) {
			throw new IllegalStateException("Node hasn't been lifted!");
		}
	}

	public void enable(boolean value) {
	    if(renderingResultDelegator!=null) {
	        renderingResultDelegator.enable(value);
	    } 
	}
	
	public void show(boolean value) {
	    if(renderingResultDelegator!=null) {
	        if (value) {
	            setVisible(true);
	            renderingResultDelegator.show(true);
	        } else {
	            setVisible(false);
	            renderingResultDelegator.show(false);
	        }
	    } 

	}
	
	public void layout() {
	    if(renderingResultDelegator!=null)
		renderingResultDelegator.layout();
	}
	
	public void cleanup() {
	    if(renderingResultDelegator!=null) {
	        renderingResultDelegator.cleanup();
	    }
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
	    
	    int max = Diagnostic.OK;
	    if (affectedObjects.containsKey(viewModelElement)) { 
            for (Diagnostic diagnostic : affectedObjects.get(viewModelElement)) {
                if(diagnostic.getSeverity() > max){
                    max = diagnostic.getSeverity();
                }
            }
        }	    
	    
	    severity = max;
	    
		for (Node<?> child : getChildren()) {
			child.validationChanged(affectedObjects);
		}
		
		if (callback != null) {
		    callback.validationChanged(affectedObjects);
		}
	}
	
	
	public boolean isLeaf() {
		return false;
	}

	public void dispose() {
		cleanup();
		selectedChildNodeListeners.clear();
		renderingResultDelegator = null;
		callback = null;
	}
	
	public void execute(TreeRendererNodeVisitor visitor) {
		visitor.executeOnNode(this);
		for (Node<?> child : getChildren()) {
			visitor.executeOnNode(child);
		}
	}

	public void lift(RenderingResultDelegator rendered) {
		renderingResultDelegator = rendered;
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
	
    public boolean isLifted() {
        return renderingResultDelegator != null;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public ValidationOccurredListener getCallback() {
        return callback;
    }

    public void setCallback(ValidationOccurredListener callback) {
        this.callback = callback;
    }
    
    public void addSelectedChildNodeListener(SelectedChildNodeListener listener) {
        selectedChildNodeListeners.add(listener);
    }
    
    public void removeSelectedChildNodeListener(SelectedChildNodeListener listener) {
        selectedChildNodeListeners.add(listener);
    }
    
    public void fireSelectedChildNodeChanged(Node<?> selectedChild) {
        for (SelectedChildNodeListener listener : selectedChildNodeListeners) {
            listener.childSelected(selectedChild);
        }
    }
}
