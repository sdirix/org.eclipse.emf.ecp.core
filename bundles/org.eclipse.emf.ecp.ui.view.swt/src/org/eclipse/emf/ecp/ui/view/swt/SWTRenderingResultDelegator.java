package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegator;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import java.util.Map;
import java.util.Set;

public class SWTRenderingResultDelegator implements RenderingResultDelegator {

	private final Control[] controls;

	public SWTRenderingResultDelegator(Control... control) {
		controls = control;
	}

	public void enable(boolean shouldEnable) {
		for (Control control : controls) {
			control.setEnabled(shouldEnable);
		}
	}

	public void show(boolean shouldShow) {
		for (Control control : controls) {
			GridData gridData = (GridData) control.getLayoutData();
			if (gridData != null) {
				gridData.exclude = false;
			}
			control.setVisible(shouldShow);
		}
	}

	public void layout() {
		for (Control control : controls) {
			Composite parent = control.getParent();
			parent.layout(true, true);
		}
	}

	public void cleanup() {
		for (Control control : controls) {
			control.dispose();
		}
	}

	public Control[] getControls() {
		return controls;
	}

	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {

	}
}
