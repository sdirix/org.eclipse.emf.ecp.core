package org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRenderer;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRendererRegistry;
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
public class SWTGroupRenderer implements SWTRenderer<Group> {

	@Override
	public SWTTreeRenderNode render(Composite parent,
			Group modelGroup,
			ECPControlContext controlContext, 
			AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		org.eclipse.swt.widgets.Group group = new org.eclipse.swt.widgets.Group(parent, SWT.TITLE);
		group.setText(modelGroup.getName());
		
		GridLayoutFactory.fillDefaults()
			.numColumns(2)
			.equalWidth(false)
			.applyTo(group);
		
		SWTTreeRenderNode node = new SWTTreeRenderNode(group, modelGroup, controlContext);
		
		for (org.eclipse.emf.ecp.view.model.Composite modelComposite : modelGroup.getComposites()) {
			
			SWTTreeRenderNode render = SWTRendererRegistry.INSTANCE.render(group, modelComposite, controlContext, adapterFactoryItemDelegator);
			node.addChild(render);
			Control control = render.getRenderedResult();
			control.setBackground(parent.getBackground());
			
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
