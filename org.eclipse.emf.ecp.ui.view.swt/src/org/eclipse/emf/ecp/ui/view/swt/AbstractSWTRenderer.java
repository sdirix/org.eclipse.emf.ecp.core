package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.view.model.Composite;

public abstract class AbstractSWTRenderer<T extends Composite> 
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
