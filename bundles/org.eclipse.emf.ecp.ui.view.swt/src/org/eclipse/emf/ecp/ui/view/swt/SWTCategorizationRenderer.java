package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class SWTCategorizationRenderer extends AbstractSWTRenderer<Categorization> {
	
	private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_categorization";

	@Override
	public SWTRendererNode render(Categorization categorization,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException {
		// TODO: and if categorization size is 0?
		return SWTRenderers.INSTANCE.render(getParent(), categorization.getCategorizations().get(0), controlContext, adapterFactoryItemDelegator);
		
//		Composite categoryComposite = new Composite(getParent(), SWT.NONE);//
		//		categoryComposite.setBackground(getParent().getBackground());
//		// TODO: custom variant
////		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN_COMPOSITE);
//		
//		GridLayoutFactory.fillDefaults()
//			.applyTo(categoryComposite);
//		GridDataFactory.fillDefaults()
//			.align(SWT.FILL, SWT.FILL)
//			.grab(true, true)
//			.applyTo(categoryComposite);
//		
//		
//		
//		SWTRendererNode node = new SWTRendererNode(categoryComposite, categorization, controlContext);
//		
//		
//		Composite composite = new Composite(categoryComposite, SWT.NONE);
//		composite.setBackground(getParent().getBackground());
//			
//		GridDataFactory.fillDefaults()
//			.align(SWT.FILL, SWT.FILL)
//			.grab(true, true)
//			.applyTo(composite);
//			
//		GridLayoutFactory.fillDefaults()
//			.applyTo(composite);
//			
//		// TODO: render first item by default
//		// TODO: what to do with empty categorizations
//		SWTRendererNode childNode = SWTRenderers.INSTANCE.render(categoryComposite, categorization.getCategorizations().get(0), controlContext, adapterFactoryItemDelegator);
//		node.addChild(childNode);
//		Control control = childNode.getRenderedResult();
//			
//		if (!childNode.isLeaf()) {
//			GridDataFactory.fillDefaults()
//			.align(SWT.FILL, SWT.FILL)
//			.grab(true, true)
//			.span(2, 1)
//			.applyTo(control);
//		} 
//		
//		return node;
	}
}
