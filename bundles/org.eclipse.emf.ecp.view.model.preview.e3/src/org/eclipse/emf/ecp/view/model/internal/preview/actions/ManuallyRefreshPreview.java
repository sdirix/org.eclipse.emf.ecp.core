package org.eclipse.emf.ecp.view.model.internal.preview.actions;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.emf.ecp.view.model.internal.preview.e3.views.PreviewView;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;

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

/**
 * @author Alexandra Buzila
 * 
 */
public class ManuallyRefreshPreview extends AbstractHandler implements IElementUpdater {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		final IViewPart viewPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.findView("org.eclipse.emf.ecp.view.model.preview.e3.views.PreviewView"); //$NON-NLS-1$
		if (viewPart != null && viewPart instanceof PreviewView) {
			((PreviewView) viewPart).render();
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.IHandler#isEnabled()
	 */
	@Override
	public boolean isEnabled() {

		//		System.out.println("REFRESH ISENABLED"); //$NON-NLS-1$
		return !getAutomaticUpdateStateValue();

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.commands.IElementUpdater#updateElement(org.eclipse.ui.menus.UIElement, java.util.Map)
	 */
	@Override
	public void updateElement(UIElement element, @SuppressWarnings("rawtypes") Map parameters) {
		element.setChecked(!getAutomaticUpdateStateValue());
	}

	private boolean getAutomaticUpdateStateValue() {
		final ICommandService service =
			(ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		final Command command = service
			.getCommand("org.eclipse.emf.ecp.view.model.internal.preview.e3.ToggleAutomaticUpdate"); //$NON-NLS-1$
		final State state = command.getState("org.eclipse.emf.ecp.view.model.internal.preview.e3.autoUpdateState"); //$NON-NLS-1$

		return (Boolean) state.getValue();
	}
}
