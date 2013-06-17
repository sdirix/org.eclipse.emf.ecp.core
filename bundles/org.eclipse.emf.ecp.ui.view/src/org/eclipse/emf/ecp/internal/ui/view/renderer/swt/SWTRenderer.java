package org.eclipse.emf.ecp.internal.ui.view.renderer.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl.SWTTreeRenderNode;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Composite;

/**
 * Common interface for all SWT renderers.
 *
 */
public interface SWTRenderer<T extends org.eclipse.emf.ecp.view.model.Composite> {
	
	/**
	 * Variant constant for indicating RAP controls.
	 */
	static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant";

	/**
	 * Render a SWT control
	 * 
	 * @param parent
	 * 			the parent composite
	 * @param model
	 * 			the semantic view model representation of the SWT control to be rendered 
	 * @param controlContext
	 * 			the control context containing the {@link EObject} to be rendered
	 * @param adapterFactoryItemDelegator
	 * 			an {@link AdapterFactoryItemDelegator} instance that may be used during rendering
	 * @return
	 */
	SWTTreeRenderNode render(final Composite parent,
            T model, 
            ECPControlContext controlContext,
            AdapterFactoryItemDelegator adapterFactoryItemDelegator);
	
}
