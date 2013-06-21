package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTColumnRenderer extends AbstractSWTRenderer<Column> {
	
	private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_control_column";

	@Override
	public SWTRendererNode render(Column modelColumn,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		Composite columnComposite = new Composite(getParent(), SWT.NONE);
		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN);
		columnComposite.setBackground(getParent().getBackground());
		SWTRendererNode node = new SWTRendererNode(columnComposite, modelColumn, controlContext);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(columnComposite);
		for (org.eclipse.emf.ecp.view.model.Composite modelComposite : modelColumn.getComposites()) {
			
			RendererNode<Control> childNode = SWTRenderers.INSTANCE.render(
					columnComposite, modelComposite, controlContext, adapterFactoryItemDelegator);
			// TOOD; when does this case apply?
			if (childNode == null) {
				continue;
			}
			node.addChild(childNode);
			Control control = childNode.getRenderedResult();
			control.setBackground(getParent().getBackground());
			//TODO Add check to handle differently if label is shown 
			if (!childNode.isLeaf() ) {
				GridDataFactory.fillDefaults()
					.align(SWT.FILL, SWT.BEGINNING)
					.grab(true, false)
					.span(2, 1).applyTo(control);
			}
		}
		
		return node;
	}
}
