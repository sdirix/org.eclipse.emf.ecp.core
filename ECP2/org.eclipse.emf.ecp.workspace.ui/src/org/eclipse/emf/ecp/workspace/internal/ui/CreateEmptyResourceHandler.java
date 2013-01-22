/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.workspace.internal.ui;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.workspace.internal.core.ResourceWrapper;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.handlers.HandlerUtil;

import java.io.IOException;

/**
 * @author Eugen Neufeld
 * 
 */
public class CreateEmptyResourceHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		InputDialog id = new InputDialog(HandlerUtil.getActiveShell(event), "Name of Resource",
			"Please enter the name for the resource", "my.resource", null);
		int result = id.open();
		if (Window.OK == result) {
			String name = id.getValue();
			ResourceWrapper<InternalRepository> wrapper = (ResourceWrapper<InternalRepository>) ((IStructuredSelection) HandlerUtil
				.getCurrentSelection(event)).getFirstElement();
			ResourceSet set = new ResourceSetImpl();

			Resource res = set.createResource(wrapper.getURI().appendSegment(name));
			try {
				res.save(null);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}

		return null;
	}

}
