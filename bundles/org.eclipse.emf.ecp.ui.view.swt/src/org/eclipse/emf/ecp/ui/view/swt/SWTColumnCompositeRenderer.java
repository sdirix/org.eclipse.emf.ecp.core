package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTColumnCompositeRenderer extends AbstractSWTRenderer<ColumnComposite> {
	
	public static final SWTColumnCompositeRenderer INSTANCE = new SWTColumnCompositeRenderer();
	private static final String CONTROL_COLUMN_COMPOSITE = "org_eclipse_emf_ecp_ui_control_column_composite";
	
	@Override
	public Control renderSWT(Node<ColumnComposite> node, AdapterFactoryItemDelegator adapterFactoryItemDelegator,Object...initData) 
					throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		
		//TODO Add check whether label is shown
//		Label l=new Label(getParent(), SWT.NONE);
//		l.setText(modelColumnComposite.getName());
		Composite parent=getParentFromInitData(initData);
		ColumnComposite modelColumnComposite = node.getRenderable();
		
		Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setBackground(parent.getBackground());
		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN_COMPOSITE);
		
		GridLayoutFactory.fillDefaults()
			.numColumns(modelColumnComposite.getComposites().size())
			.equalWidth(true)
			.applyTo(columnComposite);
		
		//TODO Add to handle differently if label is shown
		GridDataFactory.fillDefaults()
			.align(SWT.FILL, SWT.BEGINNING)
			.grab(true, false)
			.applyTo(columnComposite);
		
//		SWTLifted node = new SWTLifted(columnComposite, modelColumnComposite, controlContext);
		
		node.addRenderingResultDelegator(withSWT(columnComposite));

		for (Node<? extends Renderable> child : node.getChildren()) {
			
			Composite column = new Composite(columnComposite, SWT.NONE);
			column.setBackground(parent.getBackground());
			
			GridDataFactory.fillDefaults()
				.align(SWT.FILL, SWT.FILL)
				.grab(true, true)
				.applyTo(column);
			
			GridLayoutFactory.fillDefaults()
				.numColumns(2)
				.equalWidth(false)
				.applyTo(column);
			
			Control childControl;
			
			try {
				childControl = SWTRenderers.INSTANCE.render(column, child, adapterFactoryItemDelegator);
			} catch (NoPropertyDescriptorFoundExeption e) {
				continue;
			}
						
			if (!child.isLeaf()) {
				GridDataFactory.fillDefaults()
					.align(SWT.FILL, SWT.FILL)
					.grab(true, true)
					.span(2, 1)
					.applyTo(childControl);
			}
			
			if (!node.isVisible()) {
				node.show(false);
			}
		}
		
		return columnComposite;
	}
}
