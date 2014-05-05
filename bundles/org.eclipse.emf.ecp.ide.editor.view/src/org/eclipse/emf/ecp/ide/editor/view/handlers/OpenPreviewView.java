/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ide.editor.view.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ide.editor.view.Activator;
import org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal.MasterDetailAction;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Alexandra Buzila
 * 
 */
public class OpenPreviewView extends MasterDetailAction {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.showView("org.eclipse.emf.ecp.view.model.internal.preview.e3.views.PreviewView"); //$NON-NLS-1$
		} catch (final PartInitException e) {
			Activator
				.getDefault()
				.getLog()
				.log(new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(), e.getMessage(), e));
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal.MasterDetailAction#execute(EObject)
	 */
	@Override
	public void execute(EObject object) {
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		try {
			page.showView("org.eclipse.emf.ecp.view.model.preview.e3.views.PreviewView"); //$NON-NLS-1$
		} catch (final PartInitException e) {
			Activator
				.getDefault()
				.getLog()
				.log(new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(), e.getMessage(), e));
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal.MasterDetailAction#shouldShow(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	public boolean shouldShow(EObject eObject) {
		return true;
	}

}