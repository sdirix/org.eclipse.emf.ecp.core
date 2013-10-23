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
package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProjectWrapper;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UICheckoutController;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the EMFStore Checkout Branch Handler delegating to the EMFStore {@link UICheckoutController}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class CheckoutBranchHandler extends AbstractHandler {
	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		if (sel instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) sel;
			Object selection = ssel.getFirstElement();
			if (selection instanceof EMFStoreProjectWrapper) {
				EMFStoreProjectWrapper projectWrapper = (EMFStoreProjectWrapper) selection;

				checkoutBranch(projectWrapper, HandlerUtil.getActiveShell(event));

			}
		}

		return null;
	}

	/**
	 * @param projectWrapper
	 */
	private void checkoutBranch(EMFStoreProjectWrapper projectWrapper, Shell shell) {
		new UICheckoutController(shell, projectWrapper.getCheckoutData(), true).execute();
	}
}
