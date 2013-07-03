package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.view.model.Renderable;

public abstract class AbstractSWTRenderer<T extends Renderable> 
	implements SWTRenderer<T> {

	private org.eclipse.swt.widgets.Composite parent;
	
	@Override
	public void initialize(Object[] initData) {
		parent = (org.eclipse.swt.widgets.Composite) initData[0];
	}
	
	public org.eclipse.swt.widgets.Composite getParent() {
		return parent;
	}
}
