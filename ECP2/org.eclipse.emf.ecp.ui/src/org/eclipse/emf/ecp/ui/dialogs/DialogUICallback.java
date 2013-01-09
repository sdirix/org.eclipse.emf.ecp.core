/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
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
package org.eclipse.emf.ecp.ui.dialogs;

import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.ui.common.AbstractUICallback;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog Implementation of the {@link AbstractUICallback}.
 * 
 * @author Eugen Neufeld
 */
public class DialogUICallback extends AbstractUICallback {

	/**
	 * Convenient constructor.
	 * 
	 * @param shell the {@link Shell} to use
	 */
	public DialogUICallback(Shell shell) {
		super(shell);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#open()
	 */
	@Override
	public int open() {
		Dialog dialog = new Dialog(getShell()) {

			@Override
			protected void configureShell(Shell newShell) {
				super.configureShell(newShell);
				newShell.setText(getTitle());

			}

			@Override
			protected Control createDialogArea(Composite parent) {
				Composite composite = getCompositeProvider().createUI(parent);
				composite.setLayoutData(new GridData(GridData.FILL_BOTH));
				return composite;
			}

			@Override
			protected boolean isResizable() {
				return true;
			}

			@Override
			protected IDialogSettings getDialogBoundsSettings() {
				return getDialogSettings();
			}

			/**
			 * Return the dialog store to cache values into
			 */
			protected IDialogSettings getDialogSettings() {
				if (getSettingSectionName() == null) {
					return null;
				}
				IDialogSettings workbenchSettings = Activator.getInstance().getDialogSettings();
				IDialogSettings section = workbenchSettings.getSection(getSettingSectionName());
				if (section == null) {
					section = workbenchSettings.addNewSection(getSettingSectionName());
				}
				return section;
			}
		};
		int result = dialog.open();
		if (Window.OK == result) {
			return OK;
		}
		return CANCEL;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
