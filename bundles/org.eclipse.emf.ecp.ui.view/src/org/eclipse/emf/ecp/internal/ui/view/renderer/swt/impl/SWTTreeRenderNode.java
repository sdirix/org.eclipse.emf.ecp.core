package org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.ConditionEvaluator;
import org.eclipse.emf.ecp.internal.ui.view.IConditionEvalResult;
import org.eclipse.emf.ecp.ui.view.RendererContext.ValidationListener;
import org.eclipse.emf.ecp.view.model.Condition;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.EnableRule;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.ShowRule;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class SWTTreeRenderNode implements ValidationListener {

	private org.eclipse.swt.widgets.Control result;
	private org.eclipse.emf.ecp.view.model.Composite model;
	private List<SWTTreeRenderNode> children;
	private ECPControlContext controlContext;
	private ConditionEvaluator showEvaluator;
	private ConditionEvaluator enablementEvaluator;
	
	public SWTTreeRenderNode(org.eclipse.swt.widgets.Control result, 
			org.eclipse.emf.ecp.view.model.Composite model, 
			ECPControlContext controlContext) {
		
		this.result = result;
		this.model = model;
		this.controlContext = controlContext;
		this.children = new ArrayList<SWTTreeRenderNode>();
	}
	
	public void checkShow() {
		 if (ShowRule.class.isInstance(model.getRule())) {    
	            Condition condition = model.getRule().getCondition();
	            if (showEvaluator == null) {
	                showEvaluator = new ConditionEvaluator(controlContext.getModelElement(), condition, new IConditionEvalResult() {
	                    @Override
	                    public void evalFinished(boolean v) {
	                        if (v == ((ShowRule) model.getRule()).isHide()) {
//	                            for (org.eclipse.swt.widgets.Control control : result) {
	                                if (result != null && result instanceof Composite) {
	                                	Composite composite = (Composite) result;
	                                    GridData gridData = (GridData) composite.getLayoutData();
	                                    if (gridData != null) {
	                                        gridData.exclude = true;
	                                    }
	                                    composite.setVisible(false);
	                                    
	                                    TreeIterator<EObject> eAllContents = model.eAllContents();
	                                    while (eAllContents.hasNext()) {
	                                        EObject o = eAllContents.next();
	                                        unset(controlContext, o);
	                                    }
	                                    unset(controlContext, model);
	                                }
//	                            }
	                        } else {
//	                            for (org.eclipse.swt.widgets.Control control : result) {
	                                if (result != null) {
	                                    GridData gridData = (GridData) result.getLayoutData();
	                                    if (gridData != null) {
	                                        gridData.exclude = false;
	                                    }
	                                    result.setVisible(true);
	                                } 
//	                            }
	                        }
	                        
//	                        for (org.eclipse.swt.widgets.Control control : result) {
	                            if (result != null) {
	                                Composite parent = result.getParent();
	                                parent.layout(true, true);
//	                                break;
	                            }
//	                        }
	                        
	                    }
	                });
	            }
	        }
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
	
	public void checkEnable() {
		if (EnableRule.class.isInstance(model.getRule())) {
			Condition condition = model.getRule().getCondition();
			if (enablementEvaluator == null) {
				enablementEvaluator = new ConditionEvaluator(controlContext.getModelElement(), condition,
						new IConditionEvalResult() {
					@Override
					public void evalFinished(boolean v) {
						if (v == ((EnableRule) model.getRule()).isDisable()) {
							result.setEnabled(false);
						} else {
							result.setEnabled(true);
						}
					}
				});
			}
		}
		for (SWTTreeRenderNode child : getChildren()) {
			child.checkEnable();
		}
	}

	
	public org.eclipse.swt.widgets.Control getRenderedResult() {
		return result;
	}
	
	public org.eclipse.emf.ecp.view.model.Composite getModel() {
		return model;
	}
		public void addChild(SWTTreeRenderNode node) {
		children.add(node);
	}
	
	public List<SWTTreeRenderNode> getChildren() {
		return children;
	}

	@Override
	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
		for (SWTTreeRenderNode child : getChildren()) {
			child.validationChanged(affectedObjects);
		}
	}
	
	public boolean isLeaf() {
		return false;
	}

	public void dispose() {
		enablementEvaluator.dispose();
		showEvaluator.dispose();
		result.dispose();
	}
	
	public void execute(SWTTreeRendererNodeVisitor visitor) {
		visitor.executeOnNode(this);
		for (SWTTreeRenderNode child : getChildren()) {
			visitor.executeOnNode(child);
		}
	}

}
