package org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SWTControlRenderer extends AbstractSWTControlRender<Control> {

	private SWTControl control;

	@Override
	public SWTTreeRenderNode render(Composite parent, Control modelControl,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		EClass dataClass = modelControl.getTargetFeature().getEContainingClass();
        ECPControlContext subContext = createSubcontext(modelControl, controlContext);
        
        if (dataClass == null) {
            return null;
        }

        IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
                .getPropertyDescriptor(subContext.getModelElement(),
                        modelControl.getTargetFeature());
        
        if (itemPropertyDescriptor == null) {
            return null;
        }

        ECPControlFactory controlFactory = Activator.getDefault().getECPControlFactory();
        
        if (controlFactory == null) {
            Activator.getDefault().ungetECPControlFactory();
            return null;
        }
        
        SWTControl control = controlFactory.createControl(SWTControl.class, itemPropertyDescriptor,
                subContext);
        this.control = control;

        if (control != null) {
            int numControl = 2;
            Label label = null;
            if (control.showLabel()) {
                numControl = 1;
                label = new Label(parent, SWT.NONE);
                label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_label");
                label.setBackground(parent.getBackground());
                String extra = "";
                if (((EStructuralFeature) itemPropertyDescriptor.getFeature(null)).getLowerBound() > 0) {
                    extra = "*";
                }
                label.setText(itemPropertyDescriptor.getDisplayName(subContext.getModelElement())
                        + extra);
                label.setToolTipText(itemPropertyDescriptor.getDescription(subContext.getModelElement()));
                GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);
            }
            
            Composite controlComposite = control.createControl(parent);
            controlComposite.setEnabled(!modelControl.isReadonly());
            controlComposite.setBackground(parent.getBackground());
            
            SWTTreeRenderLeaf leaf = new SWTTreeRenderLeaf(controlComposite, modelControl, control, controlContext);
            
            GridDataFactory.fillDefaults()
            	.align(SWT.FILL, SWT.CENTER)
            	.grab(true, false)
                .span(numControl, 1)
                .applyTo(controlComposite);
            
            return leaf;
        }
        
        Activator.getDefault().ungetECPControlFactory();
        
        return null;
	}
	
	@Override
	protected SWTControl getControl() {	
		return control;
	}

}
