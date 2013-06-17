package org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRendererRegistry;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTColumnRenderer implements SWTRenderer<Column> {
	
    private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_control_column";

	@Override
	public SWTTreeRenderNode render(Composite parent, Column modelColumn,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN);
		columnComposite.setBackground(parent.getBackground());
		SWTTreeRenderNode node = new SWTTreeRenderNode(columnComposite, modelColumn, controlContext);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(columnComposite);
		
		for (org.eclipse.emf.ecp.view.model.Composite modelComposite : modelColumn.getComposites()) {
			
			SWTTreeRenderNode childNode = SWTRendererRegistry.INSTANCE.render(
					columnComposite, modelComposite, controlContext, adapterFactoryItemDelegator);
			// TOOD; when does this case apply?
			if (childNode == null) {
				continue;
			}
			node.addChild(childNode);
			Control control = node.getRenderedResult();
			control.setBackground(parent.getBackground());
			
			if (!childNode.isLeaf()) {
				GridDataFactory.fillDefaults()
					.align(SWT.FILL, SWT.BEGINNING)
					.grab(true, false)
					.span(2, 1).applyTo(control);
			}
		}
		
		return node;
	}


}
