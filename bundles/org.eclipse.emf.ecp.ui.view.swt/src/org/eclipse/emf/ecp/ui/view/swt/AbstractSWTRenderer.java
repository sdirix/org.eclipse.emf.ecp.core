package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegator;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Common base class for all SWT specific renderer classes.
 * 
 * @author emueller
 * 
 * @param <R> the actual type of the {@link Renderable} to be drawn
 */
public abstract class AbstractSWTRenderer<R extends Renderable> implements SWTRenderer<R> {

	protected org.eclipse.swt.widgets.Composite getParentFromInitData(Object[] initData) {
		return (Composite) initData[0];
	}

	public RenderingResultDelegator withSWT(Control control) {
		return new SWTRenderingResultDelegator(control);
	}

	public RenderingResultDelegator withSWTControls(SWTControl swtControl, Renderable model, Control... controls) {
		return new SWTRenderingResultDelegatorWithControl(controls, swtControl, model);
	}

	public Control render(Node<R> node, AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		Control swtControl = renderSWT(node, adapterFactoryItemDelegator, initData);

		if (!node.isVisible()) {
			node.show(false);
		}

		if (!node.isEnabled()) {
			node.enable(false);
		}

		return swtControl;
	}

	protected abstract Control renderSWT(Node<R> node, AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption;
}
