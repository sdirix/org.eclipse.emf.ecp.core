package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SWTControlRenderer extends AbstractSWTControlRenderer<Control> {

	private SWTControl control;
	
	@Override
	protected SWTControl getControl() {	
		return control;
	}

	@Override
	public org.eclipse.swt.widgets.Control render(Node<Control> node,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		
		Control modelControl = node.getRenderable();
		EClass dataClass = modelControl.getTargetFeature().getEContainingClass();
        ECPControlContext subContext = node.getControlContext();
        
        if (dataClass == null) {
            return null;
        }

        IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
                .getPropertyDescriptor(subContext.getModelElement(),
                        modelControl.getTargetFeature());
        
        if (itemPropertyDescriptor == null) {
            throw new NoPropertyDescriptorFoundExeption(subContext.getModelElement(), modelControl.getTargetFeature());
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
                label = new Label(getParent(), SWT.NONE);
                label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_label");
                label.setBackground(getParent().getBackground());
                String extra = "";
                if (((EStructuralFeature) itemPropertyDescriptor.getFeature(null)).getLowerBound() > 0) {
                    extra = "*";
                }
                label.setText(itemPropertyDescriptor.getDisplayName(subContext.getModelElement())
                        + extra);
                label.setToolTipText(itemPropertyDescriptor.getDescription(subContext.getModelElement()));
                GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);
            }
            
            Composite controlComposite = control.createControl(getParent());
            controlComposite.setEnabled(!modelControl.isReadonly());
            controlComposite.setBackground(getParent().getBackground());
            
            node.lift(withSWTControl(controlComposite, control, modelControl));
            
            GridDataFactory.fillDefaults()
            	.align(SWT.FILL, SWT.CENTER)
            	.grab(true, false)
                .span(numControl, 1)
                .applyTo(controlComposite);
            
            return controlComposite;
        }
        
        Activator.getDefault().ungetECPControlFactory();
        
        return null;
	}

}
