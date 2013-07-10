package org.eclipse.emf.ecp.ui.view.swt;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTLifted implements RenderingResultDelegator {

	private final Control control;
	
	public SWTLifted(Control control) {
		this.control = control;
	}

	@Override
	public void enable(boolean shouldEnable) {
		control.setEnabled(shouldEnable);
	}

	@Override
	public void show(boolean shouldShow) {
		  GridData gridData = (GridData) control.getLayoutData();
          if (gridData != null) {
              gridData.exclude = false;
          }
          control.setVisible(shouldShow);
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

    @Override
    public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
       
    }
	
}
