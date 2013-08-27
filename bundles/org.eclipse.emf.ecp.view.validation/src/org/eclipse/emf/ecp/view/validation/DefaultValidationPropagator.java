/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VDiagnostic;
import org.eclipse.emf.ecp.view.model.ViewFactory;

/**
 * Default propagator for the view validation service.
 * 
 * @author jfaltermeier
 * 
 */
public class DefaultValidationPropagator implements ECPValidationPropagator {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.validation.ECPValidationPropagator#canHandle(org.eclipse.emf.ecp.view.model.Renderable)
	 */
	public int canHandle(Renderable renderable) {
		return !(renderable instanceof AbstractControl) ? 1 : 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.validation.ECPValidationPropagator#propagate(org.eclipse.emf.ecp.view.model.Renderable)
	 */
	public void propagate(Renderable renderable) {
		final VDiagnostic vDiagnostic = ViewFactory.eINSTANCE.createVDiagnostic();
		if (isRelevantForPropagation(renderable)) {
			final Set<Object> childDiagnostics = new HashSet<Object>();
			for (final EObject o : renderable.eContents()) {
				if (o instanceof Renderable) {
					final Renderable childRenderable = (Renderable) o;
					childDiagnostics.addAll(childRenderable.getDiagnostic().getDiagnostics());
				}
			}

			if (childDiagnostics.isEmpty()) {
				childDiagnostics.add(Diagnostic.OK_INSTANCE);
			}

			vDiagnostic.getDiagnostics().addAll(childDiagnostics);

		} else {
			vDiagnostic.getDiagnostics().add(Diagnostic.OK_INSTANCE);

		}
		if (!VDiagnosticHelper.isEqual(renderable.getDiagnostic(), vDiagnostic)) {
			renderable.setDiagnostic(vDiagnostic);
		}
	}

	/**
	 * Check whether the validation result of given {@link Renderable} is relevant.
	 * 
	 * @param renderable the Renderable to check
	 * @return <code>true</code> if renderable is enabled, visible and not read only, <code>false</code> otherwise
	 */
	private boolean isRelevantForPropagation(Renderable renderable) {
		return true;
	}

}
