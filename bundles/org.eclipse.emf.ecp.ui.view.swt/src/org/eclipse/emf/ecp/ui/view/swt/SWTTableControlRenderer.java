package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableColumnConfiguration;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableControlConfiguration;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.TableColumn;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class SWTTableControlRenderer extends AbstractSWTRenderer<TableControl> {
	public static final SWTTableControlRenderer INSTANCE = new SWTTableControlRenderer();


	@Override
	public Control renderSWT(Node<TableControl> node,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator,Object...initData)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		
		TableControl modelTableControl = node.getRenderable();
		ECPControlContext subContext = node.getControlContext();
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

        if (control != null) {
        	Composite parent=getParentFromInitData(initData);
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
                label.setToolTipText(itemPropertyDescriptor.getDescription(subContext
                        .getModelElement()));
                GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);
            }
            
            Composite controlComposite = control.createControl(parent);
            controlComposite.setBackground(parent.getBackground());

            controlComposite.setEnabled(!modelTableControl.isReadonly());
            
            GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false)
                    .span(numControl, 1).applyTo(controlComposite);
            
            if (label == null) {
            	node.addRenderingResultDelegator(withSWTControls(control, modelTableControl, controlComposite));
            } else {
            	node.addRenderingResultDelegator(withSWTControls(control, modelTableControl, controlComposite, label));
            }
            
            return controlComposite;
        }
        
        Activator.getDefault().ungetECPControlFactory();
        return null;
	}
}
