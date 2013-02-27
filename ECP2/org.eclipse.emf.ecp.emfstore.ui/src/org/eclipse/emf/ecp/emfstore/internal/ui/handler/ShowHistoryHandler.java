/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.handler;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.internal.client.ui.controller.UIShowHistoryController;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the EMFStore ShowHistory Handler delegating to the EMFStore {@link UIShowHistoryController}.
 * 
 * @author Tobias Verhoeven
 * 
 */
public class ShowHistoryHandler extends AbstractHandler {

	/** {@inheritDoc} **/
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object object = ((IStructuredSelection) HandlerUtil.getActiveMenuSelection(event)).getFirstElement();
		EObject result = null;

		if (object instanceof EObject) {
			result = (EObject) object;
		} else if (object instanceof InternalProject) {
			result = (EObject) EMFStoreProvider.INSTANCE.getProjectSpace((InternalProject) object);
		}

		if (result != null) {
			new UIShowHistoryController(HandlerUtil.getActiveShell(event), result).execute();
		}

		return null;
	}
}