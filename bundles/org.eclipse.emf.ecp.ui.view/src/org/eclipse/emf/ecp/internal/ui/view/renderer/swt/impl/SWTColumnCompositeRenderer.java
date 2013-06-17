package org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRendererRegistry;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTColumnCompositeRenderer implements SWTRenderer<ColumnComposite> {
	
    private static final String CONTROL_COLUMN_COMPOSITE = "org_eclipse_emf_ecp_ui_control_column_composite";

	@Override
	public SWTTreeRenderNode render(Composite parent, ColumnComposite modelColumnComposite,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {

		Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setBackground(parent.getBackground());
		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN_COMPOSITE);
		
		GridLayoutFactory.fillDefaults()
			.numColumns(modelColumnComposite.getComposites().size())
			.equalWidth(true)
			.applyTo(columnComposite);
		
		int numHiddenColumns = 0;
		
		SWTTreeRenderNode node = new SWTTreeRenderNode(columnComposite, modelColumnComposite, controlContext);
		
		for (org.eclipse.emf.ecp.view.model.Composite modelComposite : modelColumnComposite.getComposites()) {
			
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
			
			SWTTreeRenderNode childNode = SWTRendererRegistry.INSTANCE.render(column, 
					modelComposite, controlContext, adapterFactoryItemDelegator);
			node.addChild(childNode);
			Control control = childNode.getRenderedResult();
			control.setBackground(parent.getBackground());
			
			if (!childNode.isLeaf()) {
				GridDataFactory.fillDefaults()
					.align(SWT.FILL, SWT.FILL)
					.grab(true, false)
					.span(2, 1)
					.applyTo(control);
			} else {
				column.dispose();
				numHiddenColumns++;
			}
		}
		
		((GridLayout) columnComposite.getLayout()).numColumns = 
				modelColumnComposite.getComposites().size() - numHiddenColumns;

		return node;
	}


}
