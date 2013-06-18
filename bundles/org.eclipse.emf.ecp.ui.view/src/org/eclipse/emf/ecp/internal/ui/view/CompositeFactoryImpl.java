package org.eclipse.emf.ecp.internal.ui.view;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRendererRegistry;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTTreeRenderNode;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTTreeRendererNodeVisitor;
import org.eclipse.emf.ecp.ui.view.CompositeFactory;
import org.eclipse.emf.ecp.view.model.Condition;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.EnableRule;
import org.eclipse.emf.ecp.view.model.Seperator;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.widgets.Composite;

public class CompositeFactoryImpl implements CompositeFactory {

    // public static CompositeFactoryImpl INSTANCE = new CompositeFactoryImpl();
    // TODO dispose
	private static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant";
    private final ComposedAdapterFactory composedAdapterFactory;
    private final AdapterFactoryItemDelegator adapterFactoryItemDelegator;

    private final Map<Control, ECPControl> controlToControlMap = new HashMap<Control, ECPControl>();
    private Map<Condition, ConditionEvaluator> evaluators;

    // RAP theming custom variant constants
    private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_control_column";
    private static final String CONTROL_COLUMN_COMPOSITE = "org_eclipse_emf_ecp_ui_control_column_composite";
    

    private Map<Class<? extends org.eclipse.emf.ecp.view.model.Composite>, SWTRenderer> renderers;
	private SWTTreeRenderNode resultComposite;

    public CompositeFactoryImpl() {
    	composedAdapterFactory = new ComposedAdapterFactory(
    			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
    	adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
    	evaluators = new LinkedHashMap<Condition, ConditionEvaluator>();
	}
	
    @Override
    public void dispose() {
    	// TODO: iterate over tree
        dispose(resultComposite);

        composedAdapterFactory.dispose();
    }

	private void dispose(SWTTreeRenderNode node) {
		node.dispose();
		for (SWTTreeRenderNode child : node.getChildren()) {
			dispose(child);
		}
	}

    @Override
    public Composite getComposite(final Composite parent,
            final org.eclipse.emf.ecp.view.model.Composite modelComposite, 
            ECPControlContext controlContext) {
        
        resultComposite = SWTRendererRegistry.INSTANCE.render(parent, modelComposite, controlContext, adapterFactoryItemDelegator);
        resultComposite.checkShow();
        resultComposite.checkEnable();
        resultComposite.getRenderedResult().setBackground(parent.getBackground());
        resultComposite.execute(new SWTTreeRendererNodeVisitor() {
			@Override
			public void executeOnNode(SWTTreeRenderNode node) {
				org.eclipse.swt.widgets.Control renderedResult = node.getRenderedResult();
				renderedResult.setBackground(parent.getBackground());
			}
		});
        return (Composite) resultComposite.getRenderedResult();
    }
    
    private boolean isLeaf(org.eclipse.emf.ecp.view.model.Composite composite) {
    	return Control.class.isInstance(composite) || Seperator.class.isInstance(composite);
    }
    
    private boolean isControl(org.eclipse.emf.ecp.view.model.Composite composite) {
    	return composite instanceof Control;
    }

    private void checkEnableRule(final org.eclipse.swt.widgets.Control resultComposite,
            final org.eclipse.emf.ecp.view.model.Composite modelComposite, EObject article) {
        if (EnableRule.class.isInstance(modelComposite.getRule())) {
            Condition condition = modelComposite.getRule().getCondition();
            if (!evaluators.containsKey(condition)) {
                evaluators.put(condition, new ConditionEvaluator(article, condition, 
                        new IConditionEvalResult() {
                    @Override
                    public void evalFinished(boolean result) {
                        if (result == ((EnableRule) modelComposite.getRule()).isDisable()) {
                            resultComposite.setEnabled(false);
                        } else {
                            resultComposite.setEnabled(true);
                        }
                    }
                }));
            }
        }
    }
   

    @Override
    public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
    	resultComposite.validationChanged(affectedObjects);
    }
   
}
