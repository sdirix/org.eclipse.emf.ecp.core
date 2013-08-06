package org.eclipse.emf.ecp.ui.view.custom.swt;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderingResultDelegator;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class SWTRenderingResultCustomControl extends SWTRenderingResultDelegator {

	private final ECPAbstractCustomControlSWT control;
	private final CustomControl model;

	public SWTRenderingResultCustomControl(ECPAbstractCustomControlSWT control, CustomControl model, Control... results) {
		super(results);
		this.control = control;
		this.model = model;
	}

	@Override
	public void cleanup() {
		super.cleanup();
		control.dispose();
	}

	public boolean canValidate() {
		return getControls() != null;
	}

	@Override
	public void validationChanged(final Map<EObject, Set<Diagnostic>> affectedObjects) {

		if (!canValidate()) {
			return;
		}
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				control.resetValidation();
				if (affectedObjects.containsKey(model)) {
					for (final Diagnostic diagnostic : affectedObjects.get(model)) {
						control.handleValidation(diagnostic);
					}
				}
			}
		});
	}
}
