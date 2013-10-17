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

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTRenderingResultDelegator implements RenderingResultDelegator {

	private final Control[] controls;

	public SWTRenderingResultDelegator(Control... control) {
		controls = control;
	}

	public void enable(boolean shouldEnable) {
		for (final Control control : controls) {
			control.setEnabled(shouldEnable);
		}
	}

	public void show(boolean shouldShow) {
		for (final Control control : controls) {
			final GridData gridData = (GridData) control.getLayoutData();
			if (gridData != null) {
				gridData.exclude = false;
			}
			control.setVisible(shouldShow);
		}
	}

	public void layout() {
		for (final Control control : controls) {
			final Composite parent = control.getParent();
			parent.layout(true, true);
		}
	}

	public void cleanup() {
		for (final Control control : controls) {
			control.dispose();
		}
	}

	public Control[] getControls() {
		return controls;
	}

	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {

	}
}
