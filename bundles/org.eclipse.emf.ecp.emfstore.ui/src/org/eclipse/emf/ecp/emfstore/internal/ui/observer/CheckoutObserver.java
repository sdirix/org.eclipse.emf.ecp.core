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
 ******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.observer;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.observer.ESCheckoutObserver;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import java.text.DateFormat;
import java.util.Date;

public class CheckoutObserver implements ESCheckoutObserver {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.emfstore.client.observer.ESCheckoutObserver#checkoutDone(org.eclipse.emf.emfstore.client.
	 * ESLocalProject)
	 */
	public void checkoutDone(ESLocalProject project) {

		boolean ecpProjectExists = false;
		boolean validProjectName = false;

		for (ECPProject ecpProject : ECPUtil.getECPProjectManager().getProjects()) {
			InternalProject internalProject = (InternalProject) ecpProject;
			Object localProject = internalProject.getProviderSpecificData();
			if (localProject instanceof ESLocalProject) {
				if (localProject == project) {
					ecpProjectExists = true;
					break;
				}
			}
		}

		String projectName = project.getProjectName();

		if (!ecpProjectExists) {
			while (!validProjectName) {
				try {
					ECPUtil.getECPProjectManager().createProject(EMFStoreProvider.INSTANCE.getProvider(), projectName,
						createECPProperties(project));
					validProjectName = true;
				} catch (ECPProjectWithNameExistsException ex) {
					InputDialog id = new InputDialog(Display.getCurrent().getActiveShell(), "Create project",
						"Enter name for checked out project:", project.getProjectName() + "@" + createDateString(),
						new IInputValidator() {

							public String isValid(String newText) {
								if (ECPUtil.getECPProjectManager().getProject(newText) == null) {
									return null;
								}
								return "A project with this name already exists!";

							}
						});
					int inputResult = id.open();
					if (Window.OK != inputResult) {
						// cancel, provide default name
						projectName = project.getProjectName() + "@" + createDateString();
					} else {
						projectName = id.getValue();
					}
				}
			}
		}
	}

	private ECPProperties createECPProperties(ESLocalProject project) {
		ECPProperties projectProperties = ECPUtil.createProperties();
		projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, project.getLocalProjectId().getId());
		return projectProperties;
	}

	private String createDateString() {
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
		return format.format(new Date());
	}

}
