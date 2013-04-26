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
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.util;

import org.eclipse.jface.dialogs.Dialog;

/**
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPDialogExecutor {

	private Dialog dialog;

	/**
	 * @param dialog
	 */
	public ECPDialogExecutor(Dialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * @param codeResult
	 */
	public abstract void handleResult(int codeResult);

	public void execute() {
		DialogOpener.openDialog(dialog, this);
	}
}
