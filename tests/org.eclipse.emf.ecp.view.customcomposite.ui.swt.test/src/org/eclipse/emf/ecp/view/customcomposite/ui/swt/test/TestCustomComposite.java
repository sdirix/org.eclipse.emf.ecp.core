/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.customcomposite.ui.swt.test;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Eugen Neufeld
 * 
 */
public class TestCustomComposite extends Composite {

	private static final long serialVersionUID = 1L;

	/**
	 * @param parent
	 */
	public TestCustomComposite(Composite parent, EObject eObject) {
		super(parent, SWT.NONE);
		createContent();
	}

	private void createContent() {
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(this);
		final Label label = new Label(this, SWT.NONE);
		label.setText("Datum");
	}

}
