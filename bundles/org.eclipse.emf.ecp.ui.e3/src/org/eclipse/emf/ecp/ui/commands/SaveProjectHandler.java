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
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.ui.util.ECPHandlerHelper;
import org.eclipse.emf.ecp.ui.tester.ECPSavePropertySource;
import org.eclipse.emf.ecp.ui.tester.SaveButtonEnablementObserver;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.ISourceProviderService;

/**
 * This Handler uses the {@link ECPHandlerHelper#saveProject(ECPProject)} method
 * to save changes of a dirty project.
 * 
 * @author Eugen Neufeld
 * @author Johannes Faltermeier
 */
public class SaveProjectHandler extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISourceProviderService sourceProviderService = (ISourceProviderService) HandlerUtil
			.getActiveWorkbenchWindow(event).getService(ISourceProviderService.class);
		final ECPSavePropertySource propertySource = (ECPSavePropertySource) sourceProviderService
			.getSourceProvider(ECPSavePropertySource.CURRENT_SAVE_STATE_PROPERTY);
		final ECPProject project = propertySource.getProjectToSave();
		if (project != null) {
			ECPHandlerHelper.saveProject(project);
			ECPUtil.getECPObserverBus().notify(SaveButtonEnablementObserver.class)
				.notifyChangeButtonState(null, false);
		}
		return null;
	}

}
