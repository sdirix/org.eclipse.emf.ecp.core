package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

/**
 * Common interface for renderers.
 * 
 * @author emueller
 *
 * @param <C>
 * 			the actual type of the control being rendered
 * @param <R>
 * 			a subtype of {@link Composite} specifying the view model type to be rendered 
 * @param <C>
 */
public interface ControlRenderer<R extends Renderable, C> {
	
	/**
	 * Render a control.
	 * 
	 * @param model
	 * 			the semantic view model representation of the control to be rendered 
	 * @param controlContext
	 * 			the control context containing the {@link EObject} to be rendered
	 * @param adapterFactoryItemDelegator
	 * 			an {@link AdapterFactoryItemDelegator} instance that may be used during rendering
	 * 
	 * @return
	 */
	// TODO: JAVADOC
	C render(Node<R> node,
            ECPControlContext controlContext,
            AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption;
	/**
	 * Initializes a renderer.
	 * 
	 * @param initData
	 * 			arbitrary data needed by the renderer to initialize itself, e.g. for SWT, 
	 * 			you must pass in the parent {@link org.eclipse.swt.widgets.Composite} 
	 */
	void initialize(Object[] initData);

}
