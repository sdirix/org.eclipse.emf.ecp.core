package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTColumnRenderer extends AbstractSWTRenderer<Column> {
	
	private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_control_column";

	@Override
	public Control render(Node<Column> node,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) 
					throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		
		Composite columnComposite = new Composite(getParent(), SWT.NONE);
		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN);
		columnComposite.setBackground(getParent().getBackground());
		
//		SWTLifted node = new SWTLifted(columnComposite, modelColumn, controlContext);
		
		node.lift(withSWT(columnComposite));
		
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(columnComposite);
		
		for (Node child : node.getChildren()) {
			
			Control childControl;
			try {
				childControl = SWTRenderers.INSTANCE.render(
					columnComposite, child, controlContext, adapterFactoryItemDelegator);
			} catch (NoPropertyDescriptorFoundExeption e) {
				continue;
			}
			
			// TOOD; when does this case apply?
			if (childControl  == null) {
				continue;
			}
			
//			Control control = childNode.getRenderedResult();
//			control.setBackground(getParent().getBackground());
			//TODO Add check to handle differently if label is shown 
			if (!child.isLeaf() ) {
				GridDataFactory.fillDefaults()
					.align(SWT.FILL, SWT.BEGINNING)
					.grab(true, false)
					.span(2, 1).applyTo(childControl);
			}
		}
		
		return columnComposite;
	}
}
