/*******************************************************************************
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.ecp.cdo.internal.ui.handlers;

import org.eclipse.emf.cdo.workspace.CDOWorkspace;

import org.eclipse.net4j.util.AdapterUtil;

import org.eclipse.emf.ecp.cdo.internal.ui.Activator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.IEvaluationService;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
public abstract class AbstractWorkspaceHandler extends AbstractHandler {
	private final String jobName;

	public AbstractWorkspaceHandler(String jobName) {
		this.jobName = jobName;
	}

	public final String getJobName() {
		return jobName;
	}

	/** {@inheritDoc} */
	public final Object execute(final ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();

			final CDOWorkspace workspace = AdapterUtil.adapt(element, CDOWorkspace.class);
			if (workspace != null) {
				try {
					if (jobName == null) {
						PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
							public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
								try {
									execute(event, workspace, monitor);
								} catch (Exception ex) {
									Activator.log(ex);
								}
							}
						});
					} else {
						new Job(jobName) {
							@Override
							protected IStatus run(IProgressMonitor monitor) {
								try {
									execute(event, workspace, monitor);
									return Status.OK_STATUS;
								} catch (Exception ex) {
									return new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex);
								}
							}
						}.schedule();
					}
				} catch (Exception ex) {
					throw new ExecutionException("Problem while handling " + element, ex);
				}
			}
		}

		return null;
	}

	protected abstract void execute(ExecutionEvent event, CDOWorkspace workspace, IProgressMonitor monitor)
		throws Exception;

	public static void refreshDirtyState(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow ww = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IEvaluationService service = (IEvaluationService) ww.getService(IEvaluationService.class);
		if (service != null) {
			service.requestEvaluation("org.eclipse.emf.cdo.workspace.dirty");
		}
	}
}
