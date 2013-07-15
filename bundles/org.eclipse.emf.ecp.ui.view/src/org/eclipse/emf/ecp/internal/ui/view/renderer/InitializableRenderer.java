package org.eclipse.emf.ecp.internal.ui.view.renderer;

public interface InitializableRenderer {
	
	/**
	 * Initializes a renderer.
	 * 
	 * @param initData
	 * 			arbitrary data needed by the renderer to initialize itself, e.g. for SWT, 
	 * 			you must pass in the parent {@link org.eclipse.swt.widgets.Composite} 
	 */
	void initialize(Object[] initData);
}
