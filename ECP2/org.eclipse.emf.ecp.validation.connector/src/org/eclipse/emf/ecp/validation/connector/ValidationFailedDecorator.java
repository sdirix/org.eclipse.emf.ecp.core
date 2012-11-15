/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
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
package org.eclipse.emf.ecp.validation.connector;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * This class decorates the navigator if the validation of a project fails.
 * 
 * @author Eugen Neufeld
 * @author emueller
 */
public class ValidationFailedDecorator extends LabelProvider implements ILightweightLabelDecorator {

	/**
	 * {@inheritDoc}
	 */
	public void decorate(final Object element, IDecoration decoration) {
				
		if (!(element instanceof EObject) && !(element instanceof ECPProject)) {
			return;
		}
		
		Integer severity = null;
		
		if (element instanceof EObject) {

			ECPProject project = getProject((EObject) element);
			
			if (project != null) {
				severity = Activator.getDefault().getValidationService(project).getDiagnostic(element).getSeverity();
			}
		} else if (element instanceof ECPProject) {
			severity = Activator.getDefault().getValidationService((ECPProject) element).getRootDiagnostic().getSeverity();
		}
		
		if (severity == null) {
			return;
		}
		
		switch (severity.intValue()) {
		case Diagnostic.ERROR:
			decoration.addOverlay(Activator.getImageDescriptor("icons/error_decorate.png"), IDecoration.BOTTOM_RIGHT);
			break;
		case Diagnostic.WARNING:
			decoration.addOverlay(Activator.getImageDescriptor("icons/warning_decorate.png"), IDecoration.BOTTOM_RIGHT);
			break;
		case Diagnostic.OK:
			decoration.addOverlay(null);
			break;
		default:
			break;
		}

	}

	// TODO: replace with util method, when available
	private ECPProject getProject(EObject element) {
		for (ECPProject project : ECPProjectManager.INSTANCE.getProjects()) {
			if (project.contains(element)) {
				return project;
			}
		}
		
		return null;
	}
}
