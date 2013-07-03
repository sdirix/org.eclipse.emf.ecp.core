package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class SWTTreeCategoryRenderer extends AbstractSWTRenderer<TreeCategory> {
	
	private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_control_treecategory";

	@Override
	public SWTRendererNode render(TreeCategory treeCategory,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException {
//		return SWTRenderers.INSTANCE.render(getParent(), category.getComposite(), controlContext, adapterFactoryItemDelegator);
		
		Composite categoryComposite = new Composite(getParent(), SWT.NONE);
		categoryComposite.setBackground(getParent().getBackground());
		// TODO: custom variant
		
		GridLayoutFactory.fillDefaults()
			.applyTo(categoryComposite);
		GridDataFactory.fillDefaults()
			.align(SWT.FILL, SWT.FILL)
			.grab(true, true)
			.applyTo(categoryComposite);
		
		
		SWTRendererNode node = new SWTRendererNode(categoryComposite, treeCategory, controlContext);
		
		
		Composite composite = new Composite(categoryComposite, SWT.NONE);
		composite.setBackground(getParent().getBackground());
			
		GridDataFactory.fillDefaults()
			.align(SWT.FILL, SWT.FILL)
			.grab(true, true)
			.applyTo(composite);
			
		GridLayoutFactory.fillDefaults()
			.applyTo(composite);
			
		SWTRendererNode childNode = SWTRenderers.INSTANCE.render(categoryComposite, treeCategory.getChildComposite(), controlContext, adapterFactoryItemDelegator);
		node.addChild(childNode);
		Control control = childNode.getRenderedResult();
			
		if (!childNode.isLeaf()) {
			GridDataFactory.fillDefaults()
			.align(SWT.FILL, SWT.FILL)
			.grab(true, true)
			.span(2, 1)
			.applyTo(control);
		} 
		
		return node;
	}
}
