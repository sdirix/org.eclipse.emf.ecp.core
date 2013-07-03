package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTRendererNode extends RendererNode<Control> {

	public SWTRendererNode(Control result, org.eclipse.emf.ecp.view.model.Renderable renderable,
			ECPControlContext controlContext) {
		super(result, renderable, controlContext);
	}

	@Override
	public void enableIsTrue() {
		getRenderedResult().setEnabled(true);
	}

	@Override
	public void enableIsFalse() {
		getRenderedResult().setEnabled(false);
	}

	@Override
	public void showIsFalse() {
		  GridData gridData = (GridData) getRenderedResult().getLayoutData();
          if (gridData != null) {
              gridData.exclude = false;
          }
          getRenderedResult().setVisible(true);
	}

	@Override
	public void showIsTrue() {
		Composite composite = (Composite) getRenderedResult();
        GridData gridData = (GridData) composite.getLayoutData();
        if (gridData != null) {
            gridData.exclude = true;
        }
        composite.setVisible(false);		
	}

	@Override
	public void layout() {
		  if (getRenderedResult() != null) {
              Composite parent = getRenderedResult().getParent();
              parent.layout(true, true);
          }
	}

	@Override
	public void cleanup() {
		getRenderedResult().dispose();
	}
}
