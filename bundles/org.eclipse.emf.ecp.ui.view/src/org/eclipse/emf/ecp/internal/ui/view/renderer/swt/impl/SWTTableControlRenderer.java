package org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableColumnConfiguration;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableControlConfiguration;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.model.TableColumn;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SWTTableControlRenderer extends AbstractSWTControlRender<TableControl> {

	private org.eclipse.emf.ecp.edit.internal.swt.controls.TableControl control;

	@Override
	public SWTTreeRenderNode render(Composite parent, TableControl modelTableControl,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		ECPControlContext subContext = createSubcontext(modelTableControl, controlContext);
        EClass dataClass = modelTableControl.getTargetFeature().getEContainingClass();
        
        if (dataClass == null) {
            return null;
        }

        IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
                .getPropertyDescriptor(subContext.getModelElement(),
        
                		modelTableControl.getTargetFeature());
        if (itemPropertyDescriptor == null) {
            return null;
        }

        TableControlConfiguration tcc = new TableControlConfiguration();
        tcc.setAddRemoveDisabled(modelTableControl.isAddRemoveDisabled());
        
        for (TableColumn column : modelTableControl.getColumns()) {
            tcc.getColumns().add(
                    new TableColumnConfiguration(column.isReadOnly(), column.getAttribute()));
        }

        org.eclipse.emf.ecp.edit.internal.swt.controls.TableControl control = new org.eclipse.emf.ecp.edit.internal.swt.controls.TableControl(
                false, itemPropertyDescriptor,
                (EStructuralFeature) itemPropertyDescriptor.getFeature(subContext
                        .getModelElement()), subContext, false, tcc);
        this.control = control;

        if (control != null) {
            int numControl = 2;
            Label label = null;
            if (control.showLabel()) {
                numControl = 1;
                label = new Label(parent, SWT.NONE);
                label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_label");
                label.setBackground(parent.getBackground());
                System.out.println(parent.getBackground());
                String extra = "";
                if (((EStructuralFeature) itemPropertyDescriptor.getFeature(null)).getLowerBound() > 0) {
                    extra = "*";
                }
                label.setText(itemPropertyDescriptor.getDisplayName(subContext.getModelElement())
                        + extra);
                label.setToolTipText(itemPropertyDescriptor.getDescription(subContext
                        .getModelElement()));
                GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);
            }
            
            Composite controlComposite = control.createControl(parent);
            controlComposite.setEnabled(!modelTableControl.isReadonly());
            controlComposite.setBackground(parent.getBackground());
            
            GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false)
                    .span(numControl, 1).applyTo(controlComposite);
            
            return new SWTTreeRenderLeaf(controlComposite, modelTableControl, control, controlContext);
        }
        
        Activator.getDefault().ungetECPControlFactory();
        return null;
	}

	@Override
	protected SWTControl getControl() {	
		return control;
	}
}
