package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.ecp.internal.ui.view.renderer.WithRenderedObject;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTLifted implements WithRenderedObject {

	private final Control control;
	
	public SWTLifted(Control control) {
		this.control = control;
	}

	@Override
	public void enableIsTrue() {
		control.setEnabled(true);
	}

	@Override
	public void enableIsFalse() {
		control.setEnabled(false);
	}

	@Override
	public void showIsFalse() {
		  GridData gridData = (GridData) control.getLayoutData();
          if (gridData != null) {
              gridData.exclude = false;
          }
          control.setVisible(true);
	}

	@Override
	public void showIsTrue() {
        GridData gridData = (GridData) control.getLayoutData();
        if (gridData != null) {
            gridData.exclude = true;
        }
        control.setVisible(false);		
	}

	@Override
	public void layout() {
		  if (control != null) {
              Composite parent = control.getParent();
              parent.layout(true, true);
          }
	}

	@Override
	public void cleanup() {
		control.dispose();
	}

	public Control getControl() {
		return control;
	}
}
