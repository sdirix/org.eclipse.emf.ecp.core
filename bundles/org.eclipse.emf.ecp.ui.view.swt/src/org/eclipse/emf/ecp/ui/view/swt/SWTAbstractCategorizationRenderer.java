package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class SWTAbstractCategorizationRenderer extends AbstractSWTRenderer<AbstractCategorization> {
	
	private static final Object CONTROL_COLUMN = "org_eclipse_emf_ecp_ui_categorization";

	@Override
	public SWTRendererNode render(AbstractCategorization abstractCategorization,
			ECPControlContext controlContext, AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException {
		
		 RendererNode<Control> childNode;
		 if (abstractCategorization instanceof Categorization) {
			 Categorization c = (Categorization) abstractCategorization;
			 return SWTRenderers.INSTANCE.render(getParent(), c.getCategorizations().get(0), controlContext, adapterFactoryItemDelegator);
		 } else if (abstractCategorization instanceof TreeCategory) {
			 TreeCategory treeCategory = (TreeCategory) abstractCategorization;
			 return SWTRenderers.INSTANCE.render(getParent(), treeCategory.getChildComposite(), controlContext, adapterFactoryItemDelegator);
		 } else {
			 Category category = (Category) abstractCategorization;
			 return SWTRenderers.INSTANCE.render(getParent(), category.getComposite(), controlContext, adapterFactoryItemDelegator);
		 }
	}
}
