package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegator;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.swt.widgets.Control;


public abstract class AbstractSWTRenderer<R extends Renderable> implements SWTRenderer<R> {

	private org.eclipse.swt.widgets.Composite parent;
	
	@Override
	public void initialize(Object[] initData) {
		parent = (org.eclipse.swt.widgets.Composite) initData[0];
	}
	
	public org.eclipse.swt.widgets.Composite getParent() {
		return parent;
	}
	
	public RenderingResultDelegator withSWT(Control control) {
		return new SWTLifted(control);
	}
	
	public RenderingResultDelegator withSWTControl(Control control, SWTControl swtControl, Renderable model) {
		return new SWTLiftedControl(control, swtControl, model);
	}
}
