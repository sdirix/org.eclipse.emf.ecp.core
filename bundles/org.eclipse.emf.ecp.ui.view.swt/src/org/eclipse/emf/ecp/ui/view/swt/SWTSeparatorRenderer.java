package org.eclipse.emf.ecp.ui.view.swt;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererLeaf;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.emf.ecp.view.model.Seperator;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Renders an {@link Seperator} to a SWT {@link Label}.
 *
 */
public class SWTSeparatorRenderer extends AbstractSWTRenderer<Seperator> { 

	@Override
	public SWTRendererNode render(Seperator model,
			ECPControlContext controlContext,
			AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		Label label = new Label(getParent(), SWT.NONE);
		label.setText(model.getName());
		label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_ui_seperator");
		
		GridDataFactory.fillDefaults()
		.align(SWT.FILL, SWT.BEGINNING)
		.grab(true, false)
		.span(2, 1)
		.applyTo(label);
		
		SWTRendererLeaf n = new SWTRendererLeaf(label, model, controlContext, null);
		
		return n;
	}

	   
}
