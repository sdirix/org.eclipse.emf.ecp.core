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

package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.ui.common.AbstractUICallback;
import org.eclipse.emf.ecp.ui.composites.ICompositeProvider;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * This is an Wizard implementation for the {@link AbstractUICallback}.
 * 
 * @param <T> the Generic type for this Callback
 * @author Eugen Neufeld
 */
public class WizardUICallback extends AbstractUICallback {

	private final ECPWizard<?> wizard;

	public <T extends ICompositeProvider> WizardUICallback(Shell shell, ECPWizard<T> wizard) {
		super(shell);
		this.wizard = wizard;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#open()
	 */
	@Override
	public int open() {
		WizardDialog wd = new WizardDialog(getShell(), wizard);

		int result = wd.open();
		if (result == WizardDialog.OK) {
			return OK;
		}
		return CANCEL;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#setCompositeUIProvider(org.eclipse.emf.ecp.ui.common.
	 * ICompositeProvider)
	 */
	@Override
	public void setCompositeUIProvider(ICompositeProvider uiProvider) {
		wizard.setCompositeProvider(uiProvider);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractUICallback#dispose()
	 */
	@Override
	public void dispose() {
		wizard.getCompositeProvider().dispose();
	}

}
