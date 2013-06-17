package org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.SWTRenderer;
import org.eclipse.emf.ecp.view.model.Seperator;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Renders an {@link Seperator} to a SWT {@link Label}.
 *
 */
public class SWTSeparatorRenderer implements SWTRenderer<Seperator> {

	@Override
	public SWTTreeRenderNode render(Composite parent, Seperator modelSeparator, ECPControlContext context, AdapterFactoryItemDelegator adapterFactoryItemDelegator) {
		
		Label label = new Label(parent, SWT.NONE);
		label.setText(modelSeparator.getName());
		label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_ui_seperator");
		
		GridDataFactory.fillDefaults()
			.align(SWT.FILL, SWT.BEGINNING)
			.grab(true, false)
			.span(2, 1)
			.applyTo(label);
		
		SWTTreeRenderLeaf n = new SWTTreeRenderLeaf(label, modelSeparator, null, context);
		
		return n;
	}

	   
}
