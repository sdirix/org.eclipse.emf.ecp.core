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

package org.eclipse.emf.ecp.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.core.util.ECPCloseable;
import org.eclipse.emf.ecp.ui.util.ECPHandlerHelper;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This Handler uses the {@link ECPHandlerHelper#changeCloseState(ECPCloseable[], String)} method
 * to close selected projects.
 * 
 * @author Eugen Neufeld
 */
public class CloseableHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection ssel = (IStructuredSelection) selection;
		String currentType = event.getParameter("org.eclipse.emf.ecp.ecpclosable.type");
		Object[] selectionArray =  ssel.toArray();
		ECPCloseable[] closeable = new ECPCloseable[selectionArray.length];
		
		for (int i = 0; i < selectionArray.length; i++) {
			closeable[i] = (ECPCloseable) selectionArray[i];
		}
		
		ECPHandlerHelper.changeCloseState(closeable, currentType);

		return null;
	}

}
