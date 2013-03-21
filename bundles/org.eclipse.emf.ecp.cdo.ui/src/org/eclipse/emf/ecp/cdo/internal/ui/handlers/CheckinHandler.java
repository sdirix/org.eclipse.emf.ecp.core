/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.cdo.internal.ui.handlers;

import org.eclipse.emf.cdo.util.CDOUtil;
import org.eclipse.emf.cdo.workspace.CDOWorkspace;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Eike Stepper
 */
public class CheckinHandler extends AbstractWorkspaceHandler {
	public CheckinHandler() {
		super("Updating...");
	}

	@Override
	protected void execute(ExecutionEvent event, CDOWorkspace workspace, IProgressMonitor monitor) throws Exception {
		// TODO remove
		CDOUtil.setLegacyModeDefault(true);
		workspace.checkin("Workspace checkin");
		refreshDirtyState(event);
	}
}
