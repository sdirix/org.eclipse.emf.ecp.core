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
import org.eclipse.emf.ecp.ui.util.HandlerHelper;
import org.eclipse.emf.ecp.wizards.CreateProjectWizard;
import org.eclipse.emf.ecp.wizards.WizardUICallback;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This Handler uses the {@link HandlerHelper#createProject(org.eclipse.emf.ecp.ui.common.AbstractUICallback)} method
 * to create a new project.
 * 
 * @author Eugen Neufeld
 */
public class CreateProjectHandler extends AbstractHandler {

	/** {@inheritDoc} */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		HandlerHelper.createProject(new WizardUICallback(HandlerUtil.getActiveShell(event),
			new CreateProjectWizard()));
		return null;
	}
}
