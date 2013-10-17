/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.internal;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class SWTRenderingResultDelegatorWithControl extends SWTRenderingResultDelegator {

	private final Renderable model;
	private final SWTControl swtControl;
	private final Adapter adapter;

	public SWTRenderingResultDelegatorWithControl(Control[] results, SWTControl swtControl, Renderable model) {
		super(results);
		this.swtControl = swtControl;
		this.model = model;
		adapter = new AdapterImpl() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
			 */
			@Override
			public void notifyChanged(Notification msg) {
				super.notifyChanged(msg);
				if (ViewPackage.eINSTANCE.getRenderable_Diagnostic().equals(msg.getFeature())) {
					if (msg.getEventType() == Notification.SET) {
						updateValidation();
					}
				}
			}

		};
		model.eAdapters().add(adapter);
		updateValidation();
	}

	@Override
	public void cleanup() {
		super.cleanup();
		swtControl.dispose();
		model.eAdapters().remove(adapter);
	}

	public boolean canValidate() {
		return getControls() != null;
	}

	private void updateValidation() {

		if (!canValidate()) {
			return;
		}
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				swtControl.resetValidation();
				if (model.getDiagnostic() != null) {
					for (final Object diagnostic : model.getDiagnostic().getDiagnostics()) {
						swtControl.handleValidation((Diagnostic) diagnostic);
					}
				}

			}
		});
	}

	@Override
	public void validationChanged(final Map<EObject, Set<Diagnostic>> affectedObjects) {
		//
		// if (!canValidate()) {
		// return;
		// }
		// Display.getDefault().syncExec(new Runnable() {
		//
		// public void run() {
		// swtControl.resetValidation();
		// if (affectedObjects.containsKey(model)) {
		// for (final Diagnostic diagnostic : affectedObjects.get(model)) {
		// swtControl.handleValidation(diagnostic);
		// }
		// }
		// }
		// });

	}
}
