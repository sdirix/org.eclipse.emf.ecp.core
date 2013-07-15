package org.eclipse.emf.ecp.ui.view.swt;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.swt.widgets.Control;

public class SWTRenderingResultDelegatorWithControl extends SWTRenderingResultDelegator {

	private Renderable model;
	private SWTControl swtControl;

	public SWTRenderingResultDelegatorWithControl(Control[] results, SWTControl swtControl, Renderable model) {
		super(results);
		this.swtControl = swtControl;
		this.model = model;
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		swtControl.dispose();
	}

	public boolean canValidate() {
		return getControls() != null;
	}

	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {

		if (!canValidate()) {
			return;
		}

		swtControl.resetValidation();
		if (affectedObjects.containsKey(model)) { 
			for (Diagnostic diagnostic : affectedObjects.get(model)) {
				swtControl.handleValidation(diagnostic);
			}
		}
	}
}
