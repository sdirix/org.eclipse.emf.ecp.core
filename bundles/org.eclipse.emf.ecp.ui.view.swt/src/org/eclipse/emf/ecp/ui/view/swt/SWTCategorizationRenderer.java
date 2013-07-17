package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

// TODO: do we need to set a custom variant
public class SWTCategorizationRenderer extends AbstractSWTRenderer<Categorization> {
	
	@Override
	public Control renderSWT(Node<Categorization> node,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
				
		Composite categoryComposite = new Composite(getParent(), SWT.NONE);
		categoryComposite.setBackground(getParent().getBackground());
		
		GridLayoutFactory.fillDefaults()
			.numColumns(1)
			.applyTo(categoryComposite);
		GridDataFactory.fillDefaults()
			.align(SWT.FILL, SWT.FILL)
			.grab(true, true)
			.applyTo(categoryComposite);
		
		
		node.addRenderingResultDelegator(withSWT(categoryComposite));
			
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
