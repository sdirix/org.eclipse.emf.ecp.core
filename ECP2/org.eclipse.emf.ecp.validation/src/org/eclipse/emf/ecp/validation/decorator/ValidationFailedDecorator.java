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
package org.eclipse.emf.ecp.validation.decorator;

import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecp.validation.Activator;
import org.eclipse.emf.ecp.validation.ValidationService;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * This class decorates the navigator if the validation of a project fails.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ValidationFailedDecorator extends LabelProvider implements ILightweightLabelDecorator {

	@Override
	public void decorate(Object element, IDecoration decoration) {

		Map<Object, Integer> severityMapping = ValidationService.INSTANCE.getViolationMapping();
		Integer severity = severityMapping.get(element);
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
		default:
			break;
		}

	}

}
