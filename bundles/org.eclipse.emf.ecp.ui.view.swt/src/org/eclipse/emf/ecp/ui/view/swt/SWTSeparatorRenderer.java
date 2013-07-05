package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.Seperator;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Renders an {@link Seperator} to a SWT {@link Label}.
 *
 */
public class SWTSeparatorRenderer extends AbstractSWTRenderer<Seperator> { 

	@Override
	public Control render(Node<Seperator> node,
			ECPControlContext controlContext,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		
		Seperator separator = node.getRenderable();
		Label label = new Label(getParent(), SWT.NONE);
		label.setText(separator.getName());
		label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_ui_seperator");
		
		GridDataFactory.fillDefaults()
		.align(SWT.FILL, SWT.CENTER)
		.grab(true, true)
		.span(2, 1)
		.applyTo(label);
		
		node.lift(withSWT(label));
		
		return label;
	}

	   
}
