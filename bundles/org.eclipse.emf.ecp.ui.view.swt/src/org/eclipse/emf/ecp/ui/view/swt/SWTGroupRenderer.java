package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Renders a SWT group.
 *
 */
public class SWTGroupRenderer extends AbstractSWTRenderer<Group> {

	@Override
	public SWTRendererNode render(Group modelGroup,
			ECPControlContext controlContext, 
			AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		org.eclipse.swt.widgets.Group group = new org.eclipse.swt.widgets.Group(getParent(), SWT.TITLE);
		group.setText(modelGroup.getName());
		
		GridLayoutFactory.fillDefaults()
			.numColumns(2)
			.equalWidth(false)
			.applyTo(group);
		
		SWTRendererNode node = new SWTRendererNode(group, modelGroup, controlContext);
		
		for (org.eclipse.emf.ecp.view.model.Composite modelComposite : modelGroup.getComposites()) {
			
			RendererNode<Control> render = SWTRenderers.INSTANCE.render(group, modelComposite, controlContext, adapterFactoryItemDelegator);
			node.addChild(render);
			Control control = render.getRenderedResult();
			control.setBackground(getParent().getBackground());
			
			if (!render.isLeaf()) {
				GridDataFactory.fillDefaults()
					.align(SWT.FILL, SWT.BEGINNING)
					.grab(true, false)
					.span(2, 1)
					.applyTo(control);
			}
		}
		
		return node;
	}


}
