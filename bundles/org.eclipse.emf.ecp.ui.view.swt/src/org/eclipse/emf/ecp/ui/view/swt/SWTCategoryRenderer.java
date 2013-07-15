package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTCategoryRenderer extends AbstractSWTRenderer<Category> {
	
	private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_category";

	@Override
	public Control renderSWT(Node<Category> node,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		Composite categoryComposite = new Composite(getParent(), SWT.NONE);
		categoryComposite.setBackground(getParent().getBackground());
		// TODO: custom variant
//		columnComposite.setData(CUSTOM_VARIANT, CONTROL_COLUMN_COMPOSITE);
		
		GridLayoutFactory.fillDefaults()
			.numColumns(1)
			.applyTo(categoryComposite);
		GridDataFactory.fillDefaults()
			.align(SWT.FILL, SWT.FILL)
			.grab(true, true)
			.applyTo(categoryComposite);
		
		
		node.addRenderingResultDelegator(withSWT(categoryComposite));
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
			
		Node<? extends Renderable> childNode = node.getChildren().get(0);
		
		Control control = SWTRenderers.INSTANCE.render(categoryComposite, childNode, adapterFactoryItemDelegator);
			
		if (!childNode.isLeaf()) {
			GridDataFactory.fillDefaults()
			.align(SWT.FILL, SWT.FILL)
			.grab(true, true)
			.span(2, 1)
			.applyTo(control);
		} 
		
		return categoryComposite;
	}
}
